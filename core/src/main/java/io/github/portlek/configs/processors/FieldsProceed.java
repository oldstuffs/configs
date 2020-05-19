package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.structure.managed.FlManaged;
import io.github.portlek.configs.structure.managed.section.CfgSection;
import java.lang.reflect.Field;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class FieldsProceed implements Proceed {

    @NotNull
    private final FlManaged managed;

    @NotNull
    private final CfgSection parent;

    @Override
    public void load() {
        for (final Field field : this.parent.getClass().getDeclaredFields()) {
            final boolean accessible = field.isAccessible();
            field.setAccessible(true);
            final Optional<InstanceProceed> instanceOptional =
                Optional.ofNullable(field.getDeclaredAnnotation(Instance.class))
                    .map(instance -> new InstanceProceed(this.managed, this.parent, field));
            final Optional<PropertyProceed> propertyOptional = Optional.ofNullable(field.getDeclaredAnnotation(Property.class))
                .map(property -> new PropertyProceed(this.managed, this.parent, property, field));
            instanceOptional.ifPresent(InstanceProceed::load);
            propertyOptional.ifPresent(PropertyProceed::load);
            field.setAccessible(accessible);
        }
    }

}
