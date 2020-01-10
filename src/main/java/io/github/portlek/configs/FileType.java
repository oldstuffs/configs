package io.github.portlek.configs;

import org.jetbrains.annotations.NotNull;

public enum FileType {

    YAML(".yml"),
    JSON(".json"),
    XML(".xml");

    @NotNull
    private final String suffix;

    FileType(@NotNull String suffix) {
        this.suffix = suffix;
    }

    @NotNull
    public String getSuffix() {
        return suffix;
    }

}
