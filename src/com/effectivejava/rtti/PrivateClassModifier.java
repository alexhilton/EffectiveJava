package com.effectivejava.rtti;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * See how we can change a class whose all fields and methods are private
 * {@link PrivateClass}
 * @author AM
 *
 */
public class PrivateClassModifier {
    private static Pattern pattern = Pattern.compile("\\w+\\.");
    
    public static void main(String[] args) {
        try {
            final Class<?> clazz = Class.forName("com.effectivejava.rtti.PrivateClass");
            showPrivateFields(clazz);
            callPrivateMethods(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void callPrivateMethods(Class<?> clazz) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        PrivateClass obj = new PrivateClass("alex");
        Method setter = obj.getClass().getDeclaredMethod("setName", obj.getClass());
        setter.setAccessible(true);
        setter.invoke(obj, "bruce");
    }

    private static void showPrivateFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        System.out.println("private fields:");
        for (Field f : fields) {
            System.out.println("\t" + f.toString());
        }
    }
}