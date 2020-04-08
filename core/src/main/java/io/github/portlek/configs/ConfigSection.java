package io.github.portlek.configs;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;

public class ConfigSection implements CfgSection {

    @Nullable
    private ConfigurationSection section;

    @Nullable
    private FlManaged managed;

    @NotNull
    @Override
    public CfgSection getBase() {
        return this;
    }

    @NotNull
    @Override
    public ConfigurationSection getConfigurationSection() {
        return Objects.requireNonNull(this.section, "You have to load your class with '#load()' method");
    }

    @Override
    @NotNull
    public final FlManaged getManaged() {
        return Objects.requireNonNull(this.managed, "You have to load your class with '#load()' method");
    }

    @Override
    public final void setup(@NotNull final FlManaged managed, @NotNull final ConfigurationSection configurationSection) {
        this.section = configurationSection;
        this.managed = managed;
    }

}
