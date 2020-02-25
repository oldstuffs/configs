package io.github.portlek.configs;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;

public interface ConfigSection {

    @NotNull
    Optional<Object> get(@NotNull String path);

    @NotNull
    Optional<Object> get(@NotNull String path, @Nullable Object def);

    @NotNull
    <T> T getOrSet(@NotNull String pat, @NotNull T fallback);

    void set(@NotNull String path, @Nullable Object object);

    @NotNull
    Optional<ConfigurationSection> getSection(@NotNull String path);

    @NotNull
    Optional<ConfigurationSection> getOrCreateSection(@NotNull String path);

    void createSection(@NotNull String path);

    @NotNull
    Optional<String> getString(@NotNull String path);

    @NotNull
    Optional<String> getString(@NotNull String path, @Nullable String def);

    int getInt(@NotNull String path);

    int getInt(@NotNull String path, int def);

    boolean getBoolean(@NotNull String path);

    boolean getBoolean(@NotNull String path, boolean def);

    double getDouble(@NotNull String path);

    double getDouble(@NotNull String path, double def);

    long getLong(@NotNull String path);

    long getLong(@NotNull String path, long def);

    @NotNull
    List<String> getStringList(@NotNull String path);

    @NotNull
    List<Integer> getIntegerList(@NotNull String path);

    @NotNull
    List<Boolean> getBooleanList(@NotNull String path);

    @NotNull
    List<Double> getDoubleList(@NotNull String path);

    @NotNull
    List<Float> getFloatList(@NotNull String path);

    @NotNull
    List<Long> getLongList(@NotNull String path);

    @NotNull
    List<Byte> getByteList(@NotNull String path);

    @NotNull
    List<Character> getCharacterList(@NotNull String path);

    @NotNull
    List<Short> getShortList(@NotNull String path);

    @NotNull
    Optional<List<?>> getList(@NotNull String path);

}
