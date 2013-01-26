package de.jaschastarke.bukkit.lib;

import org.bukkit.Location;

public final class Utils {
    private Utils() {
    }
    public static String toString(final Location loc) {
        return "{X: " + loc.getBlockX() + ", Y: " + loc.getBlockY() + ", Z: " + loc.getBlockZ() + "}";
    }

}
