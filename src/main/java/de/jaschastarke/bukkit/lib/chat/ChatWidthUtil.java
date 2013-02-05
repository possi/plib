package de.jaschastarke.bukkit.lib.chat;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.WordUtils;

public final class ChatWidthUtil {
    // Char Width from font/default.png
    public static final short[] CHAR_WIDTHS = new short[] {
        9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 4, 1, 3, 4, 4,
        4, 4, 1, 2, 2, 4, 4, 2, 4, 2, 4, 4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 2, 2, 3, 4, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3,
        4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 4, 2, 4, 4, 2, 4, 4, 4, 4, 4, 3, 4, 4, 3, 3, 4, 3, 4, 4,
        4, 4, 4, 4, 4, 3, 4, 4, 4, 4, 4, 4, 2, 1, 2, 4, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
        9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 4, 4, 4, 4, 1, -1, 3, 5, 3, 4, 4, 9, 5, 4, 2, 4, 3, 3, 2, 3, 4, 2, 2,
        2, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
        4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 4
    };
    public static final int CHAT_WIDTH = 230; // thought it would be 320;
    private static final String NEWLINE = "\n";

    private ChatWidthUtil() {
    }
    
    public static short getCharWidth(final char c) {
        if (c > CHAR_WIDTHS.length)
            return CHAR_WIDTHS[0];
        return CHAR_WIDTHS[c];
    }
    public static short getCharWidth(final String str, final int index) {
        short c = getCharWidth(str.charAt(index));
        if (c < 0)
            return 0;
        else if (index > 0 && getCharWidth(str.charAt(index - 1)) < 0)
            return 0;
        else
            return c;
    }
    
    
    public static int getStringLength(final String msg) {
        int l = 0;
        for (int i = 0; i < msg.length(); i++) {
            l += getCharWidth(msg.charAt(i));
        }
        return l;
    }
    
    public static String[] wrapLine(final String singleline) {
        String r = singleline.trim();
        int i = 0;
        int cl = 0;
        List<String> lines = new LinkedList<String>();
        while (r.length() > 0 && i < r.length()) {
            cl += getCharWidth(r, i++);
            if (cl > CHAT_WIDTH) {
                String[] wrap = WordUtils.wrap(r, i, NEWLINE, true).split(NEWLINE);
                lines.add(wrap[0].trim());
                r = r.substring(wrap[0].length()).trim();
                i = 0;
                cl = 0;
            }
        }
        if (r.length() > 0)
            lines.add(r);
        return lines.toArray(new String[lines.size()]);
    }
    
    public static int countLines(final String msg) {
        int lines = 1;
        int cl = 0;
        for (int i = 0; i < msg.length(); i++) {
            short c = getCharWidth(msg, i);
            if (cl + c > CHAT_WIDTH) {
                lines++;
                cl = c;
            } else {
                cl += c;
            }
        }
        return lines;
    }
}
