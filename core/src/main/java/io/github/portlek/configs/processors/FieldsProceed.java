package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.structure.managed.FlManaged;
import io.github.portlek.configs.structure.section.CfgSection;
import java.lang.reflect.Field;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class FieldsProceed {

    @NotNull
    private final CfgSection parent;

    @NotNull
    private final FlManaged managed;

    public void load() {
        for (final Field field : this.parent.getClass().getDeclaredFields()) {
            final boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Optional.ofNullable(field.getDeclaredAnnotation(Instance.class))
                .map(instance -> new InstanceProceed(this.managed, this.parent, field))
                .ifPresent(InstanceProceed::load);
            Optional.ofNullable(field.getDeclaredAnnotation(Property.class))
                .map(property -> new PropertyProceed(this.parent, property, field))
                .ifPresent(PropertyProceed::load);
            field.setAccessible(accessible);
        }
    }

}
