package de.jaschastarke.bukkit.lib.commands.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.jaschastarke.bukkit.lib.commands.CommandContext;
import de.jaschastarke.bukkit.lib.commands.ITabComplete;

// TODO: Cover argument params: --foo=bar and -f bar
public class TabCompletion implements ITabComplete {
    private static final Pattern USAGE_PARSE = Pattern.compile("(\\B\\-\\w\\b|\\B\\-?\\-\\w+\\b)|\\B\\[(.+?)\\]\\B|\\B<(.+?)>\\B|\\B\\|(.+?)\\|\\B|([^\\s]+)");
    private static final int USAGE_IDX_PARAM = 1;
    private static final int USAGE_IDX_OPTIONAL = 2;
    private static final int USAGE_IDX_REQUIRED = 3;
    private static final int USAGE_IDX_STATIC = 4;
    private static final int USAGE_IDX_OTHER = 5;
    
    private Map<String, String> params = new HashMap<String, String>();
    private Map<String, Completer> completer = new HashMap<String, Completer>();
    private Map<Integer, String> arguments = new HashMap<Integer, String>();
    
    public TabCompletion() {
        this(true);
    }
    public TabCompletion(final boolean initDefaults) {
        if (initDefaults) {
            completer.put("player", new PlayerCompleter());
            completer.put("world", new WorldCompleter());
            completer.put("gamemode", new GameModeCompleter());
        }
    }
    
    @Override
    public List<String> tabComplete(final CommandContext cc, final String[] args) {
        List<String> hints = new ArrayList<String>();
        int idx = 0;
        Context context = new Context(cc);
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                if (i == args.length - 1) {
                    for (String p : params.keySet()) {
                        if (p.toLowerCase().startsWith(arg.toLowerCase())) {
                            hints.add(p);
                        }
                    }
                    break;
                } else {
                    continue;
                }
            } else if (arg.isEmpty() && i == 0 && args.length == 1) {
                for (String p : params.keySet()) {
                    hints.add(p);
                }
            }
            String argName = arguments.get(idx);
            if (argName != null) {
                Completer cmpltr = completer.get(argName);
                if (cmpltr != null) {
                    if (i < args.length - 1) {
                        if (!matches(context, arg, cmpltr))
                            break; // previous completeable arguments doesn't match
                        else
                            context.setArgument(idx, arg);
                    } else {
                        List<String> tmpHints = cmpltr.get(context, arg);
                        if (tmpHints != null)
                            hints.addAll(tmpHints);
                    }
                }
            }
            idx++;
        }
        return hints;
    }
    
    public class Context {
        private Map<Integer, String> argVals = new HashMap<Integer, String>();
        private Map<String, String> paramVals = new HashMap<String, String>();
        
        private CommandContext cc;
        
        public Context(final CommandContext cc) {
            this.cc = cc;
        }
        public CommandContext getCommandContext() {
            return cc;
        }
        public void setArgument(final int index, final String val) {
            argVals.put(index, val);
        }
        public String getArgument(final int index) {
            return argVals.get(index);
        }
        public void setParameter(final String param, final String val) {
            paramVals.put(param, val);
        }
        public String getParameter(final String param) {
            return paramVals.get(param);
        }
        public TabCompletion getHelper() {
            return TabCompletion.this;
        }
    }
    
    public void setCompleter(final String name, final Completer cmpltr) {
        completer.put(name, cmpltr);
    }
    public Completer getCompleter(final String name) {
        return completer.get(name);
    }
    public void setArgumentIndex(final int idx, final String name) {
        arguments.put(idx, name);
    }
    public void setArgumentCompleter(final int idx, final String name, final Completer cmpltr) {
        setArgumentIndex(idx, name);
        setCompleter(name, cmpltr);
    }
    public void addParameter(final String name, final String argumentName) {
        params.put(name, argumentName);
    }
    public void addParameter(final String name) {
        addParameter(name, null);
    }
    
    public Map<String, String> getParams() {
        return new HashMap<String, String>(params);
    }
    public String[] getArguments() {
        int size = 0;
        for (Integer idx : arguments.keySet()) {
            size = Math.max(size, idx + 1);
        }
        String[] args = new String[size];
        for (int i = 0; i < size; i++) {
            args[i] = arguments.containsKey(i) ? arguments.get(i) : null;
        }
        return args;
    }
    
    public static TabCompletion forUsageLine(final String usageLine) {
        TabCompletion completer = new TabCompletion();
        Matcher matcher = USAGE_PARSE.matcher(usageLine);
        int idx = 0;
        while (matcher.find()) {
            if (matcher.group(USAGE_IDX_PARAM) != null) {
                completer.addParameter(matcher.group(0));
            } else if (matcher.group(USAGE_IDX_OPTIONAL) != null) {
                completer.setArgumentIndex(idx, matcher.group(USAGE_IDX_OPTIONAL));
                idx++;
            } else if (matcher.group(USAGE_IDX_REQUIRED) != null) {
                completer.setArgumentIndex(idx, matcher.group(USAGE_IDX_REQUIRED));
                idx++;
            } else if (matcher.group(USAGE_IDX_OTHER) != null) {
                completer.setArgumentIndex(idx, matcher.group(USAGE_IDX_OTHER));
                idx++;
            } else if (matcher.group(USAGE_IDX_STATIC) != null) {
                completer.setArgumentCompleter(idx, matcher.group(USAGE_IDX_STATIC), new StaticCompleter(matcher.group(USAGE_IDX_STATIC)));
                idx++;
            }
        }
        
        return completer;
    }
    
    
    public static interface Completer {
        public List<String> get(Context context, String arg);
    }
    
    protected static boolean matches(final Context context, final String val, final Completer cmpltr) {
        List<String> result = cmpltr.get(context, val);
        return result != null && result.get(0).equals(val);
    }
    
    public static class PlayerCompleter implements Completer {
        @Override
        public List<String> get(final Context context, final String arg) {
            List<String> hints = new ArrayList<String>();
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(arg.toLowerCase()))
                    hints.add(p.getName());
            }
            return hints;
        }
    }

    public static class GameModeCompleter extends EnumCompleter<GameMode> {
        public GameModeCompleter() {
            super(GameMode.class);
        }
    }
    public static class EnumCompleter<T extends Enum<T>> implements Completer {
        private Class<T> enumType;
        public EnumCompleter(final Class<T> enumType) {
            this.enumType = enumType;
        }
        @Override
        public List<String> get(final Context context, final String arg) {
            List<String> hints = new ArrayList<String>();
            for (T e : enumType.getEnumConstants()) {
                if (e.name().toLowerCase().startsWith(arg.toLowerCase()))
                    hints.add(e.name());
            }
            return hints;
        }
    }
    
    public static class WorldCompleter implements Completer {
        @Override
        public List<String> get(final Context context, final String arg) {
            List<String> hints = new ArrayList<String>();
            for (World w : Bukkit.getServer().getWorlds()) {
                if (w.getName().toLowerCase().startsWith(arg.toLowerCase()))
                    hints.add(w.getName());
            }
            return hints;
        }
    }
    
    public static class StringArrayCompleter implements Completer {
        private String[] strings;
        public StringArrayCompleter(final String[] strings) {
            this.strings = strings;
        }
        @Override
        public List<String> get(final Context context, final String arg) {
            List<String> hints = new ArrayList<String>();
            for (String s : strings) {
                if (s.toLowerCase().startsWith(arg.toLowerCase()))
                    hints.add(s);
            }
            return hints;
        }
    }
    
    public static class StaticCompleter extends StringArrayCompleter {
        public StaticCompleter(final String string) {
            super(new String[]{string});
        }
    }
}
