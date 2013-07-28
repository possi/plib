package de.jaschastarke.utils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DocComment parser
 * 
 * When a line in the doc comments ends with a whitespace, the resulted string has no Linebreak after that line.
 */
public class DocComment implements Serializable {
    private static final long serialVersionUID = -6500262229790513118L;
    private static final Pattern ANNOT_REGEX = Pattern.compile("^\\s*@(\\w+)(?:\\s+(.*))?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern NEWLINE_REGEX = Pattern.compile("\r?\n");
    private static final Pattern STRING_END_REGEX = Pattern.compile(".*[^ ] $");
    private static final Pattern LEFT_SPACE_TRIM = Pattern.compile("^ ");
    private static final String SPACE = " ";
    private static final String NEWLINE = "\n";
    
    protected String doc;
    
    public DocComment(final String comment) {
        doc = comment;
    }
    @Override
    public String toString() {
        return doc;
    }
    public String getDescription() {
        String comment = "";
        String[] lines = NEWLINE_REGEX.split(doc);
        for (int i = 0; i < lines.length; i++) {
            if (ANNOT_REGEX.matcher(lines[i]).matches()) {
                break;
            }
            comment += LEFT_SPACE_TRIM.matcher(lines[i]).replaceFirst("");
            if (!STRING_END_REGEX.matcher(lines[i]).matches())
                comment += NEWLINE;
        }
        return comment.trim();
    }
    public String getShortDesc() {
        String comment = "";
        String[] lines = NEWLINE_REGEX.split(doc);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().isEmpty() || ANNOT_REGEX.matcher(lines[i]).matches()) {
                break;
            } else {
                comment += LEFT_SPACE_TRIM.matcher(lines[i]).replaceFirst("");
                if (!STRING_END_REGEX.matcher(lines[i]).matches())
                    comment += NEWLINE;
            }
        }
        return comment.trim();
    }
    public String getLongDesc() {
        boolean longDescStarted = false;
        String comment = "";
        String[] lines = NEWLINE_REGEX.split(doc);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().length() == 0) {
                if (!longDescStarted)
                    longDescStarted = true;
                else
                    comment += NEWLINE;
            } else {
                if (ANNOT_REGEX.matcher(lines[i]).matches()) {
                    break;
                } else if (longDescStarted) {
                    comment += LEFT_SPACE_TRIM.matcher(lines[i]).replaceFirst("");
                    if (!STRING_END_REGEX.matcher(lines[i]).matches())
                        comment += NEWLINE;
                }
            }
        }
        return comment.trim();
    }
    public String getAnnotationValue(final String string) {
        String comment = null;
        String[] lines = NEWLINE_REGEX.split(doc);
        for (int i = 0; i < lines.length; i++) {
            Matcher line = ANNOT_REGEX.matcher(lines[i]);
            if (comment != null) {
                if (line.matches() || lines[i].trim().isEmpty())
                    break;
                comment += SPACE + lines[i].trim();
            } else {
                if (line.matches() && string.equals(line.group(1))) {
                    comment = line.group(2) == null ? "" : line.group(2).trim();
                }
            }
        }
        return comment;
    }
}