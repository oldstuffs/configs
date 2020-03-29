package io.github.portlek.configs;

import io.github.portlek.configs.util.Provided;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class TestProvided implements Provided<ProvidedObject> {

    @Override
    public void set(@NotNull final ProvidedObject providedObject, @NotNull final CfgSection section,
                    @NotNull final String path) {
        section.set(providedObject.getUuid() + ".name", providedObject.getName());
        section.set(providedObject.getUuid() + ".age", providedObject.getAge());
    }

    @NotNull
    @Override
    public Optional<ProvidedObject> get(@NotNull final CfgSection section, @NotNull final String path) {
        final String uuid = "9e03090a-c24b-43a3-8c29-0d47b7e3efc5";
        return Optional.of(
            new ProvidedObject(
                UUID.fromString(uuid),
                section.getOrSet(uuid + ".name", "Not found"),
                section.getOrSet(uuid + ".age", 0)
            )
        );
    }

}
