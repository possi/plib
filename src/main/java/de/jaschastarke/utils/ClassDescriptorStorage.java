package de.jaschastarke.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.Properties;

import org.apache.commons.lang.ClassUtils;

import de.jaschastarke.MultipleResourceBundle;

public final class ClassDescriptorStorage {
    private static final String NEWLINE = "\n";
    
    private static ClassDescriptorStorage instance = null;
    @Deprecated // lets try avoiding singletons
    public static ClassDescriptorStorage getInstance() {
        if (instance == null) {
            instance = new ClassDescriptorStorage();
        }
        return instance;
    }
    
    private String target = "META-INF.doccomments";
    private Locale locale = null;
    private URLClassLoader loader = null;
    private MultipleResourceBundle rb = null;
    public ClassDescriptorStorage() {
    }
    public ClassDescriptorStorage(final Locale locale) {
        this.locale = locale;
    }
    public ClassDescriptorStorage(final URLClassLoader loader) {
        this.loader = loader;
    }
    /**
     * @param locale
     * @param target 
     * @param loader
     */
    public ClassDescriptorStorage(final Locale locale, final String target, final URLClassLoader loader) {
        this.locale = locale;
        if (target != null)
            this.target = target;
        this.loader = loader;
    }
    
    public MultipleResourceBundle getResourceBundle() {
        if (rb == null)
            rb = new MultipleResourceBundle(locale, new String[]{this.target}, loader);
        return rb;
    }
    
    private Map<String, ClassDescription> descriptions = new HashMap<String, ClassDescription>();
    public ClassDescription getClassFor(final String cls) {
        if (!descriptions.containsKey(cls)) {
            ClassDescription desc = new ClassDescription(cls);
            descriptions.put(cls, desc);
            return desc;
        }
        return descriptions.get(cls);
    }
    public ClassDescription getClassFor(final Class<?> cls) {
        return getClassFor(cls.getName());
    }
    public ClassDescription getClassFor(final Object cls) {
        return getClassFor(cls.getClass().getName());
    }
    
    public void store() {
        store(new File(getTargetPath()));
    }
    
    public String getTargetPath() {
        return target.replace(".", "/") + ".properties";
    }
    
    public String getTarget() {
        return target;
    }
    public void store(final File file) {
        Properties prop = new Properties();
        for (Map.Entry<String, ClassDescription> cls : descriptions.entrySet()) {
            DocComment comment = cls.getValue().getDocComment(false);
            if (comment != null)
                prop.setProperty(cls.getKey(), comment.toString());
            for (Entry<String, DocComment> el : cls.getValue().getElements().entrySet()) {
                prop.setProperty(cls.getKey() + "*" + el.getKey(), el.getValue().toString());
            }
        }

        try {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            prop.store(new FileOutputStream(file), "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /*OutputStream fos = null;
        ObjectOutputStream o = null;
        
        file.getParentFile().mkdirs();
        
        try {
          fos = new FileOutputStream(file);
          o = new ObjectOutputStream(fos);
          o.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                o.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }
    /*public static ClassDescriptorStorage load(final File file) throws IOException {
        InputStream fis = null;
        fis = new FileInputStream(file);
        load(fis);
        fis.close();
        return instance;
    }

    public static ClassDescriptorStorage load(final InputStream resource) throws IOException {
        ObjectInputStream o = null;

        try {
          o = new ObjectInputStream(resource);
          instance = (ClassDescriptorStorage) o.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        } finally {
            try {
                o.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }*/
    
    @Override
    public String toString() {
        String str = "";
        for (Map.Entry<String, ClassDescription> entry : descriptions.entrySet()) {
            str += entry.getValue().toString() + NEWLINE;
        }
        return str.trim();
    }
    
    public class ClassDescription {
        private String name;
        private DocComment comment;
        private Map<String, DocComment> elComments = new HashMap<String, DocComment>();
        
        public ClassDescription(final String cls) {
            name = cls;
        }
        public void setDocComment(final String doc) {
            comment = new DocComment(doc);
        }
        public void setElDocComment(final String el, final String doc) {
            elComments.put(el, new DocComment(doc));
        }
        public DocComment getDocComment() {
            return getDocComment(true);
        }
        public DocComment getDocComment(final boolean load) {
            if (load && comment == null) {
                try {
                    comment = new DocComment(getResourceBundle().getString(name));
                } catch (MissingResourceException e) {
                    comment = null;
                }
            }
            return comment;
        }
        public DocComment getElDocComment(final String el) {
            if (!elComments.containsKey(el)) {
                try {
                    elComments.put(el, new DocComment(getResourceBundle().getString(name + "*" + el)));
                } catch (MissingResourceException e) {
                    elComments.put(el, null);
                }
            }
            return elComments.get(el);
        }
        public Class<?> getTheClass() throws ClassNotFoundException {
            return ClassUtils.getClass(name);
        }
        public Map<String, DocComment> getElements() {
            return elComments;
        }
        
        @Override
        public String toString() {
            String str = name + ":\n";
            for (Map.Entry<String, DocComment> el : elComments.entrySet()) {
                str += "  " + el.getKey() + ": " + el.getValue().toString() + NEWLINE;
            }
            return str;
        }
    }
}
