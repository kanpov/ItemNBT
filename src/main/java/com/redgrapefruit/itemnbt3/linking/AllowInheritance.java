package com.redgrapefruit.itemnbt3.linking;

import com.redgrapefruit.itemnbt3.specification.Specification;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks that when generating a {@link Specification} for this type, inherited fields can also be used.
 * <br><br>
 * <b>This feature is experimental</b> and not production-ready yet. Use with caution!
 */
@ApiStatus.Experimental
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowInheritance {
}
