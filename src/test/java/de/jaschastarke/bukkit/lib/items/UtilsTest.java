package de.jaschastarke.bukkit.lib.items;

import static org.junit.Assert.*;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.junit.Before;
import org.junit.Test;

public class UtilsTest {
    @Before
    public void setUp() throws Exception {
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testParseMaterial() throws MaterialNotRecognizedException, MaterialDataNotRecognizedException {
        MaterialData m;
        m = ItemUtils.parseMaterial("CHAINMAIL_HELMET");
        assertEquals(m.getItemType(), Material.CHAINMAIL_HELMET);
        assertEquals(m.getData(), 0);
        
        m = ItemUtils.parseMaterial("EXP_BOTTLE");
        assertEquals(m.getItemType(), Material.EXP_BOTTLE);
        assertEquals(m.getData(), 0);
        
        m = ItemUtils.parseMaterial("bedrock");
        assertEquals(m.getItemType(), Material.BEDROCK);
        assertEquals(m.getData(), 0);
        
        m = ItemUtils.parseMaterial("LAVA_BUCKET");
        assertEquals(m.getItemType(), Material.LAVA_BUCKET);
        assertEquals(m.getData(), 0);
        
        m = ItemUtils.parseMaterial("35:11");
        assertEquals(m.getItemType(), Material.getMaterial(35));
        assertEquals(m.getData(), 11);
        
        m = ItemUtils.parseMaterial("WOOL:11");
        assertEquals(m.getItemType(), Material.WOOL);
        assertEquals(m.getData(), 11);
        
        m = ItemUtils.parseMaterial("MONSTER_EGG:56");
        assertEquals(m.getItemType(), Material.MONSTER_EGG);
        assertEquals(m.getData(), 56);
    }

    @Test(expected = MaterialNotRecognizedException.class)
    public void testParseMaterialMaterialNotFound() throws MaterialNotRecognizedException, MaterialDataNotRecognizedException {
        ItemUtils.parseMaterial("NOTCH");
    }
    @Test(expected = MaterialNotRecognizedException.class)
    public void testParseMaterialMaterialNotFoundData() throws MaterialNotRecognizedException, MaterialDataNotRecognizedException {
        ItemUtils.parseMaterial("MOJANG:11");
    }

    @Test(expected = MaterialDataNotRecognizedException.class)
    public void testParseMaterialMaterialDataNotFound() throws MaterialNotRecognizedException, MaterialDataNotRecognizedException {
        ItemUtils.parseMaterial("WOOL:BLUE");
    }

}
