package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.structure.comparable.CmprblManaged;
import io.github.portlek.configs.structure.managed.FlManaged;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class ComparableConfigProceed {

    @NotNull
    private final LinkedConfig linked;

    @NotNull
    private final CmprblManaged<?> managed;

    public void load() {
        Arrays.stream(this.linked.value()).forEach(linkedFile -> {
            final FlManaged flmanaged = this.managed.getNewManaged().get();
            this.managed.setup(linkedFile.key(), flmanaged);
            this.managed.key(linkedFile.key());
            new ConfigProceed(linkedFile.config(), flmanaged, this.managed).load();
            flmanaged.setAutoSave(this.managed.isAutoSave());
        });
    }

}
