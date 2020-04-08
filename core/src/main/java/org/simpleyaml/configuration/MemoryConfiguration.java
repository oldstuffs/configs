package org.simpleyaml.configuration;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.utils.Validate;

/**
 * This is a {@link Configuration} implementation that does not save or load
 * from any source, and stores all values in memory only.
 * This is useful for temporary Configurations for providing defaults.
 */
public class MemoryConfiguration extends MemorySection implements Configuration {

    protected Configuration defaults;

    protected MemoryConfigurationOptions options;

    /**
     * Creates an empty {@link MemoryConfiguration} with no default values.
     */
    public MemoryConfiguration() {
    }

    /**
     * Creates an empty {@link MemoryConfiguration} using the specified {@link
     * Configuration} as a source for all default values.
     *
     * @param defaults Default value provider
     * @throws IllegalArgumentException Thrown if defaults is null
     */
    public MemoryConfiguration(@Nullable final Configuration defaults) {
        this.defaults = defaults;
    }

    @Nullable
    @Override
    public ConfigurationSection getParent() {
        return null;
    }

    @Override
    public void addDefault(@NotNull final String path, @Nullable final Object value) {
        Validate.notNull(path, "Path may not be null");
        if (this.defaults == null) {
            this.defaults = new MemoryConfiguration();
        }
        this.defaults.set(path, value);
    }

    @Override
    public void addDefaults(@NotNull final Map<String, Object> defaults) {
        Validate.notNull(defaults, "Defaults may not be null");

        for (final Map.Entry<String, Object> entry : defaults.entrySet()) {
            this.addDefault(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void addDefaults(@NotNull final Configuration defaults) {
        Validate.notNull(defaults, "Defaults may not be null");

        this.addDefaults(defaults.getValues(true));
    }

    @Override
    @Nullable
    public Configuration getDefaults() {
        return this.defaults;
    }

    @Override
    public void setDefaults(@NotNull final Configuration defaults) {
        Validate.notNull(defaults, "Defaults may not be null");

        this.defaults = defaults;
    }

    @Override
    @NotNull
    public MemoryConfigurationOptions options() {
        if (this.options == null) {
            this.options = new MemoryConfigurationOptions(this);
        }

        return this.options;
    }

}
