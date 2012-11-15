package de.jaschastarke.bukkit.lib;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import de.jaschastarke.minecraft.lib.PluginCore;

public class Core extends JavaPlugin implements PluginCore {
    private final Logger logger = Logger.getLogger("Minecraft");
    public Logger getLog() {
        return logger;
    }
    
}
