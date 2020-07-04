package io.github.portlek.configs.configuration.file;

import io.github.portlek.configs.configuration.MemoryConfiguration;
import io.github.portlek.configs.configuration.MemoryConfigurationOptions;
import org.jetbrains.annotations.NotNull;

public class FileConfigurationOptions extends MemoryConfigurationOptions {

    protected FileConfigurationOptions(@NotNull final MemoryConfiguration configuration) {
        super(configuration);
    }

    @Override
    public FileConfiguration configuration() {
        return (FileConfiguration) super.configuration();
    }

    @Override
    public FileConfigurationOptions pathSeparator(final char value) {
        super.pathSeparator(value);
        return this;
    }

    @Override
    public FileConfigurationOptions copyDefaults(final boolean value) {
        super.copyDefaults(value);
        return this;
    }

}
