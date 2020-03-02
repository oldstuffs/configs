package io.github.portlek.configs.util;

import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * Immutable Entry
 *
 * @param <X> the key
 * @param <Y> the value
 */
public final class MapEntry<X, Y> implements Map.Entry<X, Y> {

    @NotNull
    private final X key;

    @NotNull
    private final Y value;

    public MapEntry(@NotNull X key, @NotNull Y value) {
        this.key = key;
        this.value = value;
    }

    @NotNull
    public static <X, Y> Map.Entry<X, Y> of(@NotNull X key, @NotNull Y value) {
        return new MapEntry<>(key, value);
    }

    @NotNull
    @Override
    public X getKey() {
        return key;
    }

    @NotNull
    @Override
    public Y getValue() {
        return value;
    }

    @Override
    public Y setValue(Y value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        int hash;
        hash = key.hashCode();
        hash ^= value.hashCode();
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Map.Entry
            && ((Map.Entry<?, ?>) obj).getKey().equals(key)
            && ((Map.Entry<?, ?>) obj).getValue().equals(value);
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

}
