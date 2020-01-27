package io.github.portlek.configs.processors;

import io.github.portlek.configs.Managed;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Value;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public final class FieldsProceed implements Proceed<Managed> {

    @NotNull
    private final Object object;

    @NotNull
    private final String parent;

    public FieldsProceed(@NotNull Object object, @NotNull String parent) {
        this.object = object;
        this.parent = parent;
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
                    parent
                ).load(field);
            } else if (value != null) {
                new ValueProceed(
                    managed,
                    object,
                    parent,
                    value
                ).load(field);
            }

            field.setAccessible(isAccessible);
        }
    }

}
