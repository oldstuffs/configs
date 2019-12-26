package io.github.portlek.configs.annotations;

import io.github.portlek.configs.MagicConstants;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TitleValue {

    @NotNull
    String title() default MagicConstants.NULL_STRING;

    @NotNull
    String subTitle() default MagicConstants.NULL_STRING;

    int fadeIn() default 20;

    int showTime() default 20;

    int fadeOut() default 20;

}
