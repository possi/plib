package de.jaschastarke;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MultipleResourceBundle extends ResourceBundle {
    private Locale locale;
    private List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
    private ClassLoader loader = null;

    public MultipleResourceBundle(final Locale locale, final Collection<String> bundleNames) {
        this.locale = locale == null ? Locale.getDefault() : locale;
        for (String bundleName : bundleNames) {
            if (bundleName != null)
                addResourceBundle(bundleName);
        }
    }
    public MultipleResourceBundle(final Locale locale, final String[] bundleNames) {
        this.locale = locale == null ? Locale.getDefault() : locale;
        for (String bundleName : bundleNames) {
            if (bundleName != null)
                addResourceBundle(bundleName);
        }
    }
    public MultipleResourceBundle(final Locale locale, final String bundleName) {
        this.locale = locale == null ? Locale.getDefault() : locale;
        if (bundleName != null)
            addResourceBundle(bundleName);
    }
    public MultipleResourceBundle(final Locale locale, final String[] bundleNames, final URLClassLoader loader) {
        this.locale = locale == null ? Locale.getDefault() : locale;
        this.loader = loader;
        for (String bundleName : bundleNames) {
            if (bundleName != null)
                addResourceBundle(bundleName);
        }
    }
    public void addResourceBundle(final String bundleName) {
        if (loader != null)
            bundles.add(ResourceBundle.getBundle(bundleName, locale, loader));
        else
            bundles.add(ResourceBundle.getBundle(bundleName, locale));
    }
    
    @Override
    public Enumeration<String> getKeys() {
        return null;
    }

    @Override
    protected Object handleGetObject(final String key) {
        Object result = null;
        MissingResourceException nfe = null;
        for (ResourceBundle bundle : bundles) {
            try {
                result = bundle.getObject(key);
                if (result != null)
                    break;
            } catch (MissingResourceException exc) {
                nfe = exc;
            }
        }
        if (result == null) {
            throw nfe;
        }
        return result;
    }
}
