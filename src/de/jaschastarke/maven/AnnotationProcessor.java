package de.jaschastarke.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import de.jaschastarke.utils.ClassDescriptorStorage;
import de.jaschastarke.utils.ClassDescriptorStorage.ClassDescription;

@SupportedAnnotationTypes("de.jaschastarke.maven.ArchiveDocComments")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class AnnotationProcessor extends AbstractProcessor {
    /**
     * Workaround because processor is called twice by maven-compiler-plugin
     */
    private static boolean already_run = false;
    
    private Elements elementUtils;
    //private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        //typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (already_run)
            return false;
        already_run = true;
        
        ClassDescriptorStorage cds = ClassDescriptorStorage.getInstance();
        
        for (Element elem: roundEnv.getElementsAnnotatedWith(ArchiveDocComments.class)) {
            //ArchiveDocComments annot = elem.getAnnotation(ArchiveDocComments.class);
            
            TypeElement telem = (TypeElement) elem; // Because the Annotation is targeted to Types only
            Name name = elementUtils.getBinaryName(telem);
            System.out.println(name.toString());
            
            ClassDescription cd = cds.getClassFor(name.toString());
            
            List<? extends Element> members = elementUtils.getAllMembers(telem);
            for (Element symbol : members) {
                String descr = elementUtils.getDocComment(symbol);
                if (descr != null)
                    cd.setElDocComment(symbol.getSimpleName().toString(), descr.trim());
            }
            
            String descr = elementUtils.getDocComment(elem);
            if (descr != null)
                cd.setDocComment(descr.trim());
        }

        try {
            FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "META-INF/descriptions.jos");
            cds.store(new File(resource.getName()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        /*try {
            FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "META-INF/descriptions.xml");
            descriptions.storeToXML(resource.openOutputStream(), null, "UTF-8");
            /*PrintWriter writer = new PrintWriter(resource.openWriter());
            for (Entry<String, String> descr : descriptions.entrySet()) {
                System.out.println(descr.getKey()+" "+descr.getValue());
                writer.print(descr.getKey());
                writer.print(": ");
                writer.println(descr.getValue());
            }
            writer.flush();* /
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        
        return false;
    }

}
