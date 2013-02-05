package de.jaschastarke.bukkit.lib.chat;

public interface IPagination extends Appendable, CharSequence {
    public String getPageDisplay();
    /**
     * Appends a new line.
     */
    public void appendln();
    /**
     * Appends a new line AFTER the inserted text. Like System.out.println
     */
    public void appendln(CharSequence line);
    public void setFixedLines(int top, int bottom);
    /**
     * Selects the page by the numeric value in the first string-array argument. Returns the string-array without the
     * first parameter if interpreted as a page (numeric value). Doesn't change the page (default 0) if no numeric
     * value found.
     */
    public String[] selectPage(String[] args);
    public void selectPage(int page);
    public int getPageCount();
    /**
     * Returns the current selected page, if a page higher then the {@see getPageCount} is selected 0 is returned. So
     * it always gets a page containing content.
     */
    public int getSelectedPage();
    
    // Remove throws from Appendable
    public Appendable append(CharSequence arg0);
    public Appendable append(CharSequence arg0, int arg1, int arg2);
    public Appendable append(char arg0);
}
