package com.redgrapefruit.itemnbt3.specification.linking;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Setter {
    String to();
}
