/*
 * MIT License
 *
 * Copyright (c) 2019 Hasan Demirtaş
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

package io.github.portlek.configs.util;

import com.dumptruckman.bukkit.configuration.json.JsonConfiguration;
import java.io.File;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

public enum FileType {

    YAML(".yml", YamlConfiguration::loadConfiguration),
    JSON(".json", JsonConfiguration::loadConfiguration);

    @NotNull
    public final String suffix;

    @NotNull
    private final Function<File, FileConfiguration> consumer;

    FileType(@NotNull String suffix, @NotNull Function<File, FileConfiguration> consumer) {
        this.suffix = suffix;
        this.consumer = consumer;
    }

    @NotNull
    public FileConfiguration load(@NotNull File file) {
        return consumer.apply(file);
    }

}
