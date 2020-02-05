package io.github.portlek.configs.processors;

import io.github.portlek.configs.LinkedManaged;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public final class LinkedConfigProceed implements Proceed<LinkedManaged> {

    @NotNull
    private final LinkedConfig linkedConfig;

    @NotNull
    private final BiFunction<Object, String, Optional<?>> get;

    @NotNull
    private final BiPredicate<Object, String> set;

    public LinkedConfigProceed(@NotNull LinkedConfig linkedConfig,
                               @NotNull BiFunction<Object, String, Optional<?>> get,
                               @NotNull BiPredicate<Object, String> set) {
        this.linkedConfig = linkedConfig;
        this.get = get;
        this.set = set;
    }

    public LinkedConfigProceed(@NotNull LinkedConfig linkedConfig) {
        this(linkedConfig, (o, s) -> Optional.empty(), (o, s) -> false);
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
            new ConfigProceed(
                configOptional.get(),
                get,
                set
            ).load(linkedManaged);
        }
    }

}
