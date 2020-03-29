package io.github.portlek.configs;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;

public class ConfigSectionBase implements ConfigSection {

    @Nullable
    private ConfigurationSection section;

    @Nullable
    private Managed managed;

    private boolean autosave = false;

    @NotNull
    @Override
    public ConfigurationSection getConfigurationSection() {
        return Objects.requireNonNull(this.section, "You have to load your class with '#load()' method");
    }

    @Override
    public final void autoSave() {
        if (this.autosave) {
            this.getManaged().save();
        }
    }

    @Override
    public final void setup(@NotNull final Managed managed, @NotNull final ConfigurationSection configurationSection) {
        this.section = configurationSection;
        this.managed = managed;
    }

    @Override
    @NotNull
    public final Managed getManaged() {
        return Objects.requireNonNull(this.managed, "You have to load your class with '#load()' method");
    }

    @Override
    public final boolean isAutoSave() {
        return this.autosave;
    }

    @Override
    public final void setAutoSave(final boolean autosv) {
        this.autosave = autosv;
    }

}
