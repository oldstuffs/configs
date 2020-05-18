package io.github.portlek.configs;

import io.github.portlek.configs.files.yaml.FileConfiguration;
import io.github.portlek.configs.util.MapEntry;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class LinkedFileManaged extends FileManaged implements LnkdFlManaged {

    @NotNull
    private final Map<String, Map.Entry<File, FileConfiguration>> linkedFiles = new HashMap<>();

    @NotNull
    private final Supplier<String> chosen;

    @SafeVarargs
    protected LinkedFileManaged(@NotNull final Supplier<String> chosen, @NotNull final Map.Entry<String, Object>... objects) {
        super(objects);
        this.chosen = chosen;
    }

    @NotNull
    @Override
    public final <T> T match(@NotNull final Function<String, Optional<T>> function) {
        return function.apply(this.chosen.get()).orElseThrow(() ->
            new IllegalStateException("Cannot found match with the file key > " + this.chosen)
        );
    }

    @NotNull
    @Override
    public final Supplier<String> getChosen() {
        return this.chosen;
    }

    @Override
    public final void setup(@NotNull final File file, @NotNull final FileConfiguration fileConfiguration) {
        super.setup(file, fileConfiguration);
        this.linkedFiles.put(
            this.chosen.get(),
            MapEntry.from(file, fileConfiguration)
        );
    }

}
