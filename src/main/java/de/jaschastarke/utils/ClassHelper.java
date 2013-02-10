package de.jaschastarke.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public final class ClassHelper {
    //private static final String PACKAGE_CLASS_SEP = ".";
    //private static final String PACKAGE_CLASS_SEP_RE = Pattern.quote(PACKAGE_CLASS_SEP);
    
    private ClassHelper() {
    }
    
    public static Object getInstance(final String classIdentifier)
            throws NoSuchFieldException, ClassNotFoundException, InstantiationException,
                    IllegalAccessException, InvocationTargetException {
        return getInstance(classIdentifier, null);
    }
    public static Object getInstance(final String classIdentifier, final ClassLoader loader)
            throws NoSuchFieldException, ClassNotFoundException, InstantiationException,
                    IllegalAccessException, InvocationTargetException {
        String[] parts = classIdentifier.split(":");
        Class<?> cls = loader == null ? forName(parts[0]) : forName(parts[0], loader);
        if (parts.length > 1) {
            Object ret = null;
            for (int i = 1; i < parts.length; i++) {
                Field field = cls.getField(parts[i]);
                if (i == 1 && !Modifier.isStatic(field.getModifiers())) {
                    ret = cls.newInstance();
                }
                ret = field.get(ret);
            }
            return ret;
        } else {
            return cls.newInstance();
        }
    }
    public static Class<?> forName(final String name) throws ClassNotFoundException {
        return forName(name, null);
    }
    public static Class<?> forName(final String name, final ClassLoader loader) throws ClassNotFoundException {
        String[] cls = name.split("\\$");
        Class<?> theclass = loader == null ? Class.forName(cls[0]) : loader.loadClass(cls[0]);
        for (int i = 1; i < cls.length; i++) {
            Class<?> f = null;
            for (Class<?> sub : theclass.getDeclaredClasses()) {
                if (sub.getSimpleName().equals(cls[i])) {
                    f = sub;
                    break;
                }
            }
            if (f != null) {
                theclass = f;
            } else {
                throw new ClassNotFoundException();
            }
        }
        return theclass;
        
    }
    /*public static Class<?> forName(final String name) throws ClassNotFoundException {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            // Class not found, lets try if this is a field
            String[] cls = name.split(PACKAGE_CLASS_SEP_RE);
            for (int i = cls.length; i > 0; i--) {
                String clsname = StringUtil.join(cls, PACKAGE_CLASS_SEP, 0, i);
                try {
                    Class<?> theclass = Class.forName(clsname);
                    System.out.println("Is a class: " + clsname);
                    
                    for (int j = i; j < cls.length; j++) {
                        System.out.println("looking for field: " + cls[j]);
                        Field field = theclass.getField(cls[j]);
                        if (field == null || !Modifier.isStatic(field.getModifiers()))
                            throw e;
                        System.out.println("...found");
                        theclass = field.get(null).getClass();
                    }
                    
                    return theclass;
                } catch (ClassNotFoundException e1) {
                    e1.getMessage(); // ignore but bypass checkstyle
                } catch (SecurityException e1) {
                    throw new IllegalArgumentException(e1);
                } catch (NoSuchFieldException e1) {
                    throw new IllegalArgumentException(e1);
                } catch (IllegalAccessException e1) {
                    throw new IllegalArgumentException(e1);
                }
            }
            throw e;
        }
        //return forName(name, null);
    }
    public static Class<?> forName(final String name, final ClassLoader loader) throws ClassNotFoundException {
        try {
            //return Class.forName(name, true, loader);
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            // Class not found, lets try if this is a field
            String[] cls = name.split(PACKAGE_CLASS_SEP_RE);
            for (int i = cls.length; i > 0; i--) {
                String clsname = StringUtil.join(cls, PACKAGE_CLASS_SEP, 0, i);
                try {
                    Class<?> theclass = loader.loadClass(clsname);
                    //System.out.println("Is a class: " + clsname);
                    
                    for (int j = i; j < cls.length; j++) {
                        //System.out.println("looking for field: " + cls[j]);
                        Field field = theclass.getField(cls[j]);
                        if (field == null || !Modifier.isStatic(field.getModifiers()))
                            throw e;
                        //System.out.println("...found");
                        theclass = field.get(null).getClass();
                    }
                    
                    return theclass;
                } catch (ClassNotFoundException e1) {
                    e1.getMessage(); // ignore but bypass checkstyle
                } catch (SecurityException e1) {
                    throw new IllegalArgumentException(e1);
                } catch (NoSuchFieldException e1) {
                    throw new IllegalArgumentException(e1);
                } catch (IllegalAccessException e1) {
                    throw new IllegalArgumentException(e1);
                }
            }
            throw e;
        }
    }*/
}
