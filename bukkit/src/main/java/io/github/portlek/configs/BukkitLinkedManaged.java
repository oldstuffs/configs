package io.github.portlek.configs;

import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.processors.LinkedConfigProceed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Optional;
import java.util.function.Function;

public abstract class BukkitLinkedManaged extends BukkitManaged implements LinkedManaged {

    @NotNull
    private final LinkedManagedBase linkedManagedBase;

    public BukkitLinkedManaged(@NotNull String chosenFileName) {
        this.linkedManagedBase = new LinkedManagedBase(chosenFileName) {};
    }

    @Override
    public void load() {
        final LinkedConfig linkedConfig = getClass().getDeclaredAnnotation(LinkedConfig.class);

        if (linkedConfig != null) {
            try {
                new LinkedConfigProceed(
                    linkedConfig,
                    get,
                    set
                ).load(this);

                return;
            } catch (Exception ignored) {
                // ignored
            }
        }

        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public <T> T match(@NotNull Function<String, Optional<T>> function) {
        return linkedManagedBase.match(function);
    }

    @NotNull
    @Override
    public String getChosenFileName() {
        return linkedManagedBase.getChosenFileName();
    }

    @Override
    public void setup(@NotNull File file, @NotNull FileConfiguration fileConfiguration) {
        linkedManagedBase.setup(file, fileConfiguration);
    }

    @NotNull
    @Override
    public File getFile() {
        return linkedManagedBase.getFile();
    }

    @NotNull
    @Override
    public FileConfiguration getFileConfiguration() {
        return linkedManagedBase.getFileConfiguration();
    }

}
