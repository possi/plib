package de.jaschastarke;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class i18nTest {
    I18n lang;

    @Before
    public void setUp() throws Exception {
        lang = new I18n("de.jaschastarke.bukkit.test");
    }

    @Test
    public void testTrans() {
        assertEquals("Syntax: ", lang.trans("bukkit.help.syntax"));
        assertEquals("Test-Exampe", lang.trans("test.example"));
    }

    @Test
    public void testGetLocaleFromString() {
        assertEquals(Locale.US, I18n.getLocaleFromString("en_US"));
        assertEquals(Locale.US, I18n.getLocaleFromString("en-US"));
        assertEquals(Locale.GERMAN, I18n.getLocaleFromString("de"));
        assertEquals(Locale.GERMANY, I18n.getLocaleFromString("de_DE"));
    }

}
