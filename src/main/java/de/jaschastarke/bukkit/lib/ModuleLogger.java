package de.jaschastarke.bukkit.lib;

public class ModuleLogger extends PluginLogger {
    protected SimpleModule<?> mod;
    
    public ModuleLogger(Core plugin, SimpleModule<?> simpleModule) {
        super(plugin);
        this.mod = simpleModule;
    }
    
    @Override
    protected String extendMessage(String msg) {
        return "<"+mod.getName()+"> "+msg;
    }
}
