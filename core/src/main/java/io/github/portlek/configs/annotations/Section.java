package io.github.portlek.configs.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Section {

    @NotNull
    String path() default "";

    @NotNull
    String separator() default "-";

    @NotNull
    String[] comment() default {};

}
