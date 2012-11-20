package de.jaschastarke.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


final public class StringUtil {
    public static int versionCompare(String vers1, String vers2) {
        String[] v1 = vers1.split("\\.");
        String[] v2 = vers2.split("\\.");
        int i = 0;
        while (i < v1.length && i < v2.length && v1[i].equals(v2[i])) {
            i++;
        }
        if (i < v1.length && i < v2.length) {
            int diff = new Integer(v1[i]).compareTo(new Integer(v2[i]));
            return diff < 0 ? -1 : (diff == 0 ? 0 : 1);
        }
        return v1.length < v2.length ? -1 : (v1.length == v2.length ? 0 : 1);
    }
    
    public static void copyFile(InputStream is, File to) {
        try {
            if (to.getParentFile() != null && !to.getParentFile().exists())
                to.getParentFile().mkdirs();
            OutputStream os;
                os = new FileOutputStream(to);
            byte[] buffer = new byte[512];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void copyFile(File from, File to) {
        try {
            copyFile(new FileInputStream(from), to);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static String join(String[] list, String sep, int from, int range) {
        StringBuilder result = new StringBuilder();
        for (int i = from; i >= 0 && i < from + range && i < list.length; i++) {
            if (result.length() > 0)
                result.append(sep);
            result.append(list[i]);
        }
        return result.toString();
    }
    public static String join(String[] list, int from, int range) {
        return join(list, " ", from, range);
    }
    public static String join(String[] list, int from) {
        return join(list, " ", from, list.length - from);
    }
    public static String join(String[] list) {
        return join(list, " ", 0, list.length);
    }
    public static String join(String[] list, String sep) {
        return join(list, sep, 0, list.length);
    }
}
