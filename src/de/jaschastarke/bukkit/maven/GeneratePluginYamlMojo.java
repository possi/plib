/**
 * 
 */
package de.jaschastarke.bukkit.maven;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.bukkit.permissions.PermissionDefault;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import de.jaschastarke.maven.AbstractExecMojo;
import de.jaschastarke.minecraft.lib.permissions.IHasDescription;
import de.jaschastarke.minecraft.lib.permissions.IPermission;
import de.jaschastarke.minecraft.lib.permissions.IPermissionContainer;
import de.jaschastarke.utils.ClassDescriptorStorage;
import de.jaschastarke.utils.ClassDescriptorStorage.ClassDescription;
import de.jaschastarke.utils.DocComment;
import de.jaschastarke.utils.ClassHelper;

/**
 * 
 * Example Usage:
 * <pre>{@code
 *    <plugin>
 *        <groupId>org.apache.maven.plugins</groupId>
 *        <artifactId>maven-compiler-plugin</artifactId>
 *        <version>2.3.2</version>
 *        <configuration>
 *            <source>1.6</source>
 *            <target>1.6</target>
 *            <annotationProcessors>
 *              <!-- Needed to fetch DocComments from Source -->
 *              <annotationProcessor>de.jaschastarke.maven.AnnotationProcessor</annotationProcessor>
 *            </annotationProcessors>
 *        </configuration>
 *    </plugin>
 *    <plugin>
 *      <groupId>de.jaschastarke</groupId>
 *      <artifactId>plib</artifactId>
 *      <version>0.1-SNAPSHOT</version>
 *      <executions>
 *        <execution>
 *          <phase>compile</phase>
 *          <goals>
 *            <goal>pluginyaml</goal>
 *          </goals>
 *          <configuration>
 *            <!-- plugin.yml -->
 *            <mainClass>de.jaschastarke.minecraft.limitedcreative.LimitedCreative</mainClass>
 *            <softdepend>
 *              <param>WorldGuard</param>
 *              <param>WorldEdit</param>
 *              <param>MultiInv</param>
 *            </softdepend>
 *            <custom>
 *              <dev-url>http://dev.bukkit.org/server-mods/limited-creative/</dev-url>
 *            </custom>
 *            <registeredPermissions>
 *              <param>de.jaschastarke.minecraft.limitedcreative.Perms:Root</param>
 *            </registeredPermissions>
 *          </configuration>
 *        </execution>
 *      </executions>
 *    </plugin>
 * }</pre>
 * @author Jascha
 * @goal pluginyaml
 * @requiresDependencyResolution compile
 */
public class GeneratePluginYamlMojo extends AbstractExecMojo {

    /**
     * @parameter default-value="${project.version}"
     */
    private String version;
    
    /**
     * @parameter default-value="${project.name}"
     */
    private String name;
    
    /**
     * @parameter
     */
    private String mainClass;
    
    /**
     * @parameter
     */
    private String[] dependencies;
    
    /**
     * @parameter
     */
    private String[] softdepend;
    
    /**
     * @parameter
     */
    private Properties custom;
    
    /**
     * @parameter
     */
    private List<String> registeredPermissions;
    
    /**
     * @parameter default-value="${project.build.outputDirectory}/META-INF"
     * @required 
     */
    private String meta_directory;
    
    /**
     * @parameter default-value="${project.build.outputDirectory}"
     * @required 
     */
    private String target_directory;

    private ClassDescriptorStorage cds;
    
    private URLClassLoader loader;
    
    /* (non-Javadoc)
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            cds = ClassDescriptorStorage.load(new File(this.meta_directory + "/descriptions.jos"));
        } catch (IOException e) {
            throw new MojoExecutionException("Failed to load class doccomments", e);
        }

        List<URL> classpathURLs = new ArrayList<URL>();
        //this.addRelevantPluginDependenciesToClasspath(classpathURLs);
        this.addRelevantProjectDependenciesToClasspath(classpathURLs);
        loader = new URLClassLoader(classpathURLs.toArray(new URL[classpathURLs.size()]), getClass().getClassLoader());
        
        /*try {
            URL url = new File(this.classes_directory).toURI().toURL();
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{url});
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("main", this.mainClass);
        data.put("name", this.name);
        data.put("version", this.version);
        if (this.dependencies != null)
            data.put("dependencies", this.dependencies);
        if (this.softdepend != null)
            data.put("softdepend", this.softdepend);
        
        if (this.custom != null) {
            for (Entry<Object, Object> property : this.custom.entrySet()) {
                data.put((String) property.getKey(), property.getValue());
            }
        }
        
        if (this.registeredPermissions != null) {
            data.put("permissions", this.getPermissions());
        }
        
        DumperOptions options = new DumperOptions();
        options.setWidth(80);
        options.setIndent(4);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        StringWriter writer = new StringWriter();
        yaml.dump(data, writer);
        
        //getLog().info(writer.toString());
        try {
            FileWriter fw = new FileWriter(new File(this.target_directory + "/plugin.yml"));
            fw.write(writer.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private Map<String, Object> getPermissions() throws MojoFailureException {
        Map<String, Object> list = new HashMap<String, Object>();
        
        //loader.loadClass(arg0)
        
        for (String cls : this.registeredPermissions) {
            try {
                Object pobj;
                try {
                    pobj = ClassHelper.getInstance(cls, loader);
                } catch (SecurityException e) {
                    e.printStackTrace();
                    throw new MojoFailureException("registeredPermission class not found: " + cls);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    throw new MojoFailureException("registeredPermission class not found: " + cls);
                } catch (IllegalArgumentException e) {
                    throw new MojoFailureException("registeredPermission class could not be instanciated: " + cls);
                } catch (InvocationTargetException e) {
                    throw new MojoFailureException("registeredPermission class could not be instanciated: " + cls);
                }
                //Class<?> pclass = pobj.getClass();
                //Class<?> pclass = ClassHelper.forName(cls, loader);


                //if (IPermissionContainer.class.isAssignableFrom(pclass)) {
                
                //if (IPermissionContainer.class.isInstance(pobj)) {
                if (pobj instanceof IPermissionContainer) {
                    IPermissionContainer container = (IPermissionContainer) pobj;
                    addPermissionsToList(list, container);
                }
                
                /*
                if (pclass.isInstance(IPermissionContainer.class)) {
                    addPermissionsToList(list, (IPermissionContainer) pclass.newInstance());
                }
                if (pclass.isInstance(IPermission.class)) {
                    ClassDescription cd = cds.getClassFor(cls);
                    if (cd.getDocComment() != null) {
                        addPermissionToList(list, (IPermission) pclass.newInstance(), cd.getDocComment().toString());
                    } else {
                        addPermissionToList(list, (IPermission) pclass.newInstance());
                    }
                }*/
            } catch (ClassNotFoundException e) {
                throw new MojoFailureException("registeredPermission class not found: " + cls);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        
        return list;
    }
    private void addPermissionsToList(Map<String, Object> list, IPermissionContainer perms) {
        for (IPermission perm : perms.getPermissions()) {
            DocComment dc = getContainerPropertyDoc(perms, perm);
            if (dc != null) {
                addPermissionToList(list, perm, dc.getDescription());
            } else {
                addPermissionToList(list, perm);
            }
        }
    }
    private void addPermissionToList(Map<String, Object> list, IPermission perm) {
        if (perm instanceof Enum) {
            DocComment dc = getEnumPropertyDoc((Enum<?>) perm);
            if (dc != null) {
                addPermissionToList(list, perm, dc.getDescription());
            } else {
                addPermissionToList(list, perm, null);
            }
        } else {
            addPermissionToList(list, perm, null);
        }
    }
    private DocComment getContainerPropertyDoc(IPermissionContainer set, IPermission perm) {
        ClassDescription cd = cds.getClassFor(set);
        Class<?> cls = set.getClass();
        for (Field field : cls.getFields()) {
            try {
                if (Modifier.isPublic(field.getModifiers())) {
                    if (Modifier.isStatic(field.getModifiers()) && field.get(null) == perm) {
                        return cd.getElDocComment(field.getName());
                    } else if (field.get(set) == perm) {
                        return cd.getElDocComment(field.getName());
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private DocComment getEnumPropertyDoc(Enum<?> set) {
        ClassDescription cd = cds.getClassFor(set);
        Class<?> cls = set.getClass();
        for (Field field : cls.getFields()) {
            try {
                if (field.isEnumConstant() && field.get(null) == set) {
                    return cd.getElDocComment(field.getName());
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private void addPermissionToList(Map<String, Object> list, IPermission perm, String description) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (perm.getDefault() == PermissionDefault.TRUE) {
            data.put("default", true);
        } else if (perm.getDefault() == PermissionDefault.FALSE) {
            data.put("default", false);
        } else {
            data.put("default", perm.getDefault().toString());
        }
        if (perm instanceof IHasDescription) {
            data.put("description", ((IHasDescription) perm).getDescription());
        } else if (description != null) {
            data.put("description", description);
        }
        list.put(perm.getFullString(), data);
    }
    /*
    private Map<String, String> _descriptions = null;
    @SuppressWarnings("unchecked")
    private Map<String, String> loadDescriptions() {
        if (_descriptions == null) {
            _descriptions = new HashMap<String, String>();
            File file = new File(this.directory + "/descriptions.xml");
            
            Properties prop = new Properties();
            try {
                prop.loadFromXML(new FileInputStream(file));
            } catch (InvalidPropertiesFormatException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            for (Entry<Object, Object> property : prop.entrySet()) {
                String[] cls = property.getKey().toString().split("#");
                
                try {
                    IPermission permission = null;
                    Class<?> permclass = Class.forName(cls[0]);
                    if (cls.length > 1) {
                        Field field = permclass.getField(cls[1]);
                        if (field != null && Modifier.isStatic(field.getModifiers())) {
                            permission = (IPermission) field.get(null);
                        }
                    } else {
                        if (permclass.isInstance(IPermission.class)) {
                            permission = ((Class<? extends IPermission>) permclass).newInstance();
                        }
                    }
                    if (permission != null)
                        _descriptions.put(permission.getFullString(), (String) property.getValue());
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return _descriptions;
    }*/
}
