package de.jaschastarke.minecraft.lib;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public interface PluginCore {
    public Logger getLog();
    public String getName();
    
    public File getDataFolder();
    public InputStream getResource(String resourcePath);
}
