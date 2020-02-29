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

    public MapEntry(@NotNull final X key, @NotNull final Y value) {
        this.key = key;
        this.value = value;
    }

    @NotNull
    public static <X, Y> Map.Entry<X, Y> from(@NotNull final X key, @NotNull final Y value) {
        return new MapEntry<>(key, value);
    }

    @NotNull
    @Override
    public X getKey() {
        return this.key;
    }

    @NotNull
    @Override
    public Y getValue() {
        return this.value;
    }

    @Override
    public Y setValue(final Y value) {
        throw new UnsupportedOperationException("You can't set MapEntry because of that object is immutable!");
    }

    @Override
    public int hashCode() {
        int hash;
        hash = this.key.hashCode();
        hash ^= this.value.hashCode();
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Map.Entry
            && ((Map.Entry<?, ?>) obj).getKey().equals(this.key)
            && ((Map.Entry<?, ?>) obj).getValue().equals(this.value);
    }

    @Override
    public String toString() {
        return this.key + "=" + this.value;
    }

}
