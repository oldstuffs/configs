package io.github.portlek.configs;

import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.processors.LinkedConfigProceed;
import io.github.portlek.configs.util.MapEntry;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

public abstract class LinkedManagedBase extends ManagedBase implements LinkedManaged {

    @NotNull
    private final Map<String, Map.Entry<File, FileConfiguration>> linkedFiles = new HashMap<>();

    @NotNull
    private final String chosen;

    @SafeVarargs
    protected LinkedManagedBase(@NotNull final String chosen,
                                @NotNull final Map.Entry<String, Object>... objects) {
        super(objects);
        this.chosen = chosen;
    }

    @NotNull
    @Override
    public final <T> T match(@NotNull final Function<String, Optional<T>> function) {
        return function.apply(this.chosen).orElseThrow(() ->
            new IllegalStateException("Cannot found match with the file id > " + this.chosen)
        );
    }

    @NotNull
    @Override
    public final String getChosen() {
        return this.chosen;
    }

    @Override
    public final void load() {
        final LinkedConfig linked = this.getClass().getDeclaredAnnotation(LinkedConfig.class);
        if (linked != null) {
            new LinkedConfigProceed(linked).load(this);
            return;
        }
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " has not `LinkedConfig` annotation!");
    }

    @Override
    public final void setup(@NotNull final File file, @NotNull final FileConfiguration fileConfiguration) {
        super.setup(file, fileConfiguration);
        this.linkedFiles.put(
            this.chosen,
            MapEntry.from(file, fileConfiguration)
        );
    }

}
