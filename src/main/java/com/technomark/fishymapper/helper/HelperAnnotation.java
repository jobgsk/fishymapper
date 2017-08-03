package com.technomark.fishymapper.helper;

import com.technomark.fishymapper.annotation.TableColumn;
import com.technomark.fishymapper.annotation.TableEntity;
import com.technomark.fishymapper.annotation.TableJoin;
import com.technomark.fishymapper.dao.IModel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by troy on 7/2/17.
 */
public class HelperAnnotation {

    public static String getTableName(Class clazz) {
        if (!clazz.isAnnotationPresent(TableEntity.class)) {
            return null;
        }

        Annotation tableAnnotation = clazz.getAnnotation(TableEntity.class);
        TableEntity dt = (TableEntity) tableAnnotation;

        return dt.name();
    }

    public static TableColumn getTableColumn(Class clazz, String fieldName) {
        try {
            System.out.println("Debug "+clazz.getName()+"  "+fieldName);
            Field field = clazz.getDeclaredField(fieldName);

            if (!field.isAnnotationPresent(TableColumn.class)) {
                return null;
            }

            Annotation annotation = field.getAnnotation(TableColumn.class);
            return  (TableColumn) annotation;

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getColumnName(Class clazz, String fieldName) {
        TableColumn tableColumn = getTableColumn(clazz, fieldName);
        if (tableColumn == null)
            return null;

        return tableColumn.name();
    }

    public static String getReferencedField(Class clazz, String fieldName) {
        try {
            /*
             * if MANY_TO_ONE, MANY_TO_MANY return column name
             */
            Field field = clazz.getDeclaredField(fieldName);
            if (List.class.isAssignableFrom(field.getType())) {
                TableJoin tableJoin1 = getTableJoin(clazz, fieldName);
                if (tableJoin1 == null)
                    return null;

                return tableJoin1.referencedField();
            }

            /*
             * if ONE_TO_ONE, ONE_TO_MANY return referencedField from current annotation
             */
            return getColumnName(clazz, fieldName);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getJoinType(Class clazz, String fieldName) {

        TableJoin tableJoin1 = getTableJoin(clazz, fieldName);
        if (tableJoin1 == null)
            return null;

        String joinType = tableJoin1.joinType().toString();
        return joinType;
    }

    public static TableJoin getTableJoin(Class clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);

            if (!field.isAnnotationPresent(TableJoin.class)) {
                return null;
            }

            Annotation annotation = field.getAnnotation(TableJoin.class);
            TableJoin tableJoin = (TableJoin) annotation;

            return tableJoin;

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getJoinField(Class entityType, String fieldName, Class joinType) {
        try {
            TableJoin tableJoin1 = getTableJoin(entityType, fieldName);
            if (tableJoin1 == null)
                return null;

            String joinField = tableJoin1.mappedBy();
            //System.out.println("getJoinField 1 "+joinField + " " +joinType);

            String joinColumn = getColumnName(joinType, joinField);
            if (joinColumn == null)
                return tableJoin1.referencedField();

            return joinColumn;

        } catch (Throwable ex) {
            return null;
        }
    }

    public static IModel createModel(Class clazz, String alias, ResultSet rs) {
        try {
            IModel model = (IModel) clazz.newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                Class fieldType = field.getType();
                String fieldName = field.getName();

                if (field.isAnnotationPresent(TableColumn.class)) {
                    try {
                        Annotation annotation = field.getAnnotation(TableColumn.class);
                        TableColumn tableColumn = (TableColumn) annotation;

                        TableColumn.FieldType columnType = tableColumn.type();
                        String columnName = alias + "." + tableColumn.name();

                        String setter = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                        if (columnType == TableColumn.FieldType.VARCHAR)
                            clazz.getMethod(setter, fieldType).invoke(model, rs.getString(columnName));

                        else if (columnType == TableColumn.FieldType.LONG)
                            clazz.getMethod(setter, fieldType).invoke(model, rs.getLong(columnName));

                        else if (columnType == TableColumn.FieldType.INT)
                            clazz.getMethod(setter, fieldType).invoke(model, rs.getInt(columnName));

                    } catch (Throwable ex) {
                        System.out.println(ex);
                    }
                }
            }

            return model;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
