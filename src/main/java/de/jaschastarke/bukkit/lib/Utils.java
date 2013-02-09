package de.jaschastarke.bukkit.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class Utils {
    private Utils() {
    }
    public static String toString(final Location loc) {
        return "{X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ() + "}";
    }
    
    private static final long DEFAULT_MESSAGETIMEOUT = 5000; // 5 sec.
    private static HashMap<String, Long> messageCache = new HashMap<String, Long>();
    public static void sendTimeoutMessage(final Player player, final String msg, final long timeoutms) {
        for (Iterator<Entry<String, Long>> iterator = messageCache.entrySet().iterator(); iterator.hasNext();) {
            if (iterator.next().getValue() < System.currentTimeMillis())
                iterator.remove();
        }
        String hash = player.getName() + "#" + msg;
        if (!messageCache.containsKey(hash)) {
            player.sendMessage(msg);
            messageCache.put(hash, System.currentTimeMillis() + timeoutms);
        }
    }
    public static void sendTimeoutMessage(final Player player, final String msg) {
        sendTimeoutMessage(player, msg, DEFAULT_MESSAGETIMEOUT);
    }

}
