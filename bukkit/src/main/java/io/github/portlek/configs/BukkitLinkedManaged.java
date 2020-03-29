package io.github.portlek.configs;

import io.github.portlek.configs.util.BukkitItemStackProvider;
import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

public class BukkitLinkedManaged extends BukkitManaged implements LnkdFlManaged {

    @SafeVarargs
    public BukkitLinkedManaged(@NotNull final String chosen,
                               @NotNull final Map.Entry<String, Object>... objects) {
        this.base = new LinkedFileManaged(chosen);
        this.addCustomValue(ItemStack.class, new BukkitItemStackProvider());
    }

    @NotNull
    @Override
    public final <T> T match(@NotNull final Function<String, Optional<T>> function) {
        return this.base.match(function);
    }

    @NotNull
    @Override
    public final String getChosen() {
        return this.base.getChosen();
    }

    @Override
    public void setup(final @NotNull File file, final @NotNull FileConfiguration fileConfiguration) {
        this.base.setup(file, fileConfiguration);
    }

}
