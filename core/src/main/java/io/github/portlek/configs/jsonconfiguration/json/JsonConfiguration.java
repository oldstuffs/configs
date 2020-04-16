/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package io.github.portlek.configs.jsonconfiguration.json;

import io.github.portlek.configs.jsonconfiguration.util.JsonHelper;
import io.github.portlek.configs.jsonconfiguration.util.SerializationHelper;
import io.github.portlek.configs.json.Json;
import io.github.portlek.configs.json.JsonObject;
import io.github.portlek.configs.json.WriterConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import io.github.portlek.configs.simpleyaml.configuration.ConfigurationSection;
import io.github.portlek.configs.simpleyaml.configuration.InvalidConfigurationException;
import io.github.portlek.configs.simpleyaml.configuration.file.FileConfiguration;

/**
 * A JSON Configuration for Bukkit based on {@link FileConfiguration}.
 * <p>
 * Able to store all the things you'd expect from a Bukkit configuration.
 */
public class JsonConfiguration extends FileConfiguration {

    protected static final String BLANK_CONFIG = "{}\n";

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

    public static JsonConfiguration loadConfiguration(@NotNull final Reader reader) {
        return JsonConfiguration.loadConfiguration(new JsonConfiguration(), reader);
    }

    private static JsonConfiguration loadConfiguration(@NotNull final JsonConfiguration config, @NotNull final Reader reader) {
        try {
            config.load(reader);
        } catch (final FileNotFoundException ex) {
            JsonConfiguration.LOG.log(Level.SEVERE, "Cannot find file " + reader, ex);
        } catch (final IOException | InvalidConfigurationException ex) {
            JsonConfiguration.LOG.log(Level.SEVERE, "Cannot load " + reader, ex);
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

    @Override
    public @NotNull JsonConfigurationOptions options() {
        if (this.options == null) {
            this.options = new JsonConfigurationOptions(this);
        }

        return (JsonConfigurationOptions) this.options;
    }

    @Override
    protected @NotNull String buildHeader() {
        return "";
    }

    private void convertMapsToSections(@NotNull Map<?, ?> input, @NotNull final ConfigurationSection section) {
        final Object result = SerializationHelper.deserialize(input);

        if (result instanceof Map) {
            input = (Map<?, ?>) result;
            for (final Map.Entry<?, ?> entry : input.entrySet()) {
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
