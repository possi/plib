package de.jaschastarke.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.jaschastarke.Singleton;

public class ClassHelper {
    
    public static Object getInstance(String classIdentifier)
            throws SecurityException, NoSuchFieldException, ClassNotFoundException, InstantiationException,
                    IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return getInstance(classIdentifier, null);
    }
    public static Object getInstance(String classIdentifier, ClassLoader loader)
            throws SecurityException, NoSuchFieldException, ClassNotFoundException, InstantiationException,
                    IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
        } else if (cls.isAssignableFrom(Singleton.class)) {
            try {
                Method method = cls.getMethod("getInstance");
                return method.invoke(null);
            } catch (NoSuchMethodException e) {
                return cls.newInstance();
            }
        } else {
            return cls.newInstance();
        }
    }
    
    public static Class<?> forName(String name) throws ClassNotFoundException {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            // Class not found, lets try if this is a field
            String cls[] = name.split("\\.");
            for (int i = cls.length; i > 0; i--) {
                String clsname = StringUtil.join(cls, ".", 0, i);
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
                    System.out.println("Not a class: " + clsname);;
                } catch (SecurityException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (NoSuchFieldException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IllegalArgumentException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            throw e;
        }
        //return forName(name, null);
    }
    public static Class<?> forName(String name, ClassLoader loader) throws ClassNotFoundException {
        try {
            //return Class.forName(name, true, loader);
            return loader.loadClass(name);
        } catch (ClassNotFoundException e) {
            // Class not found, lets try if this is a field
            String cls[] = name.split("\\.");
            for (int i = cls.length; i > 0; i--) {
                String clsname = StringUtil.join(cls, ".", 0, i);
                try {
                    Class<?> theclass = loader.loadClass(clsname);
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
                    System.out.println("Not a class: " + clsname);;
                } catch (SecurityException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (NoSuchFieldException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IllegalArgumentException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            throw e;
        }
    }
}
