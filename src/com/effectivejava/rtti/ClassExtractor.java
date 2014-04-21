package com.effectivejava.rtti;

import java.lang.reflect.*;
import java.util.regex.Pattern;

/**
 * Extractor all the information for a class whose name given in command line.
 * including: 
 *      Fields
 *      Methods
 *      Constructors
 *      enums
 *      Inner Classes
 *      Inner Interfaces
 *      Base classes
 *      Base interfaces 
 * @author AM
 *
 */
public class ClassExtractor {
    /** to discard qualifiers */
    private static final Pattern pattern = Pattern.compile("\\w+\\.");
    
    private static String removeQualifiers(String name) {
        return pattern.matcher(name).replaceAll("");
    }
    
    public void inheritedMethod() {
        
    }
    /**
     * @param className -- must be full qualified, i.e. with package name, like: java.lang.String, otherwise you get
     * {@link ClassNotFoundException}
     * @throws ClassNotFoundException 
     */
    public static void dumpClassInfo(String className) throws ClassNotFoundException {
        final StringBuilder sb = new StringBuilder();
        final Class<?> clazz = Class.forName(className);
        sb.append(clazz.toString() + " {\n");
        // Fields
        // 'Declared' in the Class methods means all the stuff(fields, methods) declared by this class
        // Class#getDeclaredMethods(), returns all methods declared by this class, including private, public, protected and
        // package, but excluding inherited methods
        // while, Class#getMethods(), returns all the public methods, including inherited ones
        Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields != null) {
            sb.append("declared fields:\n");
            for (Field f : declaredFields) {
                sb.append("\t" + removeQualifiers(f.toString()) + "\n");
            }
        }
        Field[] fields = clazz.getFields();
        if (fields != null) {
            sb.append("feids:\n");
            for (Field f : fields) {
                sb.append("\t" + removeQualifiers(f.toString()) + "\n");
            }
        }
        // Methods
        Method[] declaredMethods = clazz.getDeclaredMethods();
        if (declaredMethods != null) {
            sb.append("declared methods:\n");
            for (Method m : declaredMethods) {
                sb.append("\t" + removeQualifiers(m.toString()) + "\n");
            }
        }
        Method[] methods = clazz.getMethods();
        if (methods != null) {
            sb.append("methods:\n");
            for (Method m : methods) {
                sb.append("\t" + removeQualifiers(m.toString()) + "\n");
            }
        }
        // Constructors
        Constructor<?>[] declaredConstructor = clazz.getDeclaredConstructors();
        if (declaredConstructor != null) {
            sb.append("declared constructors:\n");
            for (Constructor<?> c : declaredConstructor) {
                sb.append("\t" + removeQualifiers(c.toString()) + "\n");
            }
        }
        Constructor<?>[] cons = clazz.getConstructors();
        if (cons != null) {
            sb.append("constructors:\n");
            for (Constructor<?> c : cons) {
                sb.append("\t" + removeQualifiers(c.toString()) + "\n");
            }
        }
        // Enums
        Object[] enums = clazz.getEnumConstants();
        if (enums != null) {
            sb.append("enums:\n");
            for (Object o : enums) {
                sb.append("\t" + removeQualifiers(o.toString()) + "\n");
            }
        }
        // Inner classes
        Class<?>[] declaredInnerClasses = clazz.getDeclaredClasses();
        if (declaredInnerClasses != null) {
            sb.append("declared inner classes:\n");
            for (Class<?> c : declaredInnerClasses) {
                sb.append("\t" + removeQualifiers(c.toString()) + "\n");
            }
        }
        Class<?>[] innerClasses = clazz.getDeclaredClasses();
        if (innerClasses != null) {
            sb.append("inner classes:\n");
            for (Class<?> c : innerClasses) {
                sb.append("\t" + removeQualifiers(c.toString()) + "\n");
            }
        }
        // Super/Base classes
        Class<?> supers = clazz.getSuperclass();
        if (supers != null) {
            sb.append("super classes:\n");
            sb.append("\t" + removeQualifiers(supers.toString()) + "\n");
        }
        // Interfaces
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces != null) {
            sb.append("interfaces:\n");
            for (Class<?> i : interfaces) {
                sb.append("\t" + removeQualifiers(i.toString()) + "\n");
            }
        }
        sb.append("}");
        System.out.println(sb.toString());
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            dumpClassInfo("java.lang.String");
            dumpClassInfo("com.effectivejava.rtti.ClassExtractor");
            dumpClassInfo("com.effectivejava.rtti.ClassExtractorTest");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}