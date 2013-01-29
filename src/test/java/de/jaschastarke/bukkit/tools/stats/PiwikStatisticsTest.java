package de.jaschastarke.bukkit.tools.stats;

import static org.junit.Assert.*;

import org.junit.Test;

public class PiwikStatisticsTest {
    @Test
    public void testGetUniqueID() {
        assertTrue(PiwikStatistics.getUniqueID().matches("^[0-9a-f]{16}$"));
    }
}
