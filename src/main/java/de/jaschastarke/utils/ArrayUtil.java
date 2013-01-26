package de.jaschastarke.utils;

import java.util.Arrays;

public final class ArrayUtil {
    private ArrayUtil() {
    }
    public static <T> T[] getRange(final T[] arr, final int begin) {
        return getRange(arr, begin, 0);
    }
    /**
     * @param end if < 0 then is interpreted as count from end
     */
    public static <T> T[] getRange(final T[] arr, final int begin, final int end) {
        int b = begin, e = end;
        if (b < 0)
            b = arr.length + b;
        if (end < 1)
            e = arr.length + e;
        
        return Arrays.copyOfRange(arr, b, e);
/*
        int length = end - begin;
        @SuppressWarnings("unchecked")
        T[] newArgs = (T[]) new Object[length];
        System.arraycopy(arr, begin, newArgs, 0, length);
        return newArgs;*/
    }
    
    public static <T> T[] push(final T[] arr, final T appendum) {
        T[] n = Arrays.copyOfRange(arr, 0, arr.length + 1);
        n[arr.length] = appendum;
        return n;
    }
}
