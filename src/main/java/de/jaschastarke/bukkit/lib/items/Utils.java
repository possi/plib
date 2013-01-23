package de.jaschastarke.bukkit.lib.items;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public class Utils {
    public static MaterialData parseMaterial(String m) throws MaterialNotRecognizedException, MaterialDataNotRecognizedException {
        int d = -1;
        if (m.contains(":")) {
            String[] t = m.split(":");
            m = t[0];
            try {
                d = Integer.parseInt(t[1]);
            } catch (NumberFormatException ex) {
                // TODO: try to find the data value by name
                if (d == -1)
                    throw new MaterialDataNotRecognizedException();
            }
        }
        Material e = null;
        try {
            e = Material.getMaterial(Integer.parseInt(m));
        } catch (NumberFormatException ex) {
            e = Material.matchMaterial(m);
        }
        if (e == null)
            throw new MaterialNotRecognizedException();
        if (d != -1)
            return new MaterialData(e, (byte) d);
        else
            return new MaterialData(e);
    }
}
