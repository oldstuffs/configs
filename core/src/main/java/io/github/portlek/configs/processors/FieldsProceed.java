package io.github.portlek.configs.processors;

import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Value;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import org.jetbrains.annotations.NotNull;

public final class FieldsProceed implements Proceed<Managed> {

    @NotNull
    private final Object object;

    @NotNull
    private final String parent;

    @NotNull
    private final BiFunction<Object, String, Optional<?>> get;

    @NotNull
    private final BiPredicate<Object, String> set;

    public FieldsProceed(@NotNull final Object object, @NotNull final String parent,
                         @NotNull final BiFunction<Object, String, Optional<?>> get,
                         @NotNull final BiPredicate<Object, String> set) {
        this.object = object;
        this.parent = parent;
        this.get = get;
        this.set = set;
    }

    @Override
    public void load(@NotNull final Managed managed) throws Exception {
        for (final Field field : this.object.getClass().getDeclaredFields()) {
            final boolean isAccessible = field.isAccessible();

            field.setAccessible(true);

            final Instance instance = field.getDeclaredAnnotation(Instance.class);
            final Value value = field.getDeclaredAnnotation(Value.class);

            if (instance != null) {
                new InstanceProceed(
                    managed,
                    this.object,
                    this.parent,
                    this.get,
                    this.set
                ).load(field);
            } else if (value != null) {
                new ValueProceed(
                    managed,
                    this.object,
                    this.parent,
                    value,
                    this.get,
                    this.set
                ).load(field);
            }

            field.setAccessible(isAccessible);
        }
    }

}
