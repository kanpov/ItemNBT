package com.redgrapefruit.itemnbt3.linking;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Notifies the system to automatically generate the addresses for the {@link DataLink}.<br>
 * All public fields are included, their addresses equal their actual names.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auto {
}
