package de.jaschastarke;

import static org.junit.Assert.*;

import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

public class MultipleResourceBundleTest {
    ResourceBundle multi;
    
    @Before
    public void setUp() throws Exception {
        multi = new MultipleResourceBundle(null, new String[] {
            "de.jaschastarke.bukkit.messages",
            "de.jaschastarke.bukkit.test",
        });
    }

    @Test
    public void test() {
        assertEquals("Syntax: ", multi.getString("bukkit.help.syntax"));
        assertEquals("Test-Example", multi.getString("test.example"));
    }

}
