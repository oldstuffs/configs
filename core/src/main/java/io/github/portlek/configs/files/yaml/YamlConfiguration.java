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

import io.github.portlek.configs.configuration.Configuration;
import io.github.portlek.configs.configuration.ConfigurationSection;
import java.io.File;
import java.io.Reader;
import java.util.Map;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

/**
 * An implementation from {@link Configuration} which saves all files in Yaml.
 * Note that this implementation is not synchronized.
 */
public final class YamlConfiguration extends FileConfiguration {

    private static final String COMMENT_PREFIX = "# ";

    private static final String BLANK_CONFIG = "{}\n";

    private final DumperOptions yamlOptions = new DumperOptions();

    private final Representer yamlRepresenter = new YamlRepresenter();

    private final Yaml yaml = new Yaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions);

    /**
     * Creates a new {@link YamlConfiguration}, loading from the given file.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be
     * returned.
     * <p>
     * The encoding used may follow the system dependent default.
     *
     * @param file Input file
     * @return Resulting configuration
     * @throws IllegalArgumentException Thrown if file is null
     */
    @NotNull
    public static YamlConfiguration loadConfiguration(@NotNull final File file) {
        final YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        return config;
    }

    /**
     * Creates a new {@link YamlConfiguration}, loading from the given reader.
     * <p>
     * Any errors loading the Configuration will be logged and then ignored.
     * If the specified input is not a valid config, a blank config will be
     * returned.
     *
     * @param reader input
     * @return resulting configuration
     * @throws IllegalArgumentException Thrown if stream is null
     */
    @NotNull
    public static YamlConfiguration loadConfiguration(@NotNull final Reader reader) {
        final YamlConfiguration config = new YamlConfiguration();
        config.load(reader);
        return config;
    }

    @NotNull
    private static String parseHeader(@NotNull final String input) {
        final String[] lines = input.split("\r?\n", -1);
        final StringBuilder result = new StringBuilder();
        boolean readingHeader = true;
        boolean foundHeader = false;

        for (int i = 0; i < lines.length && readingHeader; i++) {
            final String line = lines[i];

            if (line.startsWith(YamlConfiguration.COMMENT_PREFIX)) {
                if (i > 0) {
                    result.append('\n');
                }

                if (line.length() > YamlConfiguration.COMMENT_PREFIX.length()) {
                    result.append(line.substring(YamlConfiguration.COMMENT_PREFIX.length()));
                }

                foundHeader = true;
            } else if (foundHeader && line.isEmpty()) {
                result.append('\n');
            } else if (foundHeader) {
                readingHeader = false;
            }
        }

        return result.toString();
    }

    @NotNull
    @Override
    public String saveToString() {
        this.yamlOptions.setIndent(this.options().indent());
        this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        final String header = this.buildHeader();
        String dump = this.yaml.dump(this.getValues(false));

        if (dump.equals(YamlConfiguration.BLANK_CONFIG)) {
            dump = "";
        }

        return header + dump;
    }

    @SneakyThrows
    @Override
    public void loadFromString(@NotNull final String contents) {
        final Map<?, ?> input;
        input = this.yaml.load(contents);
        final String header = YamlConfiguration.parseHeader(contents);
        if (!header.isEmpty()) {
            this.options().header(header);
        }
        if (input != null) {
            this.convertMapsToSections(input, this);
        }
    }

    @NotNull
    @Override
    public YamlConfigurationOptions options() {
        if (this.options == null) {
            this.options = new YamlConfigurationOptions(this);
        }

        return (YamlConfigurationOptions) this.options;
    }

    @NotNull
    @Override
    protected String buildHeader() {
        final String header = this.options().header();

        if (this.options().copyHeader()) {
            final Configuration def = this.getDefaults();

            if (def instanceof FileConfiguration) {
                final FileConfiguration filedefaults = (FileConfiguration) def;
                final String defaultsHeader = filedefaults.buildHeader();
                if (!defaultsHeader.isEmpty()) {
                    return defaultsHeader;
                }
            }
        }

        if (header.isEmpty()) {
            return "";
        }

        final StringBuilder builder = new StringBuilder();
        final String[] lines = header.split("\r?\n", -1);
        boolean startedHeader = false;

        for (int i = lines.length - 1; i >= 0; i--) {
            builder.insert(0, '\n');

            if (startedHeader || !lines[i].isEmpty()) {
                builder.insert(0, lines[i]);
                builder.insert(0, YamlConfiguration.COMMENT_PREFIX);
                startedHeader = true;
            }
        }

        return builder.toString();
    }

    private void convertMapsToSections(@NotNull final Map<?, ?> input, @NotNull final ConfigurationSection section) {
        for (final Map.Entry<?, ?> entry : input.entrySet()) {
            final String key = entry.getKey().toString();
            final Object value = entry.getValue();

            if (value instanceof Map) {
                this.convertMapsToSections((Map<?, ?>) value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }

}
