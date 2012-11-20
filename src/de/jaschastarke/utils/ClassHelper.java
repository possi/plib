package de.jaschastarke.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ClassHelper {
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
    }
}
