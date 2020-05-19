package io.github.portlek.configs.processors;

import io.github.portlek.configs.structure.managed.section.CfgSection;
import io.github.portlek.configs.structure.managed.FlManaged;
import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import java.lang.reflect.Field;
import java.util.Optional;
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
            final Optional<InstanceProceed> instanceOptional =
                Optional.ofNullable(field.getDeclaredAnnotation(Instance.class))
                    .map(instance -> new InstanceProceed(managed, this.parent));
            final Optional<PropertyProceed> propertyOptional = Optional.ofNullable(field.getDeclaredAnnotation(Property.class))
                .map(property -> new PropertyProceed(managed, this.parent, property));
            instanceOptional.ifPresent(fieldProceed ->
                fieldProceed.load(field));
            propertyOptional.ifPresent(propertyProceed ->
                propertyProceed.load(field));
            field.setAccessible(accessible);
        }
    }

}
