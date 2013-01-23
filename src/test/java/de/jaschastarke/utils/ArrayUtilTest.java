package de.jaschastarke.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayUtilTest {
    protected String[] source = new String[] {
        "First",
        "Second",
        "Third",
        "Fourth",
        "Fifth"
    };

    @Test
    public void testDefault() {
        assertArrayEquals(new String[]{"Third", "Fourth"}, ArrayUtil.getRange(source, 2, 4));
        assertArrayEquals(new String[]{"First", "Second", "Third", "Fourth"}, ArrayUtil.getRange(source, 0, 4));
    }
    
    @Test
    public void testOptionalEnd() {
        assertArrayEquals(new String[]{"Third", "Fourth", "Fifth"}, ArrayUtil.getRange(source, 2));
    }
    
    @Test
    public void testNegativeStart() {
        assertArrayEquals(new String[]{"Fourth"}, ArrayUtil.getRange(source, -2, 4));
        assertArrayEquals(new String[]{"Fourth", "Fifth"}, ArrayUtil.getRange(source, -2));
    }
    
    public void testOtherType() {
        Integer[] arr1 = new Integer[] {1, 2, 3, 4, 5};
        
        assertArrayEquals(new Integer[]{3, 4}, ArrayUtil.getRange(arr1, 2, 4));
        assertArrayEquals(new Integer[]{4, 5}, ArrayUtil.getRange(arr1, -2));
    }
    
    @Test
    public void testExpandArray() {
        assertArrayEquals(new String[]{"Third", "Fourth", "Fifth", null}, ArrayUtil.getRange(source, 2, 6));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOutOfBoundsEndBeforeBeginning() {
        assertArrayEquals(new String[]{}, ArrayUtil.getRange(source, 4, 2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOutOfBoundsEndBeforeNegativeBeginning() {
        assertArrayEquals(new String[]{}, ArrayUtil.getRange(source, -2,2));
    }
}
