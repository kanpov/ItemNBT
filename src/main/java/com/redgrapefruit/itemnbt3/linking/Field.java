package com.redgrapefruit.itemnbt3.linking;

import com.redgrapefruit.itemnbt3.serializer.SerializerRegistry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a contract for adding a <b>regular field</b> when generating a {@link DataLink}.<br><br>
 * A regular field is a serialized field that uses a built-in/simple serializer, registered in {@link SerializerRegistry}.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
    String name() default "^NULL";
}
