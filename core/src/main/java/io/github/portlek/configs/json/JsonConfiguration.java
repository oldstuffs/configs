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

/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package io.github.portlek.configs.json;

import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.configuration.InvalidConfigurationException;
import io.github.portlek.configs.jsonparser.Json;
import io.github.portlek.configs.jsonparser.JsonObject;
import io.github.portlek.configs.jsonparser.WriterConfig;
import io.github.portlek.configs.util.JsonHelper;
import io.github.portlek.configs.util.SerializationHelper;
import io.github.portlek.configs.yaml.FileConfiguration;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * A JSON Configuration for Bukkit based on {@link FileConfiguration}.
 * <p>
 * Able to store all the things you'd expect from a Bukkit configuration.
 */
public final class JsonConfiguration extends FileConfiguration {

    private static final String BLANK_CONFIG = "{}\n";

    private static final Logger LOG = Logger.getLogger(JsonConfiguration.class.getName());

    /**
     * Loads up a configuration from a json formatted file.
     * <p>
     * If the file does not exist, it will be created.  This will attempt to use UTF-8 encoding for the file, if it fails
     * to do so, the system default will be used instead.
     *
     * @param file The file to load the configuration from.
     * @return The configuration loaded from the file contents.
     */
    public static JsonConfiguration loadConfiguration(@NotNull final File file) {
        return JsonConfiguration.loadConfiguration(new JsonConfiguration(), file);
    }

    private static JsonConfiguration loadConfiguration(@NotNull final JsonConfiguration config, @NotNull final File file) {
        try {
            config.load(file);
        } catch (final FileNotFoundException ex) {
            JsonConfiguration.LOG.log(Level.SEVERE, "Cannot find file " + file, ex);
        } catch (final IOException | InvalidConfigurationException ex) {
            JsonConfiguration.LOG.log(Level.SEVERE, "Cannot load " + file, ex);
        }
        return config;
    }

    @NotNull
    @Override
    public String saveToString() {
        final JsonObject jsonObject = JsonHelper.mapAsJsonObject(this.getValues(false));
        final String dump = jsonObject.toString(WriterConfig.PRETTY_PRINT);

        if (dump.equals(JsonConfiguration.BLANK_CONFIG)) {
            return "";
        }

        return dump;
    }

    @Override
    public void loadFromString(@NotNull final String contents) {
        if (contents.isEmpty()) {
            return;
        }

        this.convertMapsToSections(
            JsonHelper.jsonObjectAsMap(
                Json.parse(contents)
            ),
            this
        );
    }

    @NotNull
    @Override
    public JsonConfigurationOptions options() {
        if (this.options == null) {
            this.options = new JsonConfigurationOptions(this);
        }

        return (JsonConfigurationOptions) this.options;
    }

    @NotNull
    @Override
    protected String buildHeader() {
        return "";
    }

    private void convertMapsToSections(@NotNull final Map<?, ?> input, @NotNull final ConfigurationSection section) {
        Map<?, ?> input1 = input;
        final Object result = SerializationHelper.deserialize(input1);

        if (result instanceof Map) {
            input1 = (Map<?, ?>) result;
            for (final Map.Entry<?, ?> entry : input1.entrySet()) {
                final String key = entry.getKey().toString();
                final Object value = entry.getValue();

                if (value instanceof Map) {
                    this.convertMapsToSections((Map<?, ?>) value, section.createSection(key));
                } else {
                    section.set(key, value);
                }
            }
        } else {
            section.set("", result);
        }
    }

}
