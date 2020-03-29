package io.github.portlek.configs;

import io.github.portlek.configs.util.BukkitItemStackProvider;
import io.github.portlek.configs.util.Provided;
import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

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
        return (FileConfiguration) super.getConfigurationSection();
    }

    @NotNull
    @Override
    public final Optional<Object> pull(@NotNull final String id) {
        return this.base.pull(id);
    }

    @Override
    public void setup(@NotNull final File file, @NotNull final FileConfiguration fileConfiguration) {
        this.base.setup(file, fileConfiguration);
    }

    @Override
    public final <T> void addCustomValue(@NotNull final Class<T> aClass, @NotNull final Provided<T> provided) {
        this.base.addCustomValue(aClass, provided);
    }

    @NotNull
    @Override
    public final <T> Optional<Provided<T>> getCustomValue(@NotNull final Class<T> aClass) {
        return this.base.getCustomValue(aClass);
    }

    @NotNull
    @Override
    public final File getFile() {
        return this.base.getFile();
    }

    @Override
    public final void addObject(@NotNull final String key, @NotNull final Object object) {
        this.base.addObject(key, object);
    }

}
