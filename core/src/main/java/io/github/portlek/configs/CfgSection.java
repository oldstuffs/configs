package io.github.portlek.configs;

import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.util.Feature;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CfgSection {

    @NotNull
    CfgSection getBase();

    @NotNull
    default String getName() {
        return this.getConfigurationSection().getName();
    }

    @NotNull
    ConfigurationSection getConfigurationSection();

    @NotNull
    default Set<String> getKeys(final boolean deep) {
        return this.getConfigurationSection().getKeys(deep);
    }

    @NotNull
    FlManaged getManaged();

    @NotNull <X, Y> Feature<X, Y> feature(@NotNull String path, @NotNull Class<X> keyClass,
                                          @NotNull Class<Y> valueClass);

    @NotNull
    default Optional<Object> get(@NotNull final String path, @Nullable final Object def) {
        return Optional.ofNullable(this.getConfigurationSection().get(path, def));
    }

    @NotNull
    default <T> T getOrSet(@NotNull final String path, @NotNull final T fallback) {
        return ((Optional<T>) this.get(path)).orElseGet(() -> {
            this.set(path, fallback);
            return fallback;
        });
    }

    @NotNull
    default Optional<Object> get(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().get(path));
    }

    default void set(@NotNull final String path, @Nullable final Object object) {
        this.getConfigurationSection().set(path, object);
        this.getManaged().autoSave();
    }

    @NotNull
    default CfgSection getOrCreateSection(@NotNull final String path) {
        return this.getSection(path).orElseGet(() -> this.createSection(path));
    }

    @NotNull
    default Optional<CfgSection> getSection(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getConfigurationSection(path))
            .map(configurationsection -> {
                final CfgSection configsection = this.getNewSection().get();
                configsection.setup(this.getManaged(), configurationsection);
                return configsection;
            });
    }

    @NotNull
    default CfgSection createSection(@NotNull final String path) {
        final CfgSection configsection = this.getNewSection().get();
        configsection.setup(this.getManaged(), this.getConfigurationSection().createSection(path));
        this.getManaged().autoSave();
        return configsection;
    }

    @NotNull
    default Supplier<CfgSection> getNewSection() {
        return ConfigSection::new;
    }

    void setup(@NotNull FlManaged managed, @NotNull ConfigurationSection configurationSection);

    @NotNull
    default <T> Optional<List<T>> getGenericList(@NotNull final String path,
                                                 @NotNull final Function<String, List<T>> function) {
        if (!this.contains(path) || !this.getList(path).isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(function.apply(path));
    }

    @NotNull
    default <T> Optional<T> getGenericObject(@NotNull final String path,
                                             @NotNull final Function<String, T> function) {
        if (this.contains(path)) {
            return Optional.ofNullable(function.apply(path));
        }
        return Optional.empty();
    }

    @NotNull
    default Optional<String> getString(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getString(path));
    }

    @NotNull
    default Optional<String> getString(@NotNull final String path, @Nullable final String def) {
        return Optional.ofNullable(this.getConfigurationSection().getString(path, def));
    }

    default Optional<Integer> getInt(@NotNull final String path) {
        return this.getGenericObject(path, this.getConfigurationSection()::getInt);
    }

    default int getInt(@NotNull final String path, final int def) {
        return this.getConfigurationSection().getInt(path, def);
    }

    default Optional<Boolean> getBoolean(@NotNull final String path) {
        return this.getGenericObject(path, this.getConfigurationSection()::getBoolean);
    }

    default boolean getBoolean(@NotNull final String path, final boolean def) {
        return this.getConfigurationSection().getBoolean(path, def);
    }

    default Optional<Double> getDouble(@NotNull final String path) {
        return this.getGenericObject(path, this.getConfigurationSection()::getDouble);
    }

    default double getDouble(@NotNull final String path, final double def) {
        return this.getConfigurationSection().getDouble(path, def);
    }

    default Optional<Long> getLong(@NotNull final String path) {
        return this.getGenericObject(path, this.getConfigurationSection()::getLong);
    }

    default long getLong(@NotNull final String path, final long def) {
        return this.getConfigurationSection().getLong(path, def);
    }

    @NotNull
    default Optional<List<String>> getStringList(@NotNull final String path) {
        return this.getGenericList(path, this.getConfigurationSection()::getStringList);
    }

    @NotNull
    default Optional<List<Integer>> getIntegerList(@NotNull final String path) {
        return this.getGenericList(path, this.getConfigurationSection()::getIntegerList);
    }

    @NotNull
    default Optional<List<Boolean>> getBooleanList(@NotNull final String path) {
        return this.getGenericList(path, this.getConfigurationSection()::getBooleanList);
    }

    @NotNull
    default Optional<List<Double>> getDoubleList(@NotNull final String path) {
        return this.getGenericList(path, this.getConfigurationSection()::getDoubleList);
    }

    @NotNull
    default Optional<List<Float>> getFloatList(@NotNull final String path) {
        return this.getGenericList(path, this.getConfigurationSection()::getFloatList);
    }

    @NotNull
    default Optional<List<Long>> getLongList(@NotNull final String path) {
        return this.getGenericList(path, this.getConfigurationSection()::getLongList);
    }

    @NotNull
    default Optional<List<Byte>> getByteList(@NotNull final String path) {
        return this.getGenericList(path, this.getConfigurationSection()::getByteList);
    }

    @NotNull
    default Optional<List<Character>> getCharacterList(@NotNull final String path) {
        return this.getGenericList(path, this.getConfigurationSection()::getCharacterList);
    }

    @NotNull
    default Optional<List<Short>> getShortList(@NotNull final String path) {
        return this.getGenericList(path, this.getConfigurationSection()::getShortList);
    }

    @NotNull
    default Optional<List<?>> getList(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getList(path));
    }

}
