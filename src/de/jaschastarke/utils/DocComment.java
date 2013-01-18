package de.jaschastarke.utils;

import java.io.Serializable;
import java.util.regex.Pattern;

public class DocComment implements Serializable {
    private static final long serialVersionUID = -6500262229790513118L;
    private static final Pattern ANNOT_REGEX = Pattern.compile("^\\s*@(\\w+)\\s+(.*)", Pattern.CASE_INSENSITIVE);
    
    protected String doc;
    
    public DocComment(String comment) {
        doc = comment;
    }
    @Override
    public String toString() {
        return doc;
    }
    public String getDescription() {
        String comment = "";
        String[] lines = doc.split("\r?\n");
        boolean newline = true;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().isEmpty()) {
                comment += newline ? "\n" : "\n\n";
                newline = true;
            } else {
                if (ANNOT_REGEX.matcher(lines[i]).matches()) {
                    break;
                } else {
                    if (!newline)
                        comment += " ";
                    comment += lines[i].trim();
                    newline = false;
                }
            }
        }
        return comment;
    }
    public String getShortDesc() {
        String comment = "";
        String[] lines = doc.split("\r?\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().isEmpty() || ANNOT_REGEX.matcher(lines[i]).matches()) {
                break;
            } else {
                if (i > 0)
                    comment += " ";
                comment += lines[i].trim();
            }
        }
        return comment;
    }
    public String getLongDesc() {
        boolean longDescStarted = false;
        String comment = "";
        String[] lines = doc.split("\r?\n");
        boolean newline = true;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().length() == 0) {
                if (!longDescStarted)
                    longDescStarted = true;
                else
                    comment += "\n\n";
                newline = true;
            } else {
                if (ANNOT_REGEX.matcher(lines[i]).matches()) {
                    break;
                } else {
                    if (!newline)
                        comment += " ";
                    comment += lines[i].trim();
                    newline = false;
                }
            }
        }
        return comment;
    }
}