package io.github.portlek.configs.util;

import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public final class CreateStorage implements Scalar<File> {

    @NotNull
    private final String path;

    @NotNull
    private final String fileName;

    public CreateStorage(@NotNull String path, @NotNull String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    @NotNull
    public File value() {
        final File storage = new File(path, fileName);

        if (!storage.exists()) {
            storage.getParentFile().mkdirs();

            try {
                storage.createNewFile();
            } catch (IOException exception) {
                throw new IllegalStateException(exception);
            }
        }

        if (!storage.exists()) {
            throw new IllegalStateException(storage.getName() + " cannot be created, please check file permissions!");
        }

        return storage;
    }

}