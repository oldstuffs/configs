package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.structure.section.CfgSection;
import io.github.portlek.reflection.RefClass;
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
        final RefClass<CfgSection> parentclass = new ClassOf<>(this.parent);
        parentclass
            .declaredFieldsWithAnnotation(Property.class, (refField, property) -> {
                new PropertyProceed(this.parent, property, refField).load();
            });
        parentclass
            .declaredFieldsWithAnnotation(Instance.class, (refField, instance) -> {
                refField.of(this.parent).get()
                    .ifPresent(initiatedCfgSection ->
                        new ClassOf<>(initiatedCfgSection).annotation(Section.class, section -> {
                            final CfgSection newsection = this.parent.getOrCreateSection(section.value());
                            newsection.setup(this.parent.getManaged(),
                                newsection.getConfigurationSection());
                            new FieldsProceed(newsection).load();
                        }));
            });
    }

}
