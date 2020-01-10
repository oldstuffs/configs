package io.github.portlek.configs.util;

import org.jetbrains.annotations.NotNull;

public final class Migrate {

    @NotNull
    private final String path;

    @NotNull
    private String only = "";

    @NotNull
    private String until = "";

    @NotNull
    private String before = "";

    @NotNull
    private String after = "";

    private Migrate(@NotNull String path) {
        this.path = path;
    }

    public static Migrate of(@NotNull String path) {
        return new Migrate(path);
    }

    public Migrate only(@NotNull String only) {
        this.only = only;
        return this;
    }

    public Migrate until(@NotNull String until) {
        this.until = until;
        return this;
    }

    public Migrate before(@NotNull String before) {
        this.before = before;
        return this;
    }

    public Migrate after(@NotNull String after) {
        this.after = after;
        return this;
    }

    @NotNull
    public String getPath() {
        return path;
    }

    @NotNull
    public String getOnly() {
        return only;
    }

    @NotNull
    public String getUntil() {
        return until;
    }

    @NotNull
    public String getBefore() {
        return before;
    }

    @NotNull
    public String getAfter() {
        return after;
    }

}
