package io.github.portlek.configs;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
    public Set<String> getKeys(final boolean deep) {
        return this.getConfigurationSection().getKeys(deep);
    }

    @NotNull
    @Override
    public final Optional<Object> get(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().get(path));
    }

    @NotNull
    @Override
    public final Optional<Object> get(@NotNull final String path, @Nullable final Object def) {
        return Optional.ofNullable(this.getConfigurationSection().get(path, def));
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public final <T> T getOrSet(@NotNull final String path, @NotNull final T fallback) {
        return ((Optional<T>) this.get(path)).orElseGet(() -> {
            this.set(path, fallback);
            this.autoSave();
            return fallback;
        });
    }

    @Override
    public final void set(@NotNull final String path, @Nullable final Object object) {
        this.getConfigurationSection().set(path, object);
        this.autoSave();
    }

    @NotNull
    @Override
    public final Optional<ConfigSection> getSection(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getConfigurationSection(path))
            .map(configurationsection -> {
                final ConfigSection configsection = new ConfigSectionBase();
                configsection.setup(this.getManaged(), configurationsection);
                return configsection;
            });
    }

    @NotNull
    @Override
    public final ConfigSection getOrCreateSection(@NotNull final String path) {
        return this.getSection(path).orElseGet(() -> this.createSection(path));
    }

    @Override
    public final ConfigSection createSection(@NotNull final String path) {
        final ConfigSection configsection = new ConfigSectionBase();
        configsection.setup(this.getManaged(), this.getConfigurationSection().createSection(path));
        configsection.setAutoSave(this.autosave);
        configsection.autoSave();
        return configsection;
    }

    @NotNull
    @Override
    public final Optional<String> getString(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getString(path));
    }

    @NotNull
    @Override
    public final Optional<String> getString(@NotNull final String path, @Nullable final String def) {
        return Optional.ofNullable(this.getConfigurationSection().getString(path, def));
    }

    @Override
    public final int getInt(@NotNull final String path) {
        return this.getConfigurationSection().getInt(path);
    }

    @Override
    public final int getInt(@NotNull final String path, final int def) {
        return this.getConfigurationSection().getInt(path, def);
    }

    @Override
    public final boolean getBoolean(@NotNull final String path) {
        return this.getConfigurationSection().getBoolean(path);
    }

    @Override
    public final boolean getBoolean(@NotNull final String path, final boolean def) {
        return this.getConfigurationSection().getBoolean(path, def);
    }

    @Override
    public final double getDouble(@NotNull final String path) {
        return this.getConfigurationSection().getDouble(path);
    }

    @Override
    public final double getDouble(@NotNull final String path, final double def) {
        return this.getConfigurationSection().getDouble(path, def);
    }

    @Override
    public final long getLong(@NotNull final String path) {
        return this.getConfigurationSection().getLong(path);
    }

    @Override
    public final long getLong(@NotNull final String path, final long def) {
        return this.getConfigurationSection().getLong(path, def);
    }

    @NotNull
    @Override
    public final List<String> getStringList(@NotNull final String path) {
        return this.getConfigurationSection().getStringList(path);
    }

    @NotNull
    @Override
    public final List<Integer> getIntegerList(@NotNull final String path) {
        return this.getConfigurationSection().getIntegerList(path);
    }

    @NotNull
    @Override
    public final List<Boolean> getBooleanList(@NotNull final String path) {
        return this.getConfigurationSection().getBooleanList(path);
    }

    @NotNull
    @Override
    public final List<Double> getDoubleList(@NotNull final String path) {
        return this.getConfigurationSection().getDoubleList(path);
    }

    @NotNull
    @Override
    public final List<Float> getFloatList(@NotNull final String path) {
        return this.getConfigurationSection().getFloatList(path);
    }

    @NotNull
    @Override
    public final List<Long> getLongList(@NotNull final String path) {
        return this.getConfigurationSection().getLongList(path);
    }

    @NotNull
    @Override
    public final List<Byte> getByteList(@NotNull final String path) {
        return this.getConfigurationSection().getByteList(path);
    }

    @NotNull
    @Override
    public final List<Character> getCharacterList(@NotNull final String path) {
        return this.getConfigurationSection().getCharacterList(path);
    }

    @NotNull
    @Override
    public final List<Short> getShortList(@NotNull final String path) {
        return this.getConfigurationSection().getShortList(path);
    }

    @NotNull
    @Override
    public final Optional<List<?>> getList(@NotNull final String path) {
        return Optional.ofNullable(this.getConfigurationSection().getList(path));
    }

    @Override
    public final boolean isAutoSave() {
        return this.autosave;
    }

    @Override
    public final void setAutoSave(final boolean autosv) {
        this.autosave = autosv;
    }

    @Override
    public final void autoSave() {
        if (this.autosave) {
            this.getManaged().save();
        }
    }

    @NotNull
    @Override
    public ConfigurationSection getConfigurationSection() {
        return Objects.requireNonNull(this.section, "You have to load your class with '#load()' method");
    }

    @Override
    @NotNull
    public final Managed getManaged() {
        return Objects.requireNonNull(this.managed, "You have to load your class with '#load()' method");
    }

    @Override
    public final void setup(@NotNull final Managed managed, @NotNull final ConfigurationSection configurationSection) {
        this.section = configurationSection;
        this.managed = managed;
    }

}
