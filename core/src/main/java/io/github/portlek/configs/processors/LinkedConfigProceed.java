package io.github.portlek.configs.processors;

import io.github.portlek.configs.LnkdFlManaged;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.annotations.LinkedFile;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class LinkedConfigProceed implements Proceed<LnkdFlManaged> {

    @NotNull
    private final LinkedConfig config;

    @Override
    public void load(@NotNull final LnkdFlManaged linked) {
        Arrays.stream(this.config.files())
            .filter(linkedFile -> linkedFile.key().equals(linked.getChosen().get()))
            .findFirst()
            .map(LinkedFile::config)
            .map(ConfigProceed::new)
            .ifPresent(configProceed -> configProceed.load(linked));
    }

}
