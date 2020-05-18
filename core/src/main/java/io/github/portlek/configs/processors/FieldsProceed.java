package io.github.portlek.configs.processors;

import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.FlManaged;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class FieldsProceed implements Proceed<FlManaged> {

    @NotNull
    private final CfgSection parent;

    @Override
    public void load(@NotNull final FlManaged managed) {
        for (final Field field : this.parent.getClass().getDeclaredFields()) {
            final boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Stream.of(
                Optional.ofNullable(field.getDeclaredAnnotation(Instance.class))
                    .map(instance -> (Proceed<Field>) new InstanceProceed(managed, this.parent)),
                Optional.ofNullable(field.getDeclaredAnnotation(Property.class))
                    .map(property -> (Proceed<Field>) new ValueProceed(managed, this.parent, property))
            )
                .filter(Optional::isPresent)
                .findFirst()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .ifPresent(fieldProceed -> fieldProceed.load(field));
            field.setAccessible(accessible);
        }
    }

}
