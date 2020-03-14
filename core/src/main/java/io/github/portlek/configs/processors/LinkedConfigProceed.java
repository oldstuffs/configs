package io.github.portlek.configs.processors;

import io.github.portlek.configs.LinkedManaged;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.LinkedConfig;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public final class LinkedConfigProceed implements Proceed<LinkedManaged> {

    @NotNull
    private final LinkedConfig linkedConfig;

    public LinkedConfigProceed(@NotNull final LinkedConfig linkedConfig) {
        this.linkedConfig = linkedConfig;
    }

    @Override
    public void load(@NotNull final LinkedManaged linkedManaged) {
        Arrays.stream(this.linkedConfig.configs())
            .filter(config -> {
                final String configSuffixName = config.type().suffix;
                String configFileName = config.name();
                if (!configFileName.endsWith(configSuffixName)) {
                    configFileName += configSuffixName;
                }
                String chosenFileName = linkedManaged.getChosen();
                if (!chosenFileName.endsWith(configSuffixName)) {
                    chosenFileName += configSuffixName;
                }
                return configFileName.equals(chosenFileName);
            })
            .findFirst().ifPresent(config -> {
            try {
                new ConfigProceed(
                    config
                ).load(linkedManaged);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

}
