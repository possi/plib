package de.jaschastarke.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;


public final class StringUtil {
    private StringUtil() {
    }
    
    private static final Pattern VERSION_SPLIT = Pattern.compile("\\.");
    public static int versionCompare(final String vers1, final String vers2) {
        String[] v1 = VERSION_SPLIT.split(vers1);
        String[] v2 = VERSION_SPLIT.split(vers2);
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
    
    private static final int BUFFER_SIZE = 512;
    public static void copyFile(final InputStream is, final File to) {
        try {
            if (to.getParentFile() != null && !to.getParentFile().exists())
                to.getParentFile().mkdirs();
            OutputStream os;
                os = new FileOutputStream(to);
            byte[] buffer = new byte[BUFFER_SIZE];
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
    public static void copyFile(final File from, final File to) {
        try {
            copyFile(new FileInputStream(from), to);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static final String DEFAULT_SEP = " ";
    public static String join(final String[] list, final String sep, final int from, final int range) {
        StringBuilder result = new StringBuilder();
        for (int i = from; i >= 0 && i < from + range && i < list.length; i++) {
            if (result.length() > 0)
                result.append(sep);
            result.append(list[i]);
        }
        return result.toString();
    }
    public static String join(final String[] list, final int from, final int range) {
        return join(list, DEFAULT_SEP, from, range);
    }
    public static String join(final String[] list, final int from) {
        return join(list, DEFAULT_SEP, from, list.length - from);
    }
    public static String join(final String[] list) {
        return join(list, DEFAULT_SEP, 0, list.length);
    }
    public static String join(final String[] list, final String sep) {
        return join(list, sep, 0, list.length);
    }
    
    /*public static String uncapitalize(String string) {
        return Character.toLowerCase(string.charAt(0)) + (string.length() > 1 ? string.substring(1) : "");
    }*/
    
    private static final String NEWLINE = "\n";
    public static String wrapLines(final String multilineText, final int wrapSize) {
        String[] lines = multilineText.split("\r?\n");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = WordUtils.wrap(lines[i], wrapSize, NEWLINE, false);
        }
        return join(lines, NEWLINE);
    }
}
