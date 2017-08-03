package com.technomark.fishymapper.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //on class level
public @interface TableColumn {
    public enum FieldType {
        VARCHAR, LONG, INT
        //VARCHAR(Types.VARCHAR), LONG(Types.BIGINT), INT (Types.INTEGER);
        //private int value;
        //FieldType(int value) { this.value = value; }
        //public int getValue() { return this.value; }
    }

    String name();
    FieldType type();

    boolean autogen() default false;
    boolean nullable() default false;
}

