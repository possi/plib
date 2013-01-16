package de.jaschastarke.bukkit.lib;

public class ModuleLogger extends PluginLogger {
    protected CoreModule mod;
    
    public ModuleLogger(Core plugin, CoreModule mod) {
        super(plugin);
        this.mod = mod;
    }
    
    @Override
    protected String extendMessage(String msg) {
        return "<"+this.mod.getName()+"> "+msg;
    }
}
