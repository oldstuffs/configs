package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.structure.comparable.CmprblManaged;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class PerObjectConfigProceed {

    @NotNull
    private final LinkedConfig config;

    @NotNull
    private final CmprblManaged perobject;

    public void load() {

    }

}
