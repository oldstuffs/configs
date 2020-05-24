package io.github.portlek.configs.obj;

import io.github.portlek.configs.annotations.ConfigSerializable;
import io.github.portlek.configs.annotations.Property;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@ConfigSerializable
@RequiredArgsConstructor
@Getter
public final class ProvidedObject {

    @Property(path = "unique-id")
    private final UUID uuid;

    @Property
    private final String name;

    @Property
    private final int age;

}
