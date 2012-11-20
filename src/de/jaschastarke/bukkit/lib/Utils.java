package de.jaschastarke.bukkit.lib;

import org.bukkit.Location;

public class Utils {

    public static String toString(Location loc) {
        return "{X: "+loc.getBlockX()+", Y: "+loc.getBlockY()+", Z: "+loc.getBlockZ()+"}";
    }

}
