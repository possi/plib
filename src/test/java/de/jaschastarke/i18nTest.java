package de.jaschastarke;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class i18nTest {
    i18n lang;

    @Before
    public void setUp() throws Exception {
        lang = new i18n("de.jaschastarke.bukkit.test");
    }

    @Test
    public void testTrans() {
        assertEquals("Syntax: ", lang.trans("bukkit.help.syntax"));
        assertEquals("Test-Exampe", lang.trans("test.example"));
    }

    @Test
    public void testGetLocaleFromString() {
        assertEquals(Locale.US, i18n.getLocaleFromString("en_US"));
        assertEquals(Locale.US, i18n.getLocaleFromString("en-US"));
        assertEquals(Locale.GERMAN, i18n.getLocaleFromString("de"));
        assertEquals(Locale.GERMANY, i18n.getLocaleFromString("de_DE"));
    }

}
