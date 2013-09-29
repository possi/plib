package de.jaschastarke.bukkit.lib.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import de.jaschastarke.configuration.ConfigurationStyle;
import de.jaschastarke.configuration.IBaseConfigurationNode;
import de.jaschastarke.configuration.IConfiguration;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.configuration.IConfigurationSubGroup;
import de.jaschastarke.utils.StringUtil;

public class YamlConfigurationDumper {
    protected DumperOptions yamlOptions = new DumperOptions();
    protected Representer yamlRepresenter = new YamlRepresenter();
    protected Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);
    protected Configuration config;
    private static final Pattern LINE_START = Pattern.compile("^", Pattern.MULTILINE);
    private static final int WRAP_SIZE = 80;
    private static final String COMMENT_PREFIX = "# ";
    
    public YamlConfigurationDumper(final Configuration conf) {
        //ClassDescriptorStorage.load(new File("META-INF/descriptions.jos"));
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        config = conf;
    }
    
    public void store(final File toFile) throws IOException {
        FileWriter writer = new FileWriter(toFile);
        writer.write(getConfigurationYamlPart(config));
        writer.close();
    }
    
    private String getConfigurationYamlPart(final IConfiguration conf) {
        return getConfigurationYamlPart(conf, 0);
    }
    
    /**
     * @TODO: do better by extending snakeYaml
     */
    private String getConfigurationYamlPart(final IConfiguration conf, final int level) {
        int indention = yamlOptions.getIndent() * level;
        yamlOptions.setWidth(WRAP_SIZE - indention);
        
        StringBuffer confsect = new StringBuffer();
        if (conf instanceof PluginConfiguration) {
            String comment = ((PluginConfiguration) conf).getDescription();
            if (comment != null) {
                comment = StringUtil.wrapLines(comment, WRAP_SIZE - indention - 2);
                confsect.append(prependLines(comment, COMMENT_PREFIX));
                confsect.append(yamlOptions.getLineBreak().getString());
            }
        }
        
        for (IBaseConfigurationNode node : conf.getConfigNodes()) {
            ConfigurationStyle style = ConfigurationStyle.DEFAULT;
            if (node instanceof IConfigurationNode) {
                style = ((IConfigurationNode) node).getStyle();
                if (style == ConfigurationStyle.HIDDEN)
                    continue;
            }
            if (confsect.length() > 0 && style != ConfigurationStyle.GROUPED_PREVIOUS) {
                confsect.append(yamlOptions.getLineBreak().getString());
            }
            
            String comment = node.getDescription();
            if (comment != null && style != ConfigurationStyle.GROUPED_PREVIOUS) {
                comment = StringUtil.wrapLines(comment, WRAP_SIZE - indention - 2);
                confsect.append(prependLines(comment, COMMENT_PREFIX));
                confsect.append(yamlOptions.getLineBreak().getString());
            }
            if (node instanceof IConfigurationSubGroup) {
                confsect.append(node.getName());
                confsect.append(":");
                confsect.append(yamlOptions.getLineBreak().getString());
                confsect.append(getConfigurationYamlPart((IConfigurationSubGroup) node, level + 1));
            } else if (node instanceof IConfigurationNode) {
                Map<String, Object> tmp = new HashMap<String, Object>();
                Object value = conf.getValue((IConfigurationNode) node);
                if (value instanceof IToGeneric)
                    value = ((IToGeneric) value).toGeneric();
                tmp.put(node.getName(), value);
                confsect.append(yaml.dump(tmp));
            }
        }
        if (level > 0)
            return prependLines(confsect.toString(), StringUtils.repeat(" ", yamlOptions.getIndent()));
        else
            return confsect.toString();
    }
    
    private static String prependLines(final String string, final String prep) {
        return LINE_START.matcher(string).replaceAll(prep);
    }
}
