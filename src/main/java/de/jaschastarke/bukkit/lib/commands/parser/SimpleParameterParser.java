package de.jaschastarke.bukkit.lib.commands.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.jaschastarke.utils.StringUtil;

public class SimpleParameterParser {
    private List<String> args;
    public SimpleParameterParser(final String[] args) {
        this.args = Arrays.asList(args);
        //this.args = new LinkedList<String>(Arrays.asList(args));
        parse();
    }
    
    private static final Pattern PARAM = Pattern.compile("^\\-(\\w)$");
    private Map<String, String> parameter = new HashMap<String, String>();
    private List<String> arguments = new LinkedList<String>();
    private void parse() {
        for (int i = 0; i < args.size(); i++) {
            Matcher m = PARAM.matcher(args.get(i));
            if (m.matches()) {
                if (i < args.size() - 1) {
                    parameter.put(m.group(1), args.get(++i));
                } else {
                    parameter.put(m.group(1), "");
                }
            } else {
                arguments.add(args.get(i));
            }
        }
    }
    
    public String getParameter(final String name) {
        return parameter.get(name.replaceFirst("^\\-", ""));
    }
    public String getArgument(final int index) {
        return (arguments.size() > index) ? arguments.get(index) : null;
    }
    public String getValue(final int from) {
        return StringUtil.join(arguments.toArray(new String[arguments.size()]), from);
    }
    public int getArgumentCount() {
        return arguments.size();
    }
    public int getParameterCount() {
        return parameter.size();
    }
}
