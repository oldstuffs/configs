package io.github.portlek.configs.util.wrapper;

import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StringWrapper implements Wrapper<String> {

    private static final String[] STRINGS = new String[0];

    @Nullable
    private final Object object;

    public StringWrapper(@Nullable final Object obj) {
        this.object = obj;
    }

    @NotNull
    @Override
    public String[] getArray() {
        final String[] strng;
        if (this.object instanceof List) {
            //noinspection unchecked
            final List<String> list = (List<String>) this.object;
            strng = list.toArray(StringWrapper.STRINGS);
        } else {
            strng = StringWrapper.STRINGS;
        }
        return strng;
    }

    @NotNull
    @Override
    public String get() {
        final String strng;
        if (this.object instanceof List<?> && ((Collection<?>) this.object).size() == 1) {
            strng = ((List<?>) this.object).get(0).toString();
        } else if (this.object != null) {
            strng = this.object.toString();
        } else {
            strng = "";
        }
        return strng;
    }

}
