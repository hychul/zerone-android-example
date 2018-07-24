package com.hychul.zerone.annotation;

import com.hychul.zerone.core.Component;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface RequireComponent {
    Class<? extends Component>[] value();
}
