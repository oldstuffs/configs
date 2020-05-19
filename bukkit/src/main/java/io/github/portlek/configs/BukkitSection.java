package io.github.portlek.configs;

import io.github.portlek.configs.files.configuration.ConfigurationSection;
import io.github.portlek.configs.structure.managed.FlManaged;
import io.github.portlek.configs.structure.managed.section.CfgSection;
import io.github.portlek.configs.structure.managed.section.ConfigSection;
import org.jetbrains.annotations.NotNull;

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
    public CfgSection getBase() {
        return this.base;
    }

    @NotNull
    @Override
    public ConfigurationSection getConfigurationSection() {
        return this.getBase().getConfigurationSection();
    }

    @Override
    @NotNull
    public final FlManaged getManaged() {
        return this.getBase().getManaged();
    }

    @Override
    public final void setup(@NotNull final FlManaged managed, @NotNull final ConfigurationSection section) {
        this.getBase().setup(managed, section);
    }

}
