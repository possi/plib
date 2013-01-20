package de.jaschastarke.bukkit.lib.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import de.jaschastarke.configuration.ElementConfigurationNode;
import de.jaschastarke.configuration.IConfigurationGroup;
import de.jaschastarke.configuration.IConfigurationNode;
import de.jaschastarke.configuration.IConfigurationSubGroup;
import de.jaschastarke.utils.ClassDescriptorStorage;
import de.jaschastarke.utils.ClassDescriptorStorage.ClassDescription;
import de.jaschastarke.utils.DocComment;
import de.jaschastarke.utils.StringUtil;

public class ConfigurationDumper {
    protected static ClassDescriptorStorage cds = ClassDescriptorStorage.getInstance();
    protected DumperOptions yamlOptions = new DumperOptions();
    protected Representer yamlRepresenter = new YamlRepresenter();
    protected Yaml yaml = new Yaml(new YamlConstructor(), yamlRepresenter, yamlOptions);
    protected Configuration config;
    private final static Pattern LINE_START = Pattern.compile("^", Pattern.MULTILINE);
    private final static int INDENT_SIZE = 4;
    private final static int WRAP_SIZE = 80;
    
    public ConfigurationDumper(Configuration conf) {
        //ClassDescriptorStorage.load(new File("META-INF/descriptions.jos"));
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        config = conf;
    }
    
    public void store(File toFile) throws IOException {
        FileWriter writer = new FileWriter(toFile);
        writer.write(getConfigurationYamlPart(config));
        writer.close();
    }
    
    private String getConfigurationYamlPart(IConfigurationGroup conf) {
        return getConfigurationYamlPart(conf, 0);
    }
    
    /**
     * @TODO: do better by extending snakeYaml
     */
    private String getConfigurationYamlPart(IConfigurationGroup conf, int level) {
        int indention = INDENT_SIZE * level;
        ClassDescription cd = cds.getClassFor(conf);
        yamlOptions.setWidth(WRAP_SIZE - indention);
        
        StringBuffer confsect = new StringBuffer();
        if (level == 0) {
            DocComment class_comment = cd.getDocComment();
            if (class_comment != null) {
                String ccmt = class_comment.getDescription();
                ccmt = StringUtil.wrapLines(ccmt, WRAP_SIZE - indention - 2);
                confsect.append(prependLines(ccmt, "# "));
                confsect.append("\n");
            }
        }
        
        for (IConfigurationNode node : conf.getConfigNodes()) {
            if (confsect.length() > 0)
                confsect.append("\n");
            if (node instanceof ElementConfigurationNode) {
                // TODO: Abstract getDocComment to be part of the configuration node?
                ElementConfigurationNode enode = (ElementConfigurationNode) node;
                if (enode.getMethod() != null) {
                    DocComment comment = cd.getElDocComment(enode.getMethod().getName());
                    if (comment != null) {
                        String cmt = comment.getDescription();
                        cmt = StringUtil.wrapLines(cmt, WRAP_SIZE - indention - 2);
                        confsect.append(prependLines(cmt, "# "));
                        confsect.append("\n");
                    }
                }
                Map<String, Object> tmp = new HashMap<String, Object>();
                if (enode.getMethod() == null)
                    tmp.put(node.getName(), config.getValues().get(node.getName()));
                else {
                    try {
                        tmp.put(node.getName(), enode.getMethod().invoke(conf));
                    } catch (IllegalArgumentException e) {
                        tmp.put(node.getName(), config.getValues().get(node.getName()));
                    } catch (IllegalAccessException e) {
                        tmp.put(node.getName(), config.getValues().get(node.getName()));
                    } catch (InvocationTargetException e) {
                        tmp.put(node.getName(), config.getValues().get(node.getName()));
                    }
                }
                confsect.append(yaml.dump(tmp));
            } else if (node instanceof IConfigurationSubGroup) {
                ClassDescription scd = cds.getClassFor(node);
                DocComment comment = scd.getDocComment();
                if (comment != null) {
                    String cmt = comment.getDescription();
                    cmt = StringUtil.wrapLines(cmt, WRAP_SIZE - indention - 2);
                    confsect.append(prependLines(cmt, "# "));
                    confsect.append("\n");
                }
                
                confsect.append(node.getName());
                confsect.append(":\n");
                confsect.append(getConfigurationYamlPart((IConfigurationSubGroup) node, level + 1));
            }
        }
        if (level > 0)
            return prependLines(confsect.toString(), StringUtils.repeat(" ", INDENT_SIZE));
        else
            return confsect.toString();
    }
    
    private static String prependLines(String string, String prep) {
        return LINE_START.matcher(string).replaceAll(prep);
    }
}
