package com.technomark.fishymapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD) //on class level
public @interface TableJoin {
    public enum JoinType {
        INNER("INNER"), LEFT("LEFT"), RIGHT("RIGHT");
        private String value;

        JoinType(String value) { this.value = value; }

        public String toString() {
            return this.value;
        }
    }

    String mappedBy() default "id"; //field name
    JoinType joinType();
    boolean cascade() default false;
    /*
     * Only used for: MANY_TO_ONE, MANY_TO_MANY
     */
    String referencedField() default "id";
    String referenceTable() default "";
}
