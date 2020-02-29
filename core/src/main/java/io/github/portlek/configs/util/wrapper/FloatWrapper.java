package io.github.portlek.configs.util.wrapper;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public final class FloatWrapper implements Wrapper<Float> {

    private static final Float[] FLOATS = new Float[0];

    @Nullable
    private final Object object;

    public FloatWrapper(@Nullable final Object obj) {
        this.object = obj;
    }

    @Override
    public Float[] getArray() {
        final Float[] array;
        if (this.object instanceof List<?>) {
            //noinspection unchecked
            array = ((List<Float>) this.object).toArray(FloatWrapper.FLOATS);
        } else {
            array = FloatWrapper.FLOATS;
        }

        return array;
    }

    @Override
    public Float get() {
        final float get;
        if (this.object instanceof Number) {
            get = ((Number) this.object).floatValue();
        } else if (this.object instanceof String) {
            get = Float.parseFloat((String) this.object);
        } else if (this.object != null) {
            get = Float.parseFloat(this.object.toString());
        } else {
            get = 1.0f;
        }
        return get;
    }

}