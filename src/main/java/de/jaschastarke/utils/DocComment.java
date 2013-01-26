package de.jaschastarke.utils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocComment implements Serializable {
    private static final long serialVersionUID = -6500262229790513118L;
    private static final Pattern ANNOT_REGEX = Pattern.compile("^\\s*@(\\w+)(?:\\s+(.*))?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern NEWLINE_REGEX = Pattern.compile("\r?\n");
    private static final String SPACE = " ";
    private static final String NEWLINE = "\n";
    private static final String NEWPARAGRAPH = "\n\n";
    
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
        boolean newline = true;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().isEmpty()) {
                comment += newline ? NEWLINE : NEWPARAGRAPH;
                newline = true;
            } else {
                if (ANNOT_REGEX.matcher(lines[i]).matches()) {
                    break;
                } else {
                    if (!newline)
                        comment += SPACE;
                    comment += lines[i].trim();
                    newline = false;
                }
            }
        }
        return comment;
    }
    public String getShortDesc() {
        String comment = "";
        String[] lines = NEWLINE_REGEX.split(doc);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().isEmpty() || ANNOT_REGEX.matcher(lines[i]).matches()) {
                break;
            } else {
                if (i > 0)
                    comment += SPACE;
                comment += lines[i].trim();
            }
        }
        return comment;
    }
    public String getLongDesc() {
        boolean longDescStarted = false;
        String comment = "";
        String[] lines = NEWLINE_REGEX.split(doc);
        boolean newline = true;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().length() == 0) {
                if (!longDescStarted)
                    longDescStarted = true;
                else
                    comment += NEWPARAGRAPH;
                newline = true;
            } else {
                if (ANNOT_REGEX.matcher(lines[i]).matches()) {
                    break;
                } else if (longDescStarted) {
                    if (!newline)
                        comment += SPACE;
                    comment += lines[i].trim();
                    newline = false;
                }
            }
        }
        return comment;
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