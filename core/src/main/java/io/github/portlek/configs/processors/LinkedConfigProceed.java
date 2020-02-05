package io.github.portlek.configs.processors;

import io.github.portlek.configs.LinkedManaged;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public final class LinkedConfigProceed implements Proceed<LinkedManaged> {

    @NotNull
    private final LinkedConfig linkedConfig;

    public LinkedConfigProceed(@NotNull LinkedConfig linkedConfig) {
        this.linkedConfig = linkedConfig;
    }

    @Override
    public void load(@NotNull LinkedManaged linkedManaged) throws Exception {
        final Optional<Config> configOptional = Arrays.stream(linkedConfig.configs())
            .filter(config -> {
                final String configSuffixName = config.type().suffix;
                String configFileName = config.name();

                if (!configFileName.endsWith(configSuffixName)) {
                    configFileName += configSuffixName;
                }

                String chosenFileName = linkedManaged.getChosenFileName();

                if (!chosenFileName.endsWith(configSuffixName)) {
                    chosenFileName += configSuffixName;
                }

                return configFileName.equals(chosenFileName);
            })
            .findFirst();

        if (configOptional.isPresent()) {
            new ConfigProceed(configOptional.get()).load(linkedManaged);
        }
    }

}
