package de.jaschastarke.bukkit.lib.chat;

import java.util.LinkedList;
import java.util.regex.Pattern;

import de.jaschastarke.utils.ArrayUtil;

public class InGamePagination implements IPagination {
    private LinkedList<StringBuilder> rows = new LinkedList<StringBuilder>();
    private IFormatter f;
    private int[] range = new int[]{0, 0};
    private int currentPage = 1;

    public InGamePagination(final IFormatter formatter) {
        f = formatter;
        appendln();
    }
    
    private void checkLineLength() {
        if (rows.size() > 0) {
            String[] wrap = ChatWidthUtil.wrapLine(rows.getLast().toString());
            if (wrap.length > 1) {
                for (int i = 0; i < wrap.length; i++) {
                    if (i == 0) {
                        rows.getLast().replace(0, rows.getLast().length(), wrap[i]);
                    } else {
                        rows.add(new StringBuilder(wrap[i]));
                    }
                }
            }
        }
    }
    
    @Override
    public String getPageDisplay() {
        return "{PAGE_X_/_Y}";
    }
    @Override
    public void appendln() {
        checkLineLength();
        rows.add(new StringBuilder());
    }
    @Override
    public void appendln(final CharSequence line) {
        append(line);
        appendln();
    }
    @Override
    public void setFixedLines(final int top, final int bottom) {
        range[0] = top; range[1] = bottom;
    }
    @Override
    public void selectPage(final int page) {
        currentPage = page;
    }
    @Override
    public String[] selectPage(final String[] args) {
        if (args.length >= 1) {
            try {
                int p = Integer.parseInt(args[0]);
                currentPage = p;
                return ArrayUtil.getRange(args, 1);
            } catch (NumberFormatException e) {
                return args;
            }
        }
        return args;
    }
    @Override
    public int getSelectedPage() {
        return currentPage > getPageCount() || currentPage < 1 ? 1 : currentPage;
    }
    @Override
    public int getPageCount() {
        int off = getPageBorderSize();
        double s = getPageSize();
        return (int) Math.ceil((rows.size() - off) / s);
    }
    
    private int getPageSize() {
        return f.getLineLimit() - getPageBorderSize();
    }
    private int getPageBorderSize() {
        int malus = range[0] + range[1];
        if (malus > f.getLineLimit())
            throw new IllegalArgumentException("The fixed lines size is greater then display size");
        return malus;
    }
    
    public String toString() {
        checkLineLength();
        
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < range[0]; i++) {
            if (ret.length() > 0)
                ret.append(f.getNewLine());
            ret.append(rows.get(i));
        }
        int pageBegin = range[0] + (getPageSize() * (getSelectedPage() - 1));
        int pageEnd = Math.min(range[0] + (getPageSize() * getSelectedPage()), rows.size());
        for (int i = pageBegin; i < pageEnd; i++) {
            if (ret.length() > 0)
                ret.append(f.getNewLine());
            ret.append(rows.get(i));
        }
        for (int i = 0; i < range[1]; i++) {
            if ((rows.size() - i - 1) > range[0]) {
                if (ret.length() > 0)
                    ret.append(f.getNewLine());
                ret.append(rows.get(rows.size() - i - 1));
            }
        }
        if (getPageCount() > 1) {
            return ret.toString().replace(getPageDisplay(), f.getString("bukkit.help.page", getPageCount(), getSelectedPage()));
        } else {
            return ret.toString().replaceAll(Pattern.quote(getPageDisplay()) + " ?", "");
        }
    }

    // Interface Appendable
    @Override
    public Appendable append(final CharSequence arg0) {
        String[] lines = arg0.toString().split("\r?\n|\r");
        for (int i = 0; i < lines.length; i++) {
            if (i > 0)
                appendln();
            rows.getLast().append(lines[i]);
        }
        return this;
    }
    @Override
    public Appendable append(final char arg0) {
        return append(String.valueOf(arg0));
    }
    @Override
    public Appendable append(final CharSequence arg0, final int arg1, final int arg2) {
        return append(arg0.subSequence(arg1, arg2));
    }
    // Interface CharSequence
    @Override
    public char charAt(final int arg0) {
        return toString().charAt(arg0);
    }
    @Override
    public int length() {
        return toString().length();
    }
    @Override
    public CharSequence subSequence(final int arg0, final int arg1) {
        return toString().subSequence(arg0, arg1);
    }
}
