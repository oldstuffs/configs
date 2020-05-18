package io.github.portlek.configs;

import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.provided.FeatureProvider;
import io.github.portlek.configs.util.Feature;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigSection implements CfgSection {

    @Nullable
    private ConfigurationSection section;

    @Nullable
    private FlManaged managed;

    @NotNull
    @Override
    public final CfgSection getBase() {
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

    @NotNull
    @Override
    public <X, Y> Feature<X, Y> feature(@NotNull final String path, @NotNull final Class<X> keyClass,
                                        @NotNull final Class<Y> valueClass) {
        final Feature<X, Y> feature = new Feature<>(this::getManaged, this, keyClass, valueClass);
        this.getManaged().addCustomValue(valueClass, new FeatureProvider<>(feature));
        return feature;
    }

    @Override
    public final void setup(@NotNull final FlManaged managed, @NotNull final ConfigurationSection configurationSection) {
        this.section = configurationSection;
        this.managed = managed;
    }

}
