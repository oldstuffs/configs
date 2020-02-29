package io.github.portlek.configs.configuration;

import io.github.portlek.configs.exception.ConfigsValidationException;
import io.github.portlek.configs.util.ClassWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigurationSection {

    @NotNull
    Set<String> singleLayerKeySet();

    @NotNull
    Set<String> singleLayerKeySet(@NotNull String key);

    @NotNull
    Set<String> keySet();

    @NotNull
    Set<String> keySet(@NotNull String key);

    @NotNull
    void remove(@NotNull String key);

    default <T> T get(@NotNull final String key, @NotNull final T def) {
        final T obj;
        if (this.contains(key)) {
            obj = ClassWrapper.getFromDef(this.get(key), def);
        } else {
            obj = def;
        }
        return obj;
    }

    boolean contains(@NotNull String key);

    @Nullable
    Object get(@NotNull String key);

    @NotNull
    default String getString(@NotNull final String key) {
        return this.getOrDefault(key, "");
    }

    @NotNull
    default <T> T getOrDefault(@NotNull final String key, @NotNull final T def) {
        final T obj;
        if (this.contains(key)) {
            obj = ClassWrapper.getFromDef(this.get(key), def);
        } else {
            obj = def;
        }
        return obj;
    }

    default long getLong(@NotNull final String key) {
        return this.getOrDefault(key, 0L);
    }

    default void setDefault(@NotNull final String key, @NotNull final Object value) {
        if (!this.contains(key)) {
            this.set(key, value);
        }
    }

    void set(@NotNull String key, @NotNull Object value);

    default boolean getBoolean(@NotNull final String key) {
        return this.getOrDefault(key, false);
    }

    default float getFloat(@NotNull final String key) {
        return this.getOrDefault(key, 0F);
    }

    default double getDouble(@NotNull final String key) {
        return this.getOrDefault(key, 0D);
    }

    default byte getByte(@NotNull final String key) {
        return this.getOrDefault(key, (byte) 0);
    }

    default int getInt(@NotNull final String key) {
        return this.getOrDefault(key, 0);
    }

    default <T> T getOrSetDefault(@NotNull final String key, @NotNull final T def) {
        final T obj;
        if (this.contains(key)) {
            obj = ClassWrapper.getFromDef(this.get(key), def);
        } else {
            this.set(key, def);
            obj = def;
        }
        return obj;
    }

    @NotNull
    default List<?> getList(@NotNull final String key) {
        return this.getOrDefault(key, new ArrayList<>());
    }

    @NotNull
    default List<String> getStringList(@NotNull final String key) {
        return this.getOrDefault(key, new ArrayList<>());
    }

    @NotNull
    default List<Integer> getIntegerList(@NotNull final String key) {
        return this.getOrDefault(key, new ArrayList<>());
    }

    @NotNull
    default List<Byte> getByteList(@NotNull final String key) {
        return this.getOrDefault(key, new ArrayList<>());
    }

    @NotNull
    default List<Long> getLongList(@NotNull final String key) {
        return this.getOrDefault(key, new ArrayList<>());
    }

    @Nullable
    default Map<?, ?> getMap(@NotNull final String key) {
        return (Map<?, ?>) this.get(key);
    }

    @NotNull
    default <E extends Enum<E>> E getEnum(@NotNull final String key, @NotNull final Class<E> type) {
        final Object object = this.get(key);
        if (object instanceof String) {
            return Enum.valueOf(type, (String) object);
        }
        throw new ConfigsValidationException("No usable Enum-Value found for '" + key + "'.");
    }

}
