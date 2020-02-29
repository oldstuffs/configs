package io.github.portlek.configs.util.wrapper;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public final class LongWrapper implements Wrapper<Long> {

    private static final Long[] LONGS = new Long[0];

    @Nullable
    private final Object object;

    public LongWrapper(@Nullable final Object obj) {
        this.object = obj;
    }

    @Override
    public Long[] getArray() {
        final Long[] array;
        if (this.object instanceof List<?>) {
            //noinspection unchecked
            array = ((List<Long>) this.object).toArray(LongWrapper.LONGS);
        } else {
            array = LongWrapper.LONGS;
        }

        return array;
    }

    @Override
    public Long get() {
        final long get;
        if (this.object instanceof Number) {
            get = ((Number) this.object).longValue();
        } else if (this.object instanceof String) {
            get = Long.parseLong((String) this.object);
        } else if (this.object != null) {
            get = Long.parseLong(this.object.toString());
        } else {
            get = 0L;
        }
        return get;
    }

}