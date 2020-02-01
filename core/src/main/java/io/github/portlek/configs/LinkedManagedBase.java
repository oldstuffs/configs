package io.github.portlek.configs;

import org.jetbrains.annotations.NotNull;

public abstract class LinkedManagedBase extends ManagedBase {

    @NotNull
    private final String fileId;

    public LinkedManagedBase(@NotNull String fileId) {
        this.fileId = fileId;
    }

    @NotNull
    public String getFileId() {
        return fileId;
    }

}
