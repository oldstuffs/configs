package io.github.portlek.configs.util.wrapper;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public final class IntegerWrapper implements Wrapper<Integer> {

    private static final Integer[] INTEGERS = new Integer[0];

    @Nullable
    private final Object object;

    public IntegerWrapper(@Nullable final Object obj) {
        this.object = obj;
    }

    @Override
    public Integer[] getArray() {
        final Integer[] array;
        if (this.object instanceof List<?>) {
            //noinspection unchecked
            array = ((List<Integer>) this.object).toArray(IntegerWrapper.INTEGERS);
        } else {
            array = IntegerWrapper.INTEGERS;
        }

        return array;
    }

    @Override
    public Integer get() {
        final int get;
        if (this.object instanceof Number) {
            get = ((Number) this.object).intValue();
        } else if (this.object instanceof String) {
            get = Integer.parseInt((String) this.object);
        } else if (this.object != null) {
            get = Integer.parseInt(this.object.toString());
        } else {
            get = 0;
        }
        return get;
    }

}