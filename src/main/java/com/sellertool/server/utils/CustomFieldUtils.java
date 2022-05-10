package com.sellertool.server.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomFieldUtils {
    public static <T> List<Field> getAllFields(T t) {
        Objects.requireNonNull(t);

        Class<?> clazz = t.getClass();
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {    // 1. 상위 클래스가 null 이 아닐때까지 모든 필드를 list 에 담는다.
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static <T> List<String> getAllFieldNames(T t){
        Objects.requireNonNull(t);

        Field field = null;

        return getAllFields(t).stream().map(r-> r.getName()).collect(Collectors.toList());
    }

    public static <T> Field getFieldByName(T t, String fieldName) {
        Objects.requireNonNull(t);

        Field field = null;
        for (Field f : getAllFields(t)) {
            if (f.getName().equals(fieldName)) {
                field = f;    // 2. 모든 필드들로부터 fieldName이 일치하는 필드 추출
                break;
            }
        }
        if (field != null) {
            field.setAccessible(true);    // 3. 접근 제어자가 private 일 경우
        }
        return field;
    }

    public static <T> T getFieldValueWithSuper(Object obj, String fieldName) {
        Objects.requireNonNull(obj);

        try {
            Field field = getFieldByName(obj, fieldName); // 4. 해당 필드 조회 후
            return (T) field.get(obj);    // 5. get 을 이용하여 field value 획득
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static <T> T getFieldValue(Object obj, String fieldName) {
        Objects.requireNonNull(obj);

        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);    // 5. get 을 이용하여 field value 획득
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static void setFieldValueWithSuper(Object obj, String fieldName, Object value) {
        Objects.requireNonNull(obj);

        try {
            Field field = getFieldByName(obj, fieldName); // 4. 해당 필드 조회 후
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) {
        Objects.requireNonNull(obj);

        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
