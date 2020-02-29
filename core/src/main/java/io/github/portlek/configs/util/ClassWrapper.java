package io.github.portlek.configs.util;

import io.github.portlek.configs.util.wrapper.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ClassWrapper {

    private ClassWrapper() {
    }

    @NotNull
    public static <T> T getFromDef(@Nullable final Object obj, @NotNull final T def) {
        final Object object;
        if (def instanceof Integer) {
            object = new IntegerWrapper(obj).get();
        } else if (def instanceof Float) {
            object = new FloatWrapper(obj).get();
        } else if (def instanceof Double) {
            object = new DoubleWrapper(obj).get();
        } else if (def instanceof Long) {
            object = new LongWrapper(obj).get();
        } else if (def instanceof Boolean) {
            if (obj == null) {
                object = false;
            } else {
                object = "true".equalsIgnoreCase(obj.toString());
            }
        } else if (def instanceof String[]) {
            object = new StringWrapper(obj).getArray();
        } else if (def instanceof Long[] || def instanceof long[]) {
            object = new LongWrapper(obj).getArray();
        } else if (def instanceof Double[] || def instanceof double[]) {
            object = new DoubleWrapper(obj).getArray();
        } else if (def instanceof Float[] || def instanceof float[]) {
            object = new FloatWrapper(obj).getArray();
        } else if (def instanceof Short[] || def instanceof short[]) {
            object = new ShortWrapper(obj).getArray();
        } else if (def instanceof Byte[] || def instanceof byte[]) {
            object = new ByteWrapper(obj).getArray();
        } else {
            object = obj;
        }
        //noinspection unchecked
        return (T) object;
    }

}
