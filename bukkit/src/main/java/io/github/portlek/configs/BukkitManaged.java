package io.github.portlek.configs;

import java.util.Map;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BukkitManaged extends ManagedBase {

    @SafeVarargs
    protected BukkitManaged(@NotNull final Map.@NotNull Entry<String, Object>... objects) {
        super(objects);
        this.addCustomValue(ItemStack.class, new BukkitItemStackProvider());
    }

    public void setItemStack(@NotNull final String path, @NotNull final ItemStack itemstack) {
        this.getCustomValue(ItemStack.class).ifPresent(provided ->
            provided.set(itemstack, this, path));
    }

    @NotNull
    public Optional<ItemStack> getItemStack(@NotNull final String path) {
        return this.getCustomValue(ItemStack.class)
            .flatMap(provided -> provided.get(this, path));
    }

}
