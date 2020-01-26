/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.github.portlek.configs.annotations;

import io.github.portlek.configs.FileType;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is for creating basic file
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {

    /**
     * @return The file name which you don't have to write {@link FileType#suffix}.
     */
    @NotNull
    String name();

    /**
     * @return The file version which will use on migrating system.
     */
    @NotNull
    String version();

    /**
     * @return The version path where will write.
     */
    @NotNull
    String versionPath() default "file-version";

    /**
     * @return The file location where will create.
     */
    @NotNull
    String location() default "%basedir%";

    /**
     * @return The file type which will create as.
     */
    @NotNull
    FileType type() default FileType.YAML;

    /**
     * @return The resource path where will create from.
     */
    @NotNull
    String resourcePath() default "";

    /**
     * @return The copy default {@link Boolean} if you want to create the file from the {@link #resourcePath()}
     */
    boolean copyDefault() default false;

}
