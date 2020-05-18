package io.github.portlek.configs.obj;

import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.provided.Provided;
import io.github.portlek.configs.util.PutDot;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class TestProvided implements Provided<ProvidedObject> {

    @Override
    public void set(@NotNull final ProvidedObject providedObject, @NotNull final CfgSection section,
                    @NotNull final String path) {
        final String finalpath = new PutDot(path).value();
        section.set(finalpath + "uuid", providedObject.getUuid().toString());
        section.set(finalpath + "name", providedObject.getName());
        section.set(finalpath + "age", providedObject.getAge());
    }

    @NotNull
    @Override
    public Optional<ProvidedObject> get(@NotNull final CfgSection section, @NotNull final String path) {
        final String finalpath = new PutDot(path).value();
        final Optional<CfgSection> optional = section.getSection(finalpath);
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        final CfgSection providedsection = optional.get();
        final String uuid = "9e03090a-c24b-43a3-8c29-0d47b7e3efc5";
        return Optional.of(
            new ProvidedObject(
                UUID.fromString(providedsection.getOrSet("uuid", uuid)),
                providedsection.getOrSet("name", "Not found"),
                providedsection.getOrSet("age", 0)
            )
        );
    }

}
