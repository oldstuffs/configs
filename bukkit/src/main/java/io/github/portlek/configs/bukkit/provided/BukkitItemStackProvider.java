///*
// * MIT License
// *
// * Copyright (c) 2020 Hasan Demirtaş
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in all
// * copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// * SOFTWARE.
// *
// */
//
//package io.github.portlek.configs.bukkit.provided;
//
//import com.cryptomorin.xseries.XEnchantment;
//import com.cryptomorin.xseries.XMaterial;
//import io.github.portlek.configs.bukkit.util.BukkitVersion;
//import io.github.portlek.configs.bukkit.util.ColorUtil;
//import io.github.portlek.configs.provided.Provided;
//import io.github.portlek.configs.oldstructure.managed.section.CfgSection;
//import io.github.portlek.configs.util.GeneralUtilities;
//import java.util.Optional;
//import java.util.stream.Collectors;
//import org.bukkit.Material;
//import org.bukkit.inventory.ItemFlag;
//import org.bukkit.inventory.ItemStack;
//import org.jetbrains.annotations.NotNull;
//
//public final class BukkitItemStackProvider implements Provided<ItemStack> {
//
//    private static final BukkitVersion BUKKIT_VERSION = new BukkitVersion();
//
//    @Override
//    public void set(@NotNull final ItemStack itemStack, @NotNull final CfgSection section,
//                    @NotNull final String path) {
//        final String fnlpath = GeneralUtilities.putDot(path);
//        section.set(fnlpath + "material", itemStack.getType().name());
//        section.set(fnlpath + "amount", itemStack.getAmount());
//        if (BukkitItemStackProvider.BUKKIT_VERSION.minor() < 13) {
//            Optional.ofNullable(itemStack.getData()).ifPresent(materialData ->
//                section.set(fnlpath + "data", (int) materialData.getData()));
//        }
//        if ((int) itemStack.getDurability() != 0) {
//            section.set(fnlpath + "damage", itemStack.getDurability());
//        }
//        Optional.ofNullable(itemStack.getItemMeta()).ifPresent(itemMeta -> {
//            if (itemMeta.hasDisplayName()) {
//                section.set(
//                    fnlpath + "display-name",
//                    itemMeta.getDisplayName().replace("§", "&")
//                );
//            }
//            Optional.ofNullable(itemMeta.getLore()).ifPresent(lore ->
//                section.set(
//                    fnlpath + "lore",
//                    lore.stream()
//                        .map(s -> s.replace("§", "&"))
//                        .collect(Collectors.toList())
//                )
//            );
//            section.set(fnlpath + "flags", itemMeta.getItemFlags().stream()
//                .map(Enum::name)
//                .collect(Collectors.toList()));
//        });
//        itemStack.getEnchantments().forEach((enchantment, integer) ->
//            section.set(fnlpath + "enchants." + enchantment.getName(), integer)
//        );
//    }
//
//    @NotNull
//    @Override
//    public Optional<ItemStack> get(@NotNull final CfgSection section, @NotNull final String path) {
//        final String fnlpath = GeneralUtilities.putDot(path);
//        final Optional<String> optional = section.getString(fnlpath + "material");
//        if (!optional.isPresent()) {
//            return Optional.empty();
//        }
//        final String mtrlstrng = optional.get();
//        final Material material;
//        if (BukkitItemStackProvider.BUKKIT_VERSION.minor() > 7) {
//            final Optional<XMaterial> xmaterialoptional = XMaterial.matchXMaterial(mtrlstrng);
//            if (!xmaterialoptional.isPresent()) {
//                return Optional.empty();
//            }
//            final Optional<Material> mtrloptnl = Optional.ofNullable(xmaterialoptional.get().parseMaterial());
//            if (!mtrloptnl.isPresent()) {
//                return Optional.empty();
//            }
//            material = mtrloptnl.get();
//        } else {
//            material = Material.getMaterial(mtrlstrng);
//        }
//        final int fnlamnt = section.getInteger(fnlpath + "amount").orElse(1);
//        final ItemStack itemStack;
//        if (BukkitItemStackProvider.BUKKIT_VERSION.minor() < 13) {
//            itemStack = new ItemStack(
//                material,
//                fnlamnt,
//                section.getInteger(fnlpath + "damage").orElse(0).shortValue(),
//                section.getInteger(fnlpath + "data").orElse(0).byteValue()
//            );
//        } else {
//            itemStack = new ItemStack(
//                material,
//                fnlamnt,
//                section.getInteger(fnlpath + "damage").orElse(0).shortValue()
//            );
//        }
//        Optional.ofNullable(itemStack.getItemMeta()).ifPresent(itemMeta -> {
//            section.getString(fnlpath + "display-name").ifPresent(s ->
//                itemMeta.setDisplayName(ColorUtil.colored(s))
//            );
//            section.getStringList(fnlpath + "lore").ifPresent(lore ->
//                itemMeta.setLore(lore.stream()
//                    .map(ColorUtil::colored)
//                    .collect(Collectors.toList())));
//            section.getSection(fnlpath + "enchants").map(enchsection ->
//                enchsection.getKeys(false)
//            ).ifPresent(set ->
//                set.forEach(s ->
//                    XEnchantment.matchXEnchantment(s).flatMap(xEnchantment ->
//                        Optional.ofNullable(xEnchantment.parseEnchantment())
//                    ).ifPresent(enchantment ->
//                        itemMeta.addEnchant(
//                            enchantment,
//                            section.getInteger(fnlpath + "enchants." + s).orElse(1), true))));
//            section.getStringList(fnlpath + "flags").ifPresent(flags ->
//                flags.stream()
//                    .map(ItemFlag::valueOf)
//                    .forEach(itemMeta::addItemFlags));
//            itemStack.setItemMeta(itemMeta);
//        });
//        return Optional.of(itemStack);
//    }
//
//}
