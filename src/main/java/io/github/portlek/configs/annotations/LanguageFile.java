package io.github.portlek.configs.annotations;

import io.github.portlek.configs.FileType;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LanguageFile {

    @NotNull
    String fileName() default "";

    @NotNull
    FileType fileType() default FileType.YAML;

    boolean copyDefault() default false;

}
