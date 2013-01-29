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
        assertEquals("Test-Example", lang.trans("test.example"));
    }

    @Test
    public void testLocaleString() {
        LocaleString s1 = new LocaleString("bukkit.help.syntax");
        assertEquals("Syntax: ", lang.trans(s1));
        assertEquals("Syntax: ", s1.toString());
        
        LocaleString s2 = new LocaleString("test.example");
        assertEquals("Test-Example", s2.translate(lang));
        assertEquals("Test-Example", s2.toString());
    }
    
    @Test
    public void testPlaceholder() {
        assertEquals("Test >Foo< Example", lang.trans("test.example.placeholder", "Foo"));
        assertEquals("Test >Foo< Example", lang.trans("test.example.placeholder", "Foo", 123, "456"));
        
        LocaleString s3 = new LocaleString("test.example.placeholder", "Abc");
        assertEquals("Test >Abc< Example", lang.trans(s3));
    }

    @Test
    public void testGetLocaleFromString() {
        assertEquals(Locale.US, I18n.getLocaleFromString("en_US"));
        assertEquals(Locale.US, I18n.getLocaleFromString("en-US"));
        assertEquals(Locale.GERMAN, I18n.getLocaleFromString("de"));
        assertEquals(Locale.GERMANY, I18n.getLocaleFromString("de_DE"));
    }

}
