package io.github.portlek.configs.util;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public final class MapEntry<X, Y> implements Map.Entry<X, Y> {

    @NotNull
    private final X key;

    @NotNull
    private final Y value;

    @NotNull
    public static <X, Y> Map.Entry<X, Y> from(@NotNull final X xkey, @NotNull final Y yvalue) {
        return new MapEntry<>(xkey, yvalue);
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
