package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.structure.perobject.PrObjctManaged;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class PerObjectConfigProceed {

    @NotNull
    private final LinkedConfig config;

    @NotNull
    private final PrObjctManaged perobject;

    public void load() {

    }

}
