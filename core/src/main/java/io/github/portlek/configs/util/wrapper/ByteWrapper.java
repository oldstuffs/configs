package io.github.portlek.configs.util.wrapper;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public final class ByteWrapper implements Wrapper<Byte> {

    private static final Byte[] BYTES = new Byte[0];

    @Nullable
    private final Object object;

    public ByteWrapper(@Nullable final Object obj) {
        this.object = obj;
    }

    @Override
    public Byte[] getArray() {
        final Byte[] array;
        if (this.object instanceof List<?>) {
            //noinspection unchecked
            array = ((List<Byte>) this.object).toArray(ByteWrapper.BYTES);
        } else {
            array = ByteWrapper.BYTES;
        }
        return array;
    }

    @Override
    public Byte get() {
        final byte get;
        if (this.object instanceof Number) {
            get = ((Number) this.object).byteValue();
        } else if (this.object instanceof String) {
            get = Byte.parseByte((String) this.object);
        } else if (this.object != null) {
            get = Byte.parseByte(this.object.toString());
        } else {
            get = (byte) 1;
        }
        return get;
    }

}