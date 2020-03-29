package io.github.portlek.configs;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class ProvidedObject {

    @NotNull
    private final UUID uuid;

    @NotNull
    private final String name;

    private final int age;

    public ProvidedObject(@NotNull final UUID uuid, @NotNull final String name, final int age) {
        this.uuid = uuid;
        this.name = name;
        this.age = age;
    }

    @NotNull
    public UUID getUuid() {
        return this.uuid;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

}
