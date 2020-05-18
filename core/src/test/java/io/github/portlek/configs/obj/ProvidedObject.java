package io.github.portlek.configs.obj;

import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public final class ProvidedObject {

    @NotNull
    private final UUID uuid;

    @NotNull
    private final String name;

    private final int age;

}
