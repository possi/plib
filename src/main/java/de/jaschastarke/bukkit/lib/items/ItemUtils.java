package de.jaschastarke.bukkit.lib.items;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

public final class ItemUtils {
    private ItemUtils() {
    }
    public static final String MATERIAL_DATA_SEP = ":";
    @SuppressWarnings("deprecation")
    public static MaterialData parseMaterial(final String m) throws MaterialNotRecognizedException, MaterialDataNotRecognizedException {
        String material = m;
        int d = -1;
        if (material.contains(MATERIAL_DATA_SEP)) {
            String[] t = material.split(MATERIAL_DATA_SEP);
            material = t[0];
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
            e = Material.getMaterial(Integer.parseInt(material));
        } catch (NumberFormatException ex) {
            e = Material.matchMaterial(material);
        }
        if (e == null)
            throw new MaterialNotRecognizedException();
        if (d != -1)
            return new MaterialData(e, (byte) d);
        else
            return new MaterialData(e);
    }
}
