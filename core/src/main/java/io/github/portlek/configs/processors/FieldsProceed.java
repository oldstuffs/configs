package io.github.portlek.configs.processors;

import io.github.portlek.configs.Managed;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Value;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public final class FieldsProceed implements Proceed<Managed> {

    @NotNull
    private final Object object;

    @NotNull
    private final String parent;

    @NotNull
    private final BiFunction<Object, String, Optional<?>> get;

    @NotNull
    private final BiPredicate<Object, String> set;

    public FieldsProceed(@NotNull Object object, @NotNull String parent,
                         @NotNull BiFunction<Object, String, Optional<?>> get,
                         @NotNull BiPredicate<Object, String> set) {
        this.object = object;
        this.parent = parent;
        this.get = get;
        this.set = set;
    }

    @Override
    public void load(@NotNull Managed managed) throws Exception {
        for (Field field : object.getClass().getDeclaredFields()) {
            final boolean isAccessible = field.isAccessible();

            field.setAccessible(true);

            final Instance instance = field.getDeclaredAnnotation(Instance.class);
            final Value value = field.getDeclaredAnnotation(Value.class);

            if (instance != null) {
                new InstanceProceed(
                    managed,
                    object,
                    parent,
                    get,
                    set
                ).load(field);
            } else if (value != null) {
                new ValueProceed(
                    managed,
                    object,
                    parent,
                    value,
                    get,
                    set
                ).load(field);
            }

            field.setAccessible(isAccessible);
        }
    }

}
