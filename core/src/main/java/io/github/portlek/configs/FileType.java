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

package io.github.portlek.configs;

import io.github.portlek.configs.files.json.JsonConfiguration;
import io.github.portlek.configs.files.yaml.FileConfiguration;
import io.github.portlek.configs.files.yaml.YamlConfiguration;
import java.io.File;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public enum FileType {

    YAML(".yml", YamlConfiguration::loadConfiguration),
    JSON(".json", JsonConfiguration::loadConfiguration);

    @NotNull
    public final String suffix;

    @NotNull
    private final Function<File, FileConfiguration> file;

    FileType(@NotNull final String sffix, @NotNull final Function<File, FileConfiguration> fle) {
        this.suffix = sffix;
        this.file = fle;
    }

    @NotNull
    public FileConfiguration load(@NotNull final File file) {
        return this.file.apply(file);
    }

}
