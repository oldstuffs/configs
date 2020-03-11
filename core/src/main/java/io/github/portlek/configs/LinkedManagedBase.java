package io.github.portlek.configs;

import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.processors.LinkedConfigProceed;
import io.github.portlek.configs.util.MapEntry;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

public abstract class LinkedManagedBase extends ManagedBase implements LinkedManaged {

    @NotNull
    private final Map<String, Map.Entry<File, FileConfiguration>> linkedFiles = new HashMap<>();

    @NotNull
    private final String chosenFileName;

    @SafeVarargs
    protected LinkedManagedBase(@NotNull final String chosenFileName,
                                @NotNull final Map.Entry<String, Object>... objects) {
        super(objects);
        this.chosenFileName = chosenFileName;
    }

    @NotNull
    @Override
    public final <T> T match(@NotNull final Function<String, Optional<T>> function) {
        return function.apply(this.chosenFileName).orElseThrow(() ->
            new IllegalStateException("Cannot found match with the file id > " + this.chosenFileName)
        );
    }

    @NotNull
    @Override
    public final String getChosenFileName() {
        return this.chosenFileName;
    }

    @Override
    public final void load() {
        final LinkedConfig linkedConfig = this.getClass().getDeclaredAnnotation(LinkedConfig.class);
        if (linkedConfig != null) {
            try {
                new LinkedConfigProceed(linkedConfig).load(this);
                return;
            } catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public final void setup(@NotNull final File file, @NotNull final FileConfiguration fileConfiguration) {
        this.linkedFiles.put(
            this.chosenFileName,
            MapEntry.of(file, fileConfiguration)
        );
    }

    @NotNull
    @Override
    public final File getFile() {
        return Objects.requireNonNull(
            this.linkedFiles.get(this.chosenFileName).getKey(),
            "You have to load your class with '#load()' method"
        );
    }

    @NotNull
    @Override
    public final FileConfiguration getFileConfiguration() {
        return Objects.requireNonNull(
            this.linkedFiles.get(this.chosenFileName).getValue(),
            "You have to load your class with '#load()' method"
        );
    }

}
