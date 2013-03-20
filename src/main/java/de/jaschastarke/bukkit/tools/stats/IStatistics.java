package de.jaschastarke.bukkit.tools.stats;

import java.io.IOException;

public interface IStatistics {
    public static final String SEPERATOR = "/";
    /**
     * Use the {@see SEPERATOR} to create subgroup of events
     * @param event
     * @throws IOException 
     */
    public void trackEvent(String event) throws IOException;
    
    public void unregister();
}
