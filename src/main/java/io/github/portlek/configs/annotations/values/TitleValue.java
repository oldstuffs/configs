package io.github.portlek.configs.annotations.values;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TitleValue {

    @NotNull
    String title() default "";

    @NotNull
    String subTitle() default "";

    int fadeIn() default 20;

    int showTime() default 20;

    int fadeOut() default 20;

}
