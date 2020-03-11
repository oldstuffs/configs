package io.github.portlek.configs;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.github.portlek.configs.util.BukkitVersion;
import io.github.portlek.configs.util.ColorUtil;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public final class BukkitItemStackProvider implements Provided<ItemStack> {

    private static final BukkitVersion BUKKIT_VERSION = new BukkitVersion();

    @Override
    public void set(@NotNull final Object fieldValue, @NotNull final Managed managed, @NotNull final String path) {
        if (!(fieldValue instanceof ItemStack)) {
            return;
        }
        final ItemStack itemStack = (ItemStack) fieldValue;
        managed.set(path + ".material", itemStack.getType().name());
        managed.set(path + ".amount", itemStack.getAmount());
        if (BukkitItemStackProvider.BUKKIT_VERSION.minor() < 13) {
            Optional.ofNullable(itemStack.getData()).ifPresent(materialData ->
                managed.set(path + ".data", (int)materialData.getData()));
        }
        if (itemStack.getDurability() != 0) {
            managed.set(path + ".damage", itemStack.getDurability());
        }
        Optional.ofNullable(itemStack.getItemMeta()).ifPresent(itemMeta -> {
            if (itemMeta.hasDisplayName()) {
                managed.set(path + ".display-name", itemMeta.getDisplayName().replace("§", "&"));
            }
            Optional.ofNullable(itemMeta.getLore()).ifPresent(lore ->
                managed.set(path + ".lore", lore.stream().map(s -> s.replace("§", "&")).collect(Collectors.toList()))
            );
        });
        itemStack.getEnchantments().forEach((enchantment, integer) ->
            managed.set(path + ".enchants." + enchantment.getName(), integer)
        );
    }

    @NotNull
    @Override
    public Optional<ItemStack> get(@NotNull final Managed managed, @NotNull final String path) {
        if (!managed.getString(path).isPresent()) {
            return Optional.empty();
        }
        final Optional<String> materialStringOptional = managed.getString(path + ".material");
        if (!materialStringOptional.isPresent()) {
            return Optional.empty();
        }
        final String materialString = materialStringOptional.get();
        final Material material;
        if (BukkitItemStackProvider.BUKKIT_VERSION.minor() > 7) {
            final Optional<XMaterial> xMaterialOptional = XMaterial.matchXMaterial(materialString);
            if (!xMaterialOptional.isPresent()) {
                return Optional.empty();
            }
            final Optional<Material> materialOptional = Optional.ofNullable(xMaterialOptional.get().parseMaterial());
            if (!materialOptional.isPresent()) {
                return Optional.empty();
            }
            material = materialOptional.get();
        } else {
            material = Material.getMaterial(materialString);
        }
        final int amount = managed.getInt(path + ".amount");
        final ItemStack itemStack;
        if (BukkitItemStackProvider.BUKKIT_VERSION.minor() < 13) {
            itemStack = new ItemStack(
                material,
                amount == 0 ? 1 : amount,
                (short) managed.getInt(path + ".damage"),
                (byte) managed.getInt(path + ".data")
            );
        } else {
            itemStack = new ItemStack(
                material,
                amount == 0 ? 1 : amount,
                (short) managed.getInt(path + ".damage")
            );
        }
        final Optional<ItemMeta> itemMetaOptional = Optional.ofNullable(itemStack.getItemMeta());
        if (!itemMetaOptional.isPresent()) {
            return Optional.empty();
        }
        final ItemMeta itemMeta = itemMetaOptional.get();
        managed.getString(path + ".display-name").ifPresent(s ->
            itemMeta.setDisplayName(
                ColorUtil.colored(s)
            )
        );
        itemMeta.setLore(
            ColorUtil.colored(
                managed.getStringList(path + ".lore")
            )
        );
        managed.getSection(path + ".enchants").map(section -> section.getKeys(false)).ifPresent(set ->
            set.forEach(s ->
                XEnchantment.matchXEnchantment(s).flatMap(xEnchantment ->
                    Optional.ofNullable(xEnchantment.parseEnchantment())
                ).ifPresent(enchantment ->
                    itemMeta.addEnchant(enchantment, managed.getInt(path + ".enchants." + s), true)
                )
            )
        );
        itemStack.setItemMeta(itemMeta);
        return Optional.of(itemStack);
    }

}
