package io.github.portlek.configs.processors;

import io.github.portlek.configs.LinkedManaged;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.LinkedConfig;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public final class LinkedConfigProceed implements Proceed<LinkedManaged> {

    @NotNull
    private final LinkedConfig config;

    public LinkedConfigProceed(@NotNull final LinkedConfig config) {
        this.config = config;
    }

    @Override
    public void load(@NotNull final LinkedManaged linked) {
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
