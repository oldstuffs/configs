package io.github.portlek.configs.processors;

import io.github.portlek.configs.Managed;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Value;
import java.lang.reflect.Field;
import org.jetbrains.annotations.NotNull;

public final class FieldsProceed implements Proceed<Managed> {

    @NotNull
    private final Object object;

    @NotNull
    private final String parent;

    public FieldsProceed(@NotNull final Object object, @NotNull final String parent) {
        this.object = object;
        this.parent = parent;
    }

    @Override
    public void load(@NotNull final Managed managed) {
        for (final Field field : this.object.getClass().getDeclaredFields()) {
            final boolean isAccessible = field.isAccessible();

            field.setAccessible(true);

            final Instance instance = field.getDeclaredAnnotation(Instance.class);
            final Value value = field.getDeclaredAnnotation(Value.class);

            if (instance != null) {
                new InstanceProceed(
                    managed,
                    this.object,
                    this.parent
                ).load(field);
            } else if (value != null) {
                new ValueProceed(
                    managed,
                    this.object,
                    this.parent,
                    value
                ).load(field);
            }

            field.setAccessible(isAccessible);
        }
    }

}
