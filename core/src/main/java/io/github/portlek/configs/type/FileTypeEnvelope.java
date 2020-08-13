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

package io.github.portlek.configs.type;

import io.github.portlek.configs.BiCons;
import io.github.portlek.configs.FileType;
import io.github.portlek.configs.Func;
import java.io.File;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class FileTypeEnvelope implements FileType {

    @NotNull
    private final String suffix;

    @NotNull
    private final Func<File, FileConfiguration> loadFunc;

    @NotNull
    private final BiCons<FileConfiguration, File> saveFunc;

    protected FileTypeEnvelope(@NotNull final String suffix, @NotNull final Func<File, FileConfiguration> loadFunc) {
        this(suffix, loadFunc, FileConfiguration::save);
    }

    @NotNull
    @Override
    public final String suffix() {
        return this.suffix;
    }

    @NotNull
    @Override
    public final FileConfiguration load(@NotNull final File file) throws Exception {
        return this.loadFunc.apply(file);
    }

    @Override
    public final void save(@NotNull final FileConfiguration configuration, @NotNull final File file) throws Exception {
        this.saveFunc.accept(configuration, file);
    }

}
