package de.jaschastarke;

import java.io.Serializable;

public class LocaleString implements CharSequence, Comparable<String>, Serializable {
    private static final long serialVersionUID = 726492298397484523L;
    
    private String value;
    private String transValue = null;
    private Object[] objects;
    
    public LocaleString(final String value, final Object... objects) {
        this.value = value;
        this.objects = objects;
    }
    
    public String getRawValue() {
        return value;
    }
    
    public Object[] getObjects() {
        return objects;
    }
    
    public boolean isTranslated() {
        return transValue != null;
    }
    
    public String toString() {
        return transValue != null ? transValue : value;
    }
    
    public String translate(final I18n lang) {
        transValue = lang.trans(value, objects);
        return transValue;
    }
    
    @Override
    public int compareTo(final String o) {
        return toString().compareTo(o.toString());
    }
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
