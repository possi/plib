package de.jaschastarke.bukkit.lib.commands.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;

import de.jaschastarke.utils.StringUtil;

public class DefinedParameterParser {
    private List<String> args;
    private String[] flagList;
    private int valueOffset;

    public DefinedParameterParser(final String args, final String[] flagList) {
        this(args, flagList, -1);
    }
    public DefinedParameterParser(final String args, final String[] flagList, final int joinedValueStart) {
        this(args.split("\\s"), flagList, joinedValueStart);
    }
    public DefinedParameterParser(final String[] args, final String[] flagList) {
        this(args, flagList, -1);
    }
    public DefinedParameterParser(final String[] args, final String[] flagList, final int joinedValueStart) {
        this.args = Arrays.asList(args);
        this.flagList = flagList;
        this.valueOffset = joinedValueStart;
        parse();
    }
    
    private static final Pattern PARAM = Pattern.compile("^\\-([\\w\\d])$|^\\-\\-([\\w\\d]+)(?:=(.+))?$");
    private static final int PARAM_OFFSET_P = 1;
    private static final int PARAM_OFFSET_M = 2;
    private static final int PARAM_OFFSET_MARG = 3;
    private List<String> flags = new ArrayList<String>();
    private Map<String, String> parameter = new HashMap<String, String>();
    private List<String> arguments = new LinkedList<String>();
    private void parse() {
        for (int i = 0; i < args.size(); i++) {
            if (valueOffset < 0 || arguments.size() < valueOffset) {
                Matcher m = PARAM.matcher(args.get(i));
                if (m.matches()) {
                    if (m.group(1) != null) {
                        if (ArrayUtils.contains(flagList, m.group(PARAM_OFFSET_P))) {
                            flags.add(m.group(PARAM_OFFSET_P));
                        } else {
                            parameter.put(m.group(PARAM_OFFSET_P), args.get(++i));
                        }
                    } else { // well than have group(2) to be != null
                        if (m.group(PARAM_OFFSET_MARG) != null) {
                            parameter.put(m.group(PARAM_OFFSET_M), m.group(PARAM_OFFSET_MARG));
                        } else if (ArrayUtils.contains(flagList, m.group(PARAM_OFFSET_M))) {
                            flags.add(m.group(PARAM_OFFSET_M));
                        } else {
                            parameter.put(m.group(PARAM_OFFSET_M), args.get(++i));
                        }
                    }
                    continue;
                }
                if (args.get(i).isEmpty())
                    continue;
            }
            arguments.add(args.get(i));
        }
    }
    
    public String getParameter(final String name) {
        return parameter.get(name.replaceFirst("^\\-{0,2}", ""));
    }
    public String getArgument(final int index) {
        if (valueOffset > -1 && index > valueOffset)
            return null;
        else if (valueOffset > -1 && index == valueOffset)
            return getValue();
        return (arguments.size() > index) ? arguments.get(index) : null;
    }
    public String getValue() {
        return StringUtil.join(arguments.toArray(new String[arguments.size()]), Math.max(valueOffset, 0));
    }
    public int getArgumentCount() {
        return valueOffset < 0 ? arguments.size() : Math.min(arguments.size(), valueOffset + 1); // Tread joined Value as one
    }
    public int getParameterCount() {
        return parameter.size();
    }
    public int getFlagCount() {
        return flags.size();
    }
    public List<String> getArguments() {
        return arguments;
    }
    public Map<String, String> getParameters() {
        return parameter;
    }
    public List<String> getFlags() {
        return flags;
    }
}
