package com.redgrapefruit.itemnbt3.linking;

import com.redgrapefruit.itemnbt3.serializer.SerializerRegistry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a contract for adding a <b>composite field</b> when generating a {@link DataLink}.<br><br>
 * A composite field is a field that is not of a built-in serializer (registered in {@link SerializerRegistry}), but is
 * a type that will be broken down recursively into other normal fields and composite fields.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Composite {
    String name() default "^NULL";
}
