/**
 * 
 */
package de.jaschastarke.bukkit.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.yaml.snakeyaml.Yaml;

import de.jaschastarke.minecraft.lib.permissions.IHasDescription;
import de.jaschastarke.minecraft.lib.permissions.IPermission;
import de.jaschastarke.minecraft.lib.permissions.IPermissionContainer;
import de.jaschastarke.utils.ClassHelper;

/**
 * @author Jascha
 * @goal pluginyaml
 */
public class GeneratePluginYamlMojo extends AbstractMojo {

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
     * @parameter default-value="{project.build.directory}/generated-sources/annotations/META-INF"
     * @required 
     */
    private String directory;
    
    /**
    * The project currently being built.
    *
    * @parameter expression="${project}"
    * @readonly
    * @required
    */
    private MavenProject project;
    
    
    /* (non-Javadoc)
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        
        // TODO: need classloader
        //project.getBuild().
        
        Map<String, Object> data = new HashMap<String, Object>();
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
        
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(data, writer);
        
        getLog().info(writer.toString());
    }
    
    private List<Object> getPermissions() throws MojoFailureException {
        List<Object> list = new ArrayList<Object>();
        
        for (String cls : this.registeredPermissions) {
            try {
                Class<?> pclass = ClassHelper.forName(cls);
                if (pclass.isInstance(IPermissionContainer.class)) {
                    addPermissionsToList(list, (IPermissionContainer) pclass.newInstance());
                } else if (pclass.isInstance(IPermission.class)) {
                    addPermissionToList(list, (IPermission) pclass.newInstance());
                }
            } catch (ClassNotFoundException e) {
                throw new MojoFailureException("registeredPermission class not found: " + cls);
            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        return list;
    }
    private void addPermissionsToList(List<Object> list, IPermissionContainer perms) {
        for (IPermission perm : perms.getPermissions()) {
            addPermissionToList(list, perm);
        }
    }
    private void addPermissionToList(List<Object> list, IPermission perm) {
        addPermissionToList(list, perm, null);
    }
    private void addPermissionToList(List<Object> list, IPermission perm, String description) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("default", perm.getDefault());
        if (perm instanceof IHasDescription) {
            data.put("description", ((IHasDescription) perm).getDescription());
        } else if (description != null) {
            data.put("description", description);
        } else if (loadDescriptions().containsKey(perm.getFullString())) {
            data.put("description", loadDescriptions().get(perm.getFullString()));
        }
    }
    
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
    }
}
