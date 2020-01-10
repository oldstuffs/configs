package io.github.portlek.configs.annotations;

import io.github.portlek.configs.FileType;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface File {

    @NotNull
    String fileName();

    @NotNull
    String configVersion();

    @NotNull
    FileType fileType() default FileType.YAML;

    @NotNull
    String resourcePath() default "";

    boolean copyDefault() default false;

    @NotNull
    String header() default "";

}
