package com.redgrapefruit.itemnbt3.linking;

import com.redgrapefruit.itemnbt3.specification.Specification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks that when generating a {@link Specification} for this type, inherited fields can also be used.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowInheritance {
}
