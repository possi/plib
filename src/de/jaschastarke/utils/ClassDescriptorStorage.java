package de.jaschastarke.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ClassUtils;

public class ClassDescriptorStorage implements Serializable {
    private static final long serialVersionUID = -8882669403001425791L;
    
    private static ClassDescriptorStorage instance = null;
    private ClassDescriptorStorage() {}
    public static ClassDescriptorStorage getInstance() {
        if (instance == null)
            instance = new ClassDescriptorStorage();
        return instance;
    }
    
    private Map<String, ClassDescription> descriptions = new HashMap<String, ClassDescription>();
    public ClassDescription getClassFor(String cls) {
        if (!descriptions.containsKey(cls)) {
            ClassDescription desc = new ClassDescription(cls);
            descriptions.put(cls, desc);
            return desc;
        }
        return descriptions.get(cls);
    }
    public ClassDescription getClassFor(Class<?> cls) {
        return getClassFor(cls.getName());
    }
    public ClassDescription getClassFor(Object cls) {
        return getClassFor(cls.getClass().getName());
    }
    
    public void store(File file) {
        OutputStream fos = null;
        ObjectOutputStream o = null;
        
        file.getParentFile().mkdirs();
        
        try {
          fos = new FileOutputStream(file);
          o = new ObjectOutputStream(fos);
          o.writeObject(this);
        } catch (IOException e) {
            System.err.println( e );
        } finally {
            try {
                o.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static ClassDescriptorStorage load(File file) throws IOException {
        InputStream fis = null;
        fis = new FileInputStream(file);
        load(fis);
        fis.close();
        return instance;
    }

    public static ClassDescriptorStorage load(InputStream resource) throws IOException {
        ObjectInputStream o = null;

        try {;
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
    }
    
    @Override
    public String toString() {
        String str = "";
        for (Map.Entry<String, ClassDescription> entry : descriptions.entrySet()) {
            str += entry.getValue().toString()+"\n";
        }
        return str.trim();
    }
    
    public class ClassDescription implements Serializable {
        private static final long serialVersionUID = -4605521733206873274L;
        
        private String name;
        private DocComment comment;
        private Map<String, DocComment> el_comments = new HashMap<String, DocComment>();
        
        public ClassDescription(String cls) {
            name = cls;
        }
        public void setDocComment(String doc) {
            comment = new DocComment(doc);
        }
        public void setElDocComment(String el, String doc) {
            el_comments.put(el, new DocComment(doc));
        }
        public DocComment getDocComment() {
            return comment;
        }
        public DocComment getElDocComment(String el) {
            return el_comments.get(el);
        }
        public Class<?> getTheClass() throws ClassNotFoundException {
            return ClassUtils.getClass(name);
        }
        
        @Override
        public String toString() {
            String str = name + ":\n";
            for (Map.Entry<String, DocComment> el : el_comments.entrySet()) {
                str += "  " + el.getKey() + ": " + el.getValue().toString()+"\n";
            }
            return str;
        }
    }
}
