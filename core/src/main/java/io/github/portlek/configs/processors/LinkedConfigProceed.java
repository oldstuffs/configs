package io.github.portlek.configs.processors;

import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import org.jetbrains.annotations.NotNull;

public final class LinkedConfigProceed implements Proceed<LinkedManaged> {

    @NotNull
    private final LinkedConfig linkedConfig;

    @NotNull
    private final BiFunction<Object, String, Optional<?>> get;

    @NotNull
    private final BiPredicate<Object, String> set;

    public LinkedConfigProceed(@NotNull final LinkedConfig linkedConfig) {
        this(linkedConfig, (o, s) -> Optional.empty(), (o, s) -> false);
    }

    public LinkedConfigProceed(@NotNull final LinkedConfig linkedConfig,
                               @NotNull final BiFunction<Object, String, Optional<?>> get,
                               @NotNull final BiPredicate<Object, String> set) {
        this.linkedConfig = linkedConfig;
        this.get = get;
        this.set = set;
    }

    @Override
    public void load(@NotNull final LinkedManaged linkedManaged) throws Exception {
        final Optional<Config> configOptional = Arrays.stream(this.linkedConfig.configs())
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
                this.get,
                this.set
            ).load(linkedManaged);
        }
    }

}
