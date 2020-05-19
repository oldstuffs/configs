package io.github.portlek.configs.obj;

import io.github.portlek.configs.provided.Provided;
import io.github.portlek.configs.structure.managed.section.CfgSection;
import io.github.portlek.configs.util.GeneralUtilities;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class TestProvided implements Provided<ProvidedObject> {

    @Override
    public void set(@NotNull final ProvidedObject providedObject, @NotNull final CfgSection section,
                    @NotNull final String path) {
        final String finalpath = GeneralUtilities.putDot(path);
        section.set(finalpath + "uuid", providedObject.getUuid().toString());
        section.set(finalpath + "name", providedObject.getName());
        section.set(finalpath + "age", providedObject.getAge());
    }

    @NotNull
    @Override
    public Optional<ProvidedObject> get(@NotNull final CfgSection section, @NotNull final String path) {
        final String finalpath = GeneralUtilities.putDot(path);
        final Optional<CfgSection> optional = section.getSection(finalpath);
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        final CfgSection providedsection = optional.get();
        final String uuid = "9e03090a-c24b-43a3-8c29-0d47b7e3efc5";
        return Optional.of(
            new ProvidedObject(
                providedsection.getOrSetUniqueId("uuid", uuid),
                providedsection.getOrSetString("name", "Not found"),
                providedsection.getOrSetInteger("age", 0)
            )
        );
    }

}
