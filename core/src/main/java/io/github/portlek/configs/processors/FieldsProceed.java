package io.github.portlek.configs.processors;

import io.github.portlek.configs.Child;
import io.github.portlek.configs.Managed;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.annotations.Value;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public final class FieldsProceed implements Proceed<Managed> {

    @NotNull
    private final Object instance;

    @NotNull
    private final String parent;

    public FieldsProceed(@NotNull Object instance, @NotNull String parent) {
        this.instance = instance;
        this.parent = parent;
    }

    @Override
    public void load(@NotNull Managed managed) throws Exception {
        for (Field field : instance.getClass().getDeclaredFields()) {
            final boolean isAccessible = field.isAccessible();

            field.setAccessible(true);

            final Value value = field.getDeclaredAnnotation(Value.class);
            final Section section = field.getDeclaredAnnotation(Section.class);
            final boolean deprecated = field.getDeclaredAnnotation(Deprecated.class) != null;

            if (!deprecated) {
                if (section != null && field.getType().equals(Child.class)) {
                    new SectionProceed(
                        managed,
                        instance,
                        parent,
                        section
                    ).load(field);
                } else if (value != null) {
                    new ValueProceed(
                        managed,
                        instance,
                        parent,
                        value
                    ).load(field);
                }
            }

            field.setAccessible(isAccessible);
        }
    }

}
