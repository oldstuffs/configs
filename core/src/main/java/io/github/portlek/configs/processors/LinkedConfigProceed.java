package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.Comment;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.annotations.LinkedFile;
import io.github.portlek.configs.structure.LnkdFlManaged;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class LinkedConfigProceed implements Runnable {

    @NotNull
    private final LnkdFlManaged linked;

    @NotNull
    private final LinkedConfig config;

    @Override
    public void run() {
        Arrays.stream(this.config.files())
            .filter(linkedFile -> linkedFile.key().equals(this.linked.getChosen().get()))
            .findFirst()
            .map(LinkedFile::config)
            .map(cnfg -> new ConfigProceed(this.linked, cnfg))
            .ifPresent(configProceed -> {
                configProceed.run();
                this.linked.getClass().getDeclaredAnnotation(Comment.class);

            });
    }

}
