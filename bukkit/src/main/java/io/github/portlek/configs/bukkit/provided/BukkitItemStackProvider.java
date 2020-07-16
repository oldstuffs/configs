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

package io.github.portlek.configs.bukkit.provided;

import io.github.portlek.bukkititembuilder.util.ItemStackUtil;
import io.github.portlek.bukkitversion.BukkitVersion;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.provided.Provided;
import io.github.portlek.configs.structure.section.CfgSection;
import io.github.portlek.configs.util.GeneralUtilities;
import java.util.Map;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class BukkitItemStackProvider implements Provided<ItemStack> {

    private static final int VERSION = new BukkitVersion()
        .minor();

    @Override
    public void set(@NotNull final ItemStack itemStack, @NotNull final CfgSection section,
                    @NotNull final String path) {
        final String fnlpath = GeneralUtilities.putDot(path);
        this.to(section.getOrCreateSection(fnlpath.substring(0, fnlpath.length() - 1))
                .getConfigurationSection(),
            itemStack);
    }

    @NotNull
    @Override
    public Optional<ItemStack> get(@NotNull final CfgSection section, @NotNull final String path) {
        final String fnlpath = GeneralUtilities.putDot(path);
        return ItemStackUtil.from(
            section.getOrCreateSection(fnlpath.substring(0, fnlpath.length() - 1))
                .getConfigurationSection().getValues(false));
    }

    private void to(@NotNull final ConfigurationSection section, @NotNull final ItemStack itemStack) {
        final Map<String, Object> map = ItemStackUtil.to(itemStack);
        map.forEach((key, value) -> {
            if (value instanceof Map<?, ?>) {
                this.to(section.createSection(key), itemStack);
            } else {
                section.set(key, value);
            }
        });
    }

}
