package io.github.portlek.configs;

import java.util.Map;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class BukkitLinkedManaged extends LinkedManagedBase {

    @SafeVarargs
    protected BukkitLinkedManaged(@NotNull final String chosen,
                                  @NotNull final Map.Entry<String, Object>... objects) {
        super(chosen, objects);

    }

    public final void setItemStack(@NotNull final String path, @NotNull final ItemStack item) {
        this.getCustomValue(ItemStack.class).ifPresent(provided ->
            provided.set(item, this, path));
    }

    @NotNull
    public final Optional<ItemStack> getItemStack(@NotNull final String path) {
        return this.getCustomValue(ItemStack.class)
            .flatMap(provided -> ((BukkitItemStackProvider) provided).get(this, path));
    }

}
