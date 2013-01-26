package de.jaschastarke.bukkit.lib;

public class ModuleLogger extends PluginLogger {
    protected SimpleModule<?> mod;
    
    public ModuleLogger(final Core plugin, final SimpleModule<?> simpleModule) {
        super(plugin);
        this.mod = simpleModule;
    }
    
    @Override
    protected String extendMessage(final String msg) {
        return "<" + mod.getName() + "> " + msg;
    }
}
