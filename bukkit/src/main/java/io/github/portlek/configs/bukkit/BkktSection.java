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

package io.github.portlek.configs.bukkit;

import io.github.portlek.configs.structure.managed.section.CfgSection;
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
            .ifPresent(provided -> provided.set(itemstack, section, ""));
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
            .flatMap(provided -> provided.get(section, ""));
    }

}
