package io.github.portlek.configs;

import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;

public class BukkitSection implements BkktSection {

    @NotNull
    private final CfgSection base;

    public BukkitSection() {
        this(new ConfigSection());
    }

    public BukkitSection(@NotNull final CfgSection base) {
        this.base = base;
    }

    @NotNull
    @Override
    public final CfgSection getBase() {
        return this.base;
    }

    @NotNull
    @Override
    public ConfigurationSection getConfigurationSection() {
        return this.base.getConfigurationSection();
    }

    @Override
    public final void autoSave() {
        this.base.autoSave();
    }

    @Override
    public final void setup(@NotNull final FlManaged managed, @NotNull final ConfigurationSection section) {
        this.base.setup(managed, section);
    }

    @Override
    @NotNull
    public final FlManaged getManaged() {
        return this.base.getManaged();
    }

    @Override
    public final boolean isAutoSave() {
        return this.base.isAutoSave();
    }

    @Override
    public final void setAutoSave(final boolean autosv) {
        this.base.setAutoSave(autosv);
    }

}
