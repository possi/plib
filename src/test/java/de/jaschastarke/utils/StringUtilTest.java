package de.jaschastarke.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StringUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testVersionCompare() {
        assertEquals(0, StringUtil.versionCompare("1.0", "1.000"));
        assertEquals(-1, StringUtil.versionCompare("1.0", "1.0.1"));
        assertEquals(-1, StringUtil.versionCompare("0.10", "0.10.1"));
        assertEquals(1, StringUtil.versionCompare("0.10", "0.9"));
        assertEquals(1, StringUtil.versionCompare("0.10", "0.2"));
        assertEquals(-1, StringUtil.versionCompare("0.10", "0.11"));
        assertEquals(-1, StringUtil.versionCompare("1.10", "2.1"));
    }
    
    /*
    @Test
    public void testCopyFileInputStreamFile() {
        fail("Not yet implemented");
    }

    @Test
    public void testCopyFileFileFile() {
        fail("Not yet implemented");
    }*/

    @Test
    public void testJoinStringArrayStringIntInt() {
        assertEquals("b.c", StringUtil.join(new String[]{"a", "b", "c", "d"}, ".", 1, 2));
    }

    @Test
    public void testJoinStringArrayIntInt() {
        assertEquals("b c", StringUtil.join(new String[]{"a", "b", "c", "d"}, 1, 2));
    }

    @Test
    public void testJoinStringArrayInt() {
        assertEquals("c d", StringUtil.join(new String[]{"a", "b", "c", "d"}, 2));
    }

    @Test
    public void testJoinStringArray() {
        assertEquals("a b c d", StringUtil.join(new String[]{"a", "b", "c", "d"}));
    }

    @Test
    public void testJoinStringArrayString() {
        assertEquals("a.b.c.d", StringUtil.join(new String[]{"a", "b", "c", "d"}, "."));
    }

    @Test
    public void testWrapLines() {
        assertEquals("ab cd ef\ngh\nij kl mn", StringUtil.wrapLines("ab cd ef gh\nij kl mn", 8));
    }

}
