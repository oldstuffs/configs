package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.ComparableConfig;
import io.github.portlek.configs.structure.comparable.CmprblManaged;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class ComparableConfigProceed<S extends CmprblManaged<S>> {

    @NotNull
    private final ComparableConfig comparable;

    @NotNull
    private final CmprblManaged<S> managed;

    public void load() {
        Arrays.stream(this.comparable.value())
            .map(config -> new ConfigProceed(config, this.managed))
            .forEach(ConfigProceed::load);
    }

}
