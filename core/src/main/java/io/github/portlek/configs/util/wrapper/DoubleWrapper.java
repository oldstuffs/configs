package io.github.portlek.configs.util.wrapper;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public final class DoubleWrapper implements Wrapper<Double> {

    private static final Double[] DOUBLES = new Double[0];

    @Nullable
    private final Object object;

    public DoubleWrapper(@Nullable final Object obj) {
        this.object = obj;
    }

    @Override
    public Double[] getArray() {
        final Double[] array;
        if (this.object instanceof List<?>) {
            //noinspection unchecked
            array = ((List<Double>) this.object).toArray(DoubleWrapper.DOUBLES);
        } else {
            array = DoubleWrapper.DOUBLES;
        }

        return array;
    }

    @Override
    public Double get() {
        final double get;
        if (this.object instanceof Number) {
            get = ((Number) this.object).doubleValue();
        } else if (this.object instanceof String) {
            get = Double.parseDouble((String) this.object);
        } else if (this.object != null) {
            get = Double.parseDouble(this.object.toString());
        } else {
            get = 1.0d;
        }
        return get;
    }

}