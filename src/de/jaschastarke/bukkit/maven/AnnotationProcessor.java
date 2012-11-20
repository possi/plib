package de.jaschastarke.bukkit.maven;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import de.jaschastarke.minecraft.lib.annotations.PermissionDescripted;
import de.jaschastarke.minecraft.lib.annotations.PermissionDescripted.Type;

@SupportedAnnotationTypes("de.jaschastarke.minecraft.lib.annotations.PermissionDescripted")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class AnnotationProcessor extends AbstractProcessor {
    /**
     * Workaround because processor is called twice by maven-compiler-plugin
     */
    private static boolean already_run = false;
    
    private Elements elementUtils;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (already_run)
            return false;
        already_run = true;
        
        Properties descriptions = new Properties();
        
        for (Element elem: roundEnv.getElementsAnnotatedWith(PermissionDescripted.class)) {
            PermissionDescripted annot = elem.getAnnotation(PermissionDescripted.class);
            
            TypeElement telem = (TypeElement) elem; // Because the Annotation is targeted to Types only
            Name name = elementUtils.getBinaryName(telem);
            
            TypeMirror IPermType = elementUtils.getTypeElement("de.jaschastarke.minecraft.lib.permissions.IPermission").asType();
            
            if (annot.value() == Type.STATIC_ATTRIBUTES) {
                List<? extends Element> members = elementUtils.getAllMembers(telem);
                for (Element symbol : members) {
                    if (symbol.getKind() == ElementKind.ENUM_CONSTANT ||
                            (symbol.getKind() == ElementKind.FIELD && symbol.getModifiers().contains(Modifier.STATIC))) {
                        
                        TypeMirror type = symbol.asType();
                        
                        if (typeUtils.isSubtype(type, IPermType)) {
                            String descr = elementUtils.getDocComment(symbol);
                            if (descr != null) {
                                descriptions.setProperty(name.toString()+"#"+symbol.getSimpleName(), descr.trim());
                            }
                        }
                    }
                }
            } else {
                String descr = elementUtils.getDocComment(elem);
                if (descr != null) {
                    descriptions.setProperty(name.toString(), descr);
                }
            }
        }
        
        try {
            FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "META-INF/descriptions.xml");
            descriptions.storeToXML(resource.openOutputStream(), null, "UTF-8");
            /*PrintWriter writer = new PrintWriter(resource.openWriter());
            for (Entry<String, String> descr : descriptions.entrySet()) {
                System.out.println(descr.getKey()+" "+descr.getValue());
                writer.print(descr.getKey());
                writer.print(": ");
                writer.println(descr.getValue());
            }
            writer.flush();*/
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return false;
    }

}
