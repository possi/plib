package de.jaschastarke.bukkit.lib.chat;

public class NoPager implements IPagination {
    private StringBuilder sb = new StringBuilder(); 
    @Override
    public Appendable append(final CharSequence csq) {
        sb.append(csq);
        return this;
    }
    @Override
    public Appendable append(final char c) {
        sb.append(c);
        return this;
    }
    @Override
    public Appendable append(final CharSequence csq, final int start, final int end) {
        sb.append(csq, start, end);
        return this;
    }
    @Override
    public char charAt(final int index) {
        return sb.charAt(index);
    }
    @Override
    public int length() {
        return sb.length();
    }
    @Override
    public CharSequence subSequence(final int start, final int end) {
        return sb.subSequence(start, end);
    }
    @Override
    public String getPageDisplay() {
        return sb.toString();
    }
    @Override
    public void appendln() {
        sb.append(AbstractFormatter.NEWLINE);
    }
    @Override
    public void setFixedLines(final int top, final int bottom) {
        return;
    }
    @Override
    public void appendln(final CharSequence line) {
        sb.append(line).append(AbstractFormatter.NEWLINE);
    }
    @Override
    public String[] selectPage(final String[] args) {
        return args;
    }
    @Override
    public void selectPage(final int page) {
    }
    @Override
    public int getPageCount() {
        return 1;
    }
    @Override
    public int getSelectedPage() {
        return 0;
    }
    public String toString() {
        return sb.toString();
    }
}