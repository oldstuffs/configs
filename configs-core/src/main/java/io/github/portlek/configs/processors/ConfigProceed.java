/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free from charge, to any person obtaining a copy
 * from this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies from the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions from the Software.
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

package io.github.portlek.configs.processors;

import io.github.portlek.configs.FlManaged;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.configs.util.GeneralUtilities;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class ConfigProceed {

    @NotNull
    private final Config config;

    @NotNull
    private final Object parentObject;

    @NotNull
    private final FlManaged managed;

    public ConfigProceed(@NotNull final Config config, @NotNull final FlManaged managed) {
        this(config, managed, managed);
    }

    @SneakyThrows
    public void load() {
        final FileType type = this.config.type();
        final String name;
        if (this.config.value().endsWith(type.suffix)) {
            name = this.config.value();
        } else {
            name = this.config.value() + type.suffix;
        }
        final File file = new File(
            GeneralUtilities.addSeparator(
                this.config.location()
                    .replace("%basedir%",
                        GeneralUtilities.basedir(this.managed.getClass()).getParentFile().getAbsolutePath())),
            name);
        if (this.config.copyDefault() && !file.exists()) {
            GeneralUtilities.saveResource(this.parentObject.getClass(), file,
                GeneralUtilities.addSeparator(this.config.resourcePath()) + name);
        } else if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        this.managed.setup(file, type.load(file));
        new FieldsProceed(this.parentObject, this.managed).load();
        this.managed.save();
    }

}
