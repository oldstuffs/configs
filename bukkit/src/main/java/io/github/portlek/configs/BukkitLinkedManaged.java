package io.github.portlek.configs;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

public abstract class BukkitLinkedManaged extends BukkitManaged implements LinkedManaged {

    @NotNull
    private final LinkedManaged linkedManaged;

    @SafeVarargs
    protected BukkitLinkedManaged(@NotNull final String chosenFileName,
                                  @NotNull final Map.Entry<String, Object>... objects) {
        super(objects);
        this.linkedManaged = new LinkedManagedBase(chosenFileName) {
        };
    }

    @Override
    public void load() {
        this.linkedManaged.load();
    }

    @Override
    public final void setup(@NotNull final File file, @NotNull final FileConfiguration fileConfiguration) {
        this.linkedManaged.setup(file, fileConfiguration);
    }

    @NotNull
    @Override
    public final File getFile() {
        return this.linkedManaged.getFile();
    }

    @NotNull
    @Override
    public final FileConfiguration getFileConfiguration() {
        return this.linkedManaged.getFileConfiguration();
    }

    @Nullable
    @Override
    public final <T> T match(@NotNull final Function<String, Optional<T>> function) {
        return this.linkedManaged.match(function);
    }

    @NotNull
    @Override
    public final String getChosenFileName() {
        return this.linkedManaged.getChosenFileName();
    }

}
