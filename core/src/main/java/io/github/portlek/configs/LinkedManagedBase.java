package io.github.portlek.configs;

import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.processors.LinkedConfigProceed;
import io.github.portlek.configs.util.MapEntry;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public abstract class LinkedManagedBase extends ManagedBase implements LinkedManaged {

    @NotNull
    private final Map<String, Map.Entry<File, FileConfiguration>> linkedFiles = new HashMap<>();

    @NotNull
    private final String chosenFileName;

    public LinkedManagedBase(@NotNull String chosenFileName) {
        this.chosenFileName = chosenFileName;
    }

    @NotNull
    @Override
    public <T> T match(@NotNull Function<String, Optional<T>> function) {
        return function.apply(chosenFileName).orElseThrow(() ->
            new IllegalStateException("Cannot found match with the file id > " + chosenFileName)
        );
    }

    @NotNull
    @Override
    public String getChosenFileName() {
        return chosenFileName;
    }

    @Override
    public void load() {
        final LinkedConfig linkedConfig = getClass().getDeclaredAnnotation(LinkedConfig.class);

        if (linkedConfig != null) {
            try {
                new LinkedConfigProceed(linkedConfig).load(this);

                return;
            } catch (Exception ignored) {
                // ignored
            }
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public void setup(@NotNull File file, @NotNull FileConfiguration fileConfiguration) {
        if (linkedFiles.containsKey(chosenFileName)) {
            throw new IllegalStateException("You can't use #setup(File, FileConfiguration) method twice!");
        }

        linkedFiles.put(
            chosenFileName,
            MapEntry.of(file, fileConfiguration)
        );
    }

    @NotNull
    @Override
    public File getFile() {
        return Objects.requireNonNull(
            linkedFiles.get(chosenFileName).getKey(),
            "You have to load your class with '#load()' method"
        );
    }

    @NotNull
    @Override
    public FileConfiguration getFileConfiguration() {
        return Objects.requireNonNull(
            linkedFiles.get(chosenFileName).getValue(),
            "You have to load your class with '#load()' method"
        );
    }

}
