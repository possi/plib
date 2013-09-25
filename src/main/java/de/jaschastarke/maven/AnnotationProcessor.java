package de.jaschastarke.maven;

import java.io.File;
import java.io.FileWriter;
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
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import de.jaschastarke.bukkit.lib.configuration.Configuration;
import de.jaschastarke.utils.ClassDescriptorStorage;
import de.jaschastarke.utils.ClassDescriptorStorage.ClassDescription;

@SupportedAnnotationTypes({
    "de.jaschastarke.maven.ArchiveDocComments",
    "de.jaschastarke.maven.PluginCommand",
    "de.jaschastarke.maven.PluginConfigurations",
    "de.jaschastarke.maven.PluginPermissions"
})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class AnnotationProcessor extends AbstractProcessor {
    private static final String REGISTERED_COMMANDS = "registeredCommands";
    private static final String REGISTERED_PERMISSIONS = "registeredPermissions";
    private static final String REGISTERED_CONFIGURATIONS = "registeredConfigurations";
    private static final String PARENT_SUFFIX = "Parent";
    /**
     * Workaround because processor is called twice by maven-compiler-plugin
     */
    private static boolean alreadyRun = false;
    
    private Elements elementUtils;
    //private Types typeUtils;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        //typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        if (alreadyRun)
            return false;
        alreadyRun = true;
        
        ClassDescriptorStorage cds = new ClassDescriptorStorage();
        
        for (Element elem : roundEnv.getElementsAnnotatedWith(ArchiveDocComments.class)) {
            TypeElement telem = (TypeElement) elem; // Because the Annotation is targeted to Types only
            Name name = elementUtils.getBinaryName(telem);
            
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
            FileObject resource = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", cds.getTargetPath());
            cds.store(new File(resource.toUri()));
        } catch (IOException e) {
            throw new RuntimeException("Annotation processor failed to write output file", e);
        }
        
        
        Properties registration = new Properties();
        int permissions = 0;
        int commands = 0;
        int configs = 0;
        for (Element elem : roundEnv.getElementsAnnotatedWith(PluginPermissions.class)) {
            String identifier;
            if (elem.getKind() == ElementKind.CLASS) {
                TypeElement klass = (TypeElement) elem;
                identifier = klass.getQualifiedName().toString();
            } else if (elem.getKind() == ElementKind.FIELD) {
                TypeElement klass = (TypeElement) elem.getEnclosingElement();
                identifier = klass.getQualifiedName().toString() + ":" + elem.getSimpleName();
            } else {
                throw new RuntimeException("@PluginPermission-Annotation on invalid Type: " + elem.getKind().toString());
            }
            registration.setProperty(REGISTERED_PERMISSIONS + (++permissions), identifier);
        }
        for (Element elem : roundEnv.getElementsAnnotatedWith(PluginCommand.class)) {
            TypeElement klass = (TypeElement) elem;
            String identifier = klass.getQualifiedName().toString();
            registration.setProperty(REGISTERED_COMMANDS + (++commands), identifier);
        }
        for (Element elem : roundEnv.getElementsAnnotatedWith(PluginConfigurations.class)) {
            PluginConfigurations annot = elem.getAnnotation(PluginConfigurations.class);
            TypeElement klass = (TypeElement) elem;
            String identifier = klass.getQualifiedName().toString();
            registration.setProperty(REGISTERED_CONFIGURATIONS + (++configs), identifier);
            try {
                if (annot.parent() != Configuration.class)
                    registration.setProperty(REGISTERED_CONFIGURATIONS + configs + PARENT_SUFFIX, annot.parent().getName());
            } catch (MirroredTypeException mte) {
                String annotParentName = ((TypeElement) (((DeclaredType) mte.getTypeMirror()).asElement())).getQualifiedName().toString();
                if (!annotParentName.equals(Configuration.class.getName()))
                    registration.setProperty(REGISTERED_CONFIGURATIONS + configs + PARENT_SUFFIX, annotParentName);
            }
        }
        
        try {
            FileObject resource = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "GeneratePluginYamlMojo.properties");
            registration.store(new FileWriter(new File(resource.toUri())), "Auto generated File, only for Maven plugin.yml generation");
        } catch (IOException e) {
            throw new RuntimeException("Annotation processor failed to write output file", e);
        }
        
        return false;
    }

}
