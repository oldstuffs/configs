package io.github.portlek.configs.util.wrapper;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public final class ShortWrapper implements Wrapper<Short> {

    private static final Short[] SHORTS = new Short[0];

    @Nullable
    private final Object object;

    public ShortWrapper(@Nullable final Object obj) {
        this.object = obj;
    }

    @Override
    public Short[] getArray() {
        final Short[] array;
        if (this.object instanceof List<?>) {
            //noinspection unchecked
            array = ((List<Short>) this.object).toArray(ShortWrapper.SHORTS);
        } else {
            array = ShortWrapper.SHORTS;
        }
        return array;
    }

    @Override
    public Short get() {
        final short get;
        if (this.object instanceof Number) {
            get = ((Number) this.object).shortValue();
        } else if (this.object instanceof String) {
            get = Short.parseShort((String) this.object);
        } else if (this.object != null) {
            get = Short.parseShort(this.object.toString());
        } else {
            get = (short) 0;
        }
        return get;
    }

}