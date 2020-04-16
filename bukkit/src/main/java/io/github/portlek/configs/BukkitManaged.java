package io.github.portlek.configs;

import io.github.portlek.configs.util.BukkitItemStackProvider;
import io.github.portlek.configs.util.Provided;
import io.github.portlek.configs.yaml.configuration.file.FileConfiguration;
import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BukkitManaged extends BukkitSection implements FlManaged {

    @SafeVarargs
    public BukkitManaged(@NotNull final Map.Entry<String, Object>... objects) {
        this(new FileManaged(), objects);
    }

    @SafeVarargs
    public BukkitManaged(@NotNull final FlManaged managed, @NotNull final Map.Entry<String, Object>... objects) {
        super(managed);
        this.addCustomValue(ItemStack.class, new BukkitItemStackProvider());
        Arrays.stream(objects).forEach(entry -> this.addObject(entry.getKey(), entry.getValue()));
    }

    @NotNull
    @Override
    public final FileConfiguration getConfigurationSection() {
        return ((FlManaged) this.getBase()).getConfigurationSection();
    }

    @NotNull
    @Override
    public final Optional<Object> pull(@NotNull final String id) {
        return ((FlManaged) this.getBase()).pull(id);
    }

    @Override
    public final void setup(@NotNull final File file, @NotNull final FileConfiguration fileConfiguration) {
        ((FlManaged) this.getBase()).setup(file, fileConfiguration);
    }

    @Override
    public final <T> void addCustomValue(@NotNull final Class<T> aClass, @NotNull final Provided<T> provided) {
        ((FlManaged) this.getBase()).addCustomValue(aClass, provided);
    }

    @NotNull
    @Override
    public final <T> Optional<Provided<T>> getCustomValue(@NotNull final Class<T> aClass) {
        return ((FlManaged) this.getBase()).getCustomValue(aClass);
    }

    @NotNull
    @Override
    public final File getFile() {
        return ((FlManaged) this.getBase()).getFile();
    }

    @Override
    public final void addObject(@NotNull final String key, @NotNull final Object object) {
        ((FlManaged) this.getBase()).addObject(key, object);
    }

    @Override
    public final boolean isAutoSave() {
        return ((FlManaged) this.getBase()).isAutoSave();
    }

    @Override
    public final void setAutoSave(final boolean autosv) {
        ((FlManaged) this.getBase()).setAutoSave(autosv);
    }

    @Override
    public final void autoSave() {
        ((FlManaged) this.getBase()).autoSave();
    }

}
