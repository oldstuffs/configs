package io.github.portlek.configs.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface File {

    @NotNull
    String fileName() default "config.yml";

    @NotNull
    String resourcePath() default "";

    boolean copyDefault() default true;

}
