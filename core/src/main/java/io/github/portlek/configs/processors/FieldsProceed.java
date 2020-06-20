package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.Instance;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.annotations.Section;
import io.github.portlek.configs.structure.section.CfgSection;
import io.github.portlek.reflection.RefClass;
import io.github.portlek.reflection.clazz.ClassOf;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class FieldsProceed {

    @NotNull
    private final CfgSection parent;

    public void load() {
        final RefClass<CfgSection> parentclass = new ClassOf<>(this.parent);
        parentclass
            .declaredFieldsWithAnnotation(Property.class, (refField, property) ->
                new PropertyProceed(this.parent, property, refField).load());
        parentclass
            .declaredFieldsWithAnnotation(Instance.class, (refField, instance) ->
                refField.of(this.parent).get()
                    .filter(o -> CfgSection.class.isAssignableFrom(o.getClass()))
                    .map(o -> (CfgSection) o)
                    .ifPresent(initiatedCfgSection ->
                        new ClassOf<>(initiatedCfgSection).annotation(Section.class, section -> {
                            initiatedCfgSection.setup(this.parent.getParent(),
                                this.parent.getOrCreateSection(section.value()).getConfigurationSection());
                            new FieldsProceed(initiatedCfgSection).load();
                        })));
    }

}
