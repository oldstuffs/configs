package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.structure.section.CfgSection;
import java.lang.reflect.Field;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class FieldsProceed {

    @NotNull
    private final CfgSection parent;

    @SneakyThrows
    public void load() {
        for (final Field field : this.parent.getClass().getDeclaredFields()) {
            final boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Optional.ofNullable(field.getDeclaredAnnotation(Instance.class))
                .filter(instance -> CfgSection.class.isAssignableFrom(field.getType()))
                .map(instance -> (CfgSection) field.get(this.parent))
                .ifPresent(initiatedCfgSection ->
                    Optional.ofNullable(initiatedCfgSection.getClass().getDeclaredAnnotation(Section.class))
                        .ifPresent(section -> {
                            initiatedCfgSection.setup(this.parent.getManaged(),
                                this.parent.getOrCreateSection(section.value()).getConfigurationSection());
                            new FieldsProceed(initiatedCfgSection).load();
                        }));
            Optional.ofNullable(field.getDeclaredAnnotation(Property.class))
                .map(property -> new PropertyProceed(this.parent, property, field))
                .ifPresent(PropertyProceed::load);
            field.setAccessible(accessible);
        }
    }

}
