/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.github.portlek.sponge.provided;

import io.github.portlek.configs.provided.Provided;
import io.github.portlek.configs.structure.managed.section.CfgSection;
import io.github.portlek.configs.util.GeneralUtilities;
import io.github.portlek.sponge.util.ColorUtil;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public final class SpongeItemStackProvider implements Provided<ItemStack> {

    @Override
    public void set(@NotNull final ItemStack itemStack, @NotNull final CfgSection section,
                    @NotNull final String path) {
        final String fnlpath = GeneralUtilities.putDot(path);
        section.set(fnlpath + "type", itemStack.getType().getName());
        section.set(fnlpath + "amount", itemStack.getQuantity());
        itemStack.get(Keys.DISPLAY_NAME)
            .map(ColorUtil::serialize)
            .ifPresent(s ->
                section.set(fnlpath + "display-name", s));
        itemStack.get(Keys.ITEM_LORE).ifPresent(lore ->
            section.set(
                fnlpath + "lore",
                lore.stream()
                    .map(text -> text.replace("§", Text.of("&")))
                    .collect(Collectors.toList())));
        itemStack.get(Keys.ITEM_ENCHANTMENTS).ifPresent(enchantments -> {
            final CfgSection enchSection = section.getOrCreateSection(fnlpath + "enchants");
            enchantments.forEach(enchantment ->
                enchSection.set(enchantment.getType().getName(), enchantment.getLevel()));
        });
    }

    @SneakyThrows
    @NotNull
    @Override
    public Optional<ItemStack> get(@NotNull final CfgSection section, @NotNull final String path) {
        final String fnlpath = GeneralUtilities.putDot(path);
        final Optional<String> optional = section.getString(fnlpath + "type");
        if (!optional.isPresent()) {
            return Optional.empty();
        }
        final String typeString = optional.get();
        final ItemType type = (ItemType) ItemTypes.class.getField(typeString).get(null);
        final ItemStack itemStack = ItemStack.of(type);
        itemStack.setQuantity(1);
        section.getInteger("amount")
            .ifPresent(itemStack::setQuantity);
        section.getString("display-name")
            .map(ColorUtil::colored)
            .ifPresent(text -> itemStack.offer(Keys.DISPLAY_NAME, text));
        section.getStringList("lore").ifPresent(lore ->
            itemStack.offer(Keys.ITEM_LORE, lore.stream()
                .map(ColorUtil::colored)
                .collect(Collectors.toList())));
        section.getSection("enchants").ifPresent(enchSection ->
            itemStack.offer(Keys.ITEM_ENCHANTMENTS, enchSection.getKeys(false).stream()
                .filter(s -> enchSection.getInteger(s).isPresent())
                .map(s ->
                    Enchantment.of(
                        (EnchantmentType) EnchantmentTypes.class.getField(s).get(null),
                        enchSection.getInteger(s).get()))
                .collect(Collectors.toList()))
        );
        return Optional.of(itemStack);
    }

}
