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

    public MapEntry(@NotNull final X xkey, @NotNull final Y yvalue) {
        this.key = xkey;
        this.value = yvalue;
    }

    @NotNull
    public static <X, Y> Map.Entry<X, Y> from(@NotNull final X xkey, @NotNull final Y yvalue) {
        return new MapEntry<>(xkey, yvalue);
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
    public Y setValue(@NotNull final Y yvalue) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " is an immutable class, you can't edit it!");
    }

    @Override
    public int hashCode() {
        return this.key.hashCode() ^ this.value.hashCode();
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
