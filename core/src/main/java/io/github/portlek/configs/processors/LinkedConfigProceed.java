package io.github.portlek.configs.processors;

import io.github.portlek.configs.LnkdFlManaged;
import io.github.portlek.configs.annotations.LinkedConfig;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public final class LinkedConfigProceed implements Proceed<LnkdFlManaged> {

    @NotNull
    private final LinkedConfig config;

    public LinkedConfigProceed(@NotNull final LinkedConfig cnfg) {
        this.config = cnfg;
    }

    @Override
    public void load(@NotNull final LnkdFlManaged linked) {
        Arrays.stream(this.config.files())
            .filter(linkedFile -> linkedFile.key().equals(linked.getChosen()))
            .findFirst()
            .ifPresent(linkedFile ->
                new ConfigProceed(linkedFile.config()).load(linked)
            );
    }

}
