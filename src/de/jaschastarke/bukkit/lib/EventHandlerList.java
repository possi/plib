package de.jaschastarke.bukkit.lib;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class EventHandlerList {
    private List<Listener> listeners = new ArrayList<Listener>();
    private JavaPlugin plugin;
    public EventHandlerList(JavaPlugin plugin) {
        this.plugin = plugin; 
    }
    public void registerEvents(Listener eventListener) {
        addListener(eventListener);
        listenTo(eventListener);
    }
    public void addListener(Listener eventListener) {
        if (!listeners.contains(eventListener))
            listeners.add(eventListener);
    }
    protected void removeEvents(Listener eventListener) {
        HandlerList.unregisterAll(eventListener);
        listeners.remove(eventListener);
    }
    public void removeAllEvents() {
        Iterator<Listener> it = listeners.iterator();
        while (it.hasNext()) {
            HandlerList.unregisterAll(it.next());
            it.remove();
        }
    }
    public void unregisterAllEvents() {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
    }
    public void registerAllEvents() {
        for (Listener listener : listeners) {
            listenTo(listener);
        }
    }
    private void listenTo(Listener eventListener) {
        plugin.getServer().getPluginManager().registerEvents(eventListener, plugin);
    }
}
