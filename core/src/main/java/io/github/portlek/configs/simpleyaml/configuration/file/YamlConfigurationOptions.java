package io.github.portlek.configs.simpleyaml.configuration.file;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import io.github.portlek.configs.simpleyaml.utils.Validate;

/**
 * Various settings for controlling the input and output of a {@link
 * YamlConfiguration}
 */
public class YamlConfigurationOptions extends FileConfigurationOptions {

    private int indent = 2;

    protected YamlConfigurationOptions(@NotNull final YamlConfiguration configuration) {
        super(configuration);
    }

    @NotNull
    @Override
    public YamlConfiguration configuration() {
        return (YamlConfiguration) super.configuration();
    }

    @NotNull
    @Override
    public YamlConfigurationOptions pathSeparator(final char value) {
        super.pathSeparator(value);
        return this;
    }

    @NotNull
    @Override
    public YamlConfigurationOptions copyDefaults(final boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @NotNull
    @Override
    public YamlConfigurationOptions header(@Nullable final String value) {
        super.header(value);
        return this;
    }

    @NotNull
    @Override
    public YamlConfigurationOptions copyHeader(final boolean value) {
        super.copyHeader(value);
        return this;
    }

    /**
     * Gets how much spaces should be used to indent each line.
     * <p>
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @return How much to indent by
     */
    public int indent() {
        return this.indent;
    }

    /**
     * Sets how much spaces should be used to indent each line.
     * <p>
     * The minimum value this may be is 2, and the maximum is 9.
     *
     * @param value New indent
     * @return This object, for chaining
     */
    @NotNull
    public YamlConfigurationOptions indent(final int value) {
        Validate.isTrue(value >= 2, "Indent must be at least 2 characters");
        Validate.isTrue(value <= 9, "Indent cannot be greater than 9 characters");

        this.indent = value;
        return this;
    }

}
