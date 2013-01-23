package de.jaschastarke;

import java.io.Serializable;

public class LocaleString implements CharSequence, Comparable<String>, Serializable {
    private static final long serialVersionUID = 726492298397484523L;
    
    private String value;
    private String trans_value = null;
    private Object[] objects;
    
    public LocaleString(String value, Object... objects) {
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
        return trans_value != null;
    }
    
    public String toString() {
        return trans_value == null ? trans_value : value;
    }
    
    public String translate(i18n lang) {
        trans_value = lang.trans(value, objects);
        return trans_value;
    }
    
    @Override
    public int compareTo(String o) {
        return toString().compareTo(o.toString());
    }
    @Override
    public char charAt(int arg0) {
        return toString().charAt(arg0);
    }
    @Override
    public int length() {
        return toString().length();
    }
    @Override
    public CharSequence subSequence(int arg0, int arg1) {
        return toString().subSequence(arg0, arg1);
    }

}
