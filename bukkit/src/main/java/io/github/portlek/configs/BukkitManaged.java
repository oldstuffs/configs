package io.github.portlek.configs;

import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class BukkitManaged extends ManagedBase {

    @SafeVarargs
    protected BukkitManaged(@NotNull final Map.@NotNull Entry<String, Object>... objects) {
        super(objects);
        this.addCustomValue(ItemStack.class, new BukkitItemStackProvider());
    }

}
