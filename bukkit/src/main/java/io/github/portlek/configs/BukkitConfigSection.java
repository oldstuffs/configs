package io.github.portlek.configs;

import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BukkitConfigSection extends ConfigSectionBase {

    public final void setItemStack(@NotNull final String path, @NotNull final ItemStack item) {
        this.getManaged().getCustomValue(ItemStack.class).ifPresent(provided ->
            provided.set(item, this, path));
    }

    @NotNull
    public final Optional<ItemStack> getItemStack(@NotNull final String path) {
        return this.getManaged().getCustomValue(ItemStack.class)
            .flatMap(provided -> provided.get(this, path));
    }

}
