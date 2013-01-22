package de.jaschastarke.utils;

import java.util.Arrays;

final public class ArrayUtil {
    public static <T> T[] getRange(T[] arr, int begin) {
        return getRange(arr, begin, 0);
    }
    /**
     * @param end if < 0 then is interpreted as count from end
     */
    public static <T> T[] getRange(T[] arr, int begin, int end) {
        if (begin < 0)
            begin = arr.length + begin;
        if (end < 1)
            end = arr.length + end;
        
        return Arrays.copyOfRange(arr, begin, end);
/*
        int length = end - begin;
        @SuppressWarnings("unchecked")
        T[] newArgs = (T[]) new Object[length];
        System.arraycopy(arr, begin, newArgs, 0, length);
        return newArgs;*/
    }
}
