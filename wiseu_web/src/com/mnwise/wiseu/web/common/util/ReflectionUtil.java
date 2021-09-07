package com.mnwise.wiseu.web.common.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reflection Utility Class 리플렉션 관련 유틸리티 클래스
 */
public class ReflectionUtil {
    private static final Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * Get a field's value.
     *
     * @param field The class field.
     * @param bean The specific object to examine.
     * @return The value of the field.
     */
    public static Object getFieldValue(Field field, Object bean) {
        try {
            return field.get(bean);
        } catch(Exception e) {
            log.error(null, e);
            throw new RuntimeException("Cannot get " + field.getName() + " Error: " + e.getMessage());
        }
    }

    /**
     * Get all declared fields - helpful for retrieving all the fields of an object, including those inherited from parent classes.
     *
     * @param target The class to examine.
     * @return Array of fields.
     */
    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    public static Field[] getAllDeclaredFields(Class target) {
        // Recursing
        List temporary = new LinkedList();
        if(target.getSuperclass() != null) {
            Field[] allFields = getAllDeclaredFields(target.getSuperclass());
            temporary.addAll(Arrays.asList(allFields));
        }

        // Fetching all class fields
        Field[] currentFields = target.getDeclaredFields();
        temporary.addAll(Arrays.asList(currentFields));
        return (Field[]) temporary.toArray(new Field[temporary.size()]);
    }

    /**
     * Get all declared fields - helpful for retrieving all the fields of an object, including those inherited from parent classes.
     *
     * @param bean The object to examine.
     * @return List of all the fields.
     */
    public static List<Field> getFields(Object bean) {
        Field[] fields = getAllDeclaredFields(bean.getClass());
        AccessibleObject.setAccessible(fields, true);
        return Arrays.asList(fields);
    }

    /**
     * Set a field's value.
     *
     * @param field The class field to set.
     * @param bean The specific object that contains the field.
     * @param value The value that will be applied to the field.
     */
    public static void setField(Field field, Object bean, Object value) {
        try {
            if(value != null) {
                field.set(bean, value);
            }
        } catch(IllegalArgumentException e) {
            throw new RuntimeException("Cannot set " + field.getName() + " = (" + value + ") Error: " + e.getMessage());
        } catch(IllegalAccessException e) {
            throw new RuntimeException("Cannot set " + field.getName() + " = (" + value + ") Error: " + e.getMessage());
        }
    }

    /**
     * Get a field's value.
     *
     * @param field The class field.
     * @param bean The specific object to examine.
     * @return The value of the field. public static Object getFieldValue(Field field, Object bean) { try { return field.get(bean); } catch (IllegalArgumentException e) { e.printStackTrace(); throw
     *         new RuntimeException("Cannot get " + field.getName() + " Error: " + e.getMessage()); } catch (IllegalAccessException e) { e.printStackTrace(); throw new RuntimeException("Cannot get " +
     *         field.getName() + " Error: " + e.getMessage()); } } /** Factory method that returns a new instance of the given Class.
     * @param createClass The Class to create an object from.
     * @return A newly created object of the Class.
     */
    @SuppressWarnings("rawtypes")
    public static Object newInstance(Class createClass) {
        try {
            return createClass.newInstance();
        } catch(InstantiationException e) {
            throw new RuntimeException("Cannot create " + createClass.getName() + ": " + e.getMessage());

        } catch(IllegalAccessException e) {
            throw new RuntimeException("Cannot create " + createClass.getName() + ": " + e.getMessage());
        }
    }
}
