package io.github.portlek.configs;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.github.portlek.configs.util.BukkitVersion;
import io.github.portlek.configs.util.ColorUtil;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class BukkitItemStackProvider implements Provided<ItemStack> {

    private static final BukkitVersion BUKKIT_VERSION = new BukkitVersion();

    @Override
    public void set(@NotNull final ItemStack itemStack, @NotNull final ConfigSection section, @NotNull final String path) {
        section.set(path + ".material", itemStack.getType().name());
        section.set(path + ".amount", itemStack.getAmount());
        if (BukkitItemStackProvider.BUKKIT_VERSION.minor() < 13) {
            Optional.ofNullable(itemStack.getData()).ifPresent(materialData ->
                section.set(path + ".data", (int) materialData.getData()));
        }
        if (itemStack.getDurability() != 0) {
            section.set(path + ".damage", itemStack.getDurability());
        }
        Optional.ofNullable(itemStack.getItemMeta()).ifPresent(itemMeta -> {
            if (itemMeta.hasDisplayName()) {
                section.set(path + ".display-name", itemMeta.getDisplayName().replace("§", "&"));
            }
            Optional.ofNullable(itemMeta.getLore()).ifPresent(lore ->
                section.set(path + ".lore", lore.stream().map(s -> s.replace("§", "&")).collect(Collectors.toList()))
            );
            section.set(path + ".flags", itemMeta.getItemFlags().stream()
                .map(Enum::name)
                .collect(Collectors.toList()));
        });
        itemStack.getEnchantments().forEach((enchantment, integer) ->
            section.set(path + ".enchants." + enchantment.getName(), integer)
        );
    }

    @NotNull
    @Override
    public Optional<ItemStack> get(@NotNull final ConfigSection section, @NotNull final String path) {
        final Optional<String> optional = section.getString(path + ".material");
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        final String mtrlstrng = optional.get();
        final Material material;
        if (BukkitItemStackProvider.BUKKIT_VERSION.minor() > 7) {
            final Optional<XMaterial> xmaterialoptional = XMaterial.matchXMaterial(mtrlstrng);
            if (!xmaterialoptional.isPresent()) {
                return Optional.empty();
            }
            final Optional<Material> mtrloptnl = Optional.ofNullable(xmaterialoptional.get().parseMaterial());
            if (!mtrloptnl.isPresent()) {
                return Optional.empty();
            }
            material = mtrloptnl.get();
        } else {
            material = Material.getMaterial(mtrlstrng);
        }
        final int amount = section.getInt(path + ".amount");
        final int fnlamnt;
        if (amount == 0) {
            fnlamnt = 1;
        } else {
            fnlamnt = amount;
        }
        final ItemStack itemStack;
        if (BukkitItemStackProvider.BUKKIT_VERSION.minor() < 13) {
            itemStack = new ItemStack(
                material,
                fnlamnt,
                (short) section.getInt(path + ".damage"),
                (byte) section.getInt(path + ".data")
            );
        } else {
            itemStack = new ItemStack(
                material,
                fnlamnt,
                (short) section.getInt(path + ".damage")
            );
        }
        Optional.ofNullable(itemStack.getItemMeta()).ifPresent(itemMeta -> {
            section.getString(path + ".display-name").ifPresent(s ->
                itemMeta.setDisplayName(
                    ColorUtil.colored(s)
                )
            );
            itemMeta.setLore(
                ColorUtil.colored(
                    section.getStringList(path + ".lore")
                )
            );
            section.getSection(path + ".enchants").map(enchsection ->
                enchsection.getKeys(false)
            ).ifPresent(set ->
                set.forEach(s ->
                    XEnchantment.matchXEnchantment(s).flatMap(xEnchantment ->
                        Optional.ofNullable(xEnchantment.parseEnchantment())
                    ).ifPresent(enchantment ->
                        itemMeta.addEnchant(enchantment, section.getInt(path + ".enchants." + s), true)
                    )
                )
            );
            section.getStringList(path + ".flags").stream()
                .map(ItemFlag::valueOf)
                .forEach(itemMeta::addItemFlags);
            itemStack.setItemMeta(itemMeta);
        });
        return Optional.of(itemStack);
    }

}
