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
        Arrays.stream(this.config.configs())
            .filter(config -> {
                final String suffix = config.type().suffix;
                String name = config.name();
                if (!name.endsWith(suffix)) {
                    name += suffix;
                }
                String chosen = linked.getChosen();
                if (!chosen.endsWith(suffix)) {
                    chosen += suffix;
                }
                return name.equals(chosen);
            }).findFirst()
            .ifPresent(config ->
                new ConfigProceed(
                    config
                ).load(linked)
            );
    }

}
