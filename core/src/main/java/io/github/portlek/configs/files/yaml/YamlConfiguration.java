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

package io.github.portlek.configs.files.yaml;

import io.github.portlek.configs.configuration.exceptions.InvalidConfigurationException;
import io.github.portlek.configs.configuration.file.FileConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

public final class YamlConfiguration extends FileConfiguration {

    @NotNull
    public static YamlConfiguration loadConfiguration(@NotNull final File file) {
        return YamlConfiguration.run(config -> config.load(file));
    }

    @NotNull
    private static YamlConfiguration run(@NotNull final YamlConfiguration.YamlRunnable runnable) {
        final YamlConfiguration config = new YamlConfiguration();
        try {
            runnable.run(config);
        } catch (final IOException | InvalidConfigurationException ex) {
            Logger.getLogger(YamlConfiguration.class.getName()).log(Level.SEVERE, "Cannot load configuration from stream", ex);
        }
        return config;
    }

    @SneakyThrows
    @NotNull
    @Override
    public String saveToString() {
        return Helper.mapAsYamlMapping(this.getValues(false)).toString();
    }

    @SneakyThrows
    @Override
    public void loadFromString(@NotNull final String contents) {
        Helper.loadFromString(this, contents);
    }

    @NotNull
    @Override
    public YamlConfigurationOptions options() {
        if (this.options == null) {
            this.options = new YamlConfigurationOptions(this);
        }
        return (YamlConfigurationOptions) this.options;
    }

    private interface YamlRunnable {

        void run(@NotNull YamlConfiguration config) throws IOException, InvalidConfigurationException;

    }

}
