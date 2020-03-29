package io.github.portlek.configs;

import java.util.Optional;
import java.util.function.Supplier;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface BkktSection extends CfgSection {

    @Override
    @NotNull
    default Supplier<CfgSection> getNewSection() {
        return BukkitSection::new;
    }

    @NotNull
    default Optional<ItemStack> getItemStack() {
        return this.getItemStack("");
    }

    default void setItemStack(@NotNull final ItemStack itemstack) {
        this.setItemStack("", itemstack);
    }

    default void setItemStack(@NotNull final String path, @NotNull final ItemStack itemstack) {
        final CfgSection section;
        if (path.isEmpty()) {
            section = this;
        } else {
            section = this.getOrCreateSection(path);
        }
        this.getManaged().getCustomValue(ItemStack.class)
            .ifPresent(provided -> provided.set(itemstack, section, path));
    }

    @NotNull
    default Optional<ItemStack> getItemStack(@NotNull final String path) {
        final CfgSection section;
        if (path.isEmpty()) {
            section = this;
        } else {
            section = this.getOrCreateSection(path);
        }
        return this.getManaged().getCustomValue(ItemStack.class)
            .flatMap(provided -> provided.get(section, path));
    }


}
