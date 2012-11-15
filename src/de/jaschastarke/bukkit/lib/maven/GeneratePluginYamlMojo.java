/**
 * 
 */
package de.jaschastarke.bukkit.lib.maven;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.yaml.snakeyaml.Yaml;

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
    
    /* (non-Javadoc)
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
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
        
        Yaml yaml = new Yaml();
        StringWriter writer = new StringWriter();
        yaml.dump(data, writer);
        
        getLog().info(writer.toString());
    }

}
