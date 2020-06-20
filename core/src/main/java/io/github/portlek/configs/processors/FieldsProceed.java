package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.structure.section.CfgSection;
import io.github.portlek.reflection.clazz.ClassOf;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class FieldsProceed {

    @NotNull
    private final CfgSection parent;

    @SneakyThrows
    public void load() {
        final ClassOf<CfgSection> parentClass = new ClassOf<>(this.parent);
        parentClass
            .declaredFieldsWithAnnotation(Property.class, (refField, property) -> {
                new PropertyProceed(this.parent, property, refField).load();
            });
        parentClass
            .declaredFieldsWithAnnotation(Instance.class, (refField, instance) -> {
                if (CfgSection.class.isAssignableFrom(refField.type())) {
                    refField.of(this.parent).get()
                        .map(o -> (CfgSection) o)
                        .ifPresent(initiatedCfgSection -> {
                            new ClassOf<>(initiatedCfgSection).annotation(Section.class, section -> {
                                initiatedCfgSection.setup(this.parent.getManaged(),
                                    this.parent.getOrCreateSection(section.value()).getConfigurationSection());
                                new FieldsProceed(initiatedCfgSection).load();
                            });
                        });
                }
            });
    }

}
