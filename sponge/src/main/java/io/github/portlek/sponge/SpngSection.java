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

package io.github.portlek.sponge;

import io.github.portlek.configs.structure.managed.section.CfgSection;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.item.inventory.ItemStack;

public interface SpngSection extends CfgSection {

    @Override
    @NotNull
    default Supplier<CfgSection> getNewSection() {
        return SpongeSection::new;
    }

    @NotNull
    default Optional<ItemStack> getItemStack() {
        return this.getManaged().getCustomValue(ItemStack.class)
            .flatMap(provided -> provided.get(this, ""));
    }

    default void setItemStack(@NotNull final ItemStack itemstack) {
        this.getManaged().getCustomValue(ItemStack.class)
            .ifPresent(provided -> provided.set(itemstack, this, ""));
    }

    default void setItemStack(@NotNull final String path, @NotNull final ItemStack itemstack) {
        final SpngSection section;
        if (path.isEmpty()) {
            section = this;
        } else {
            section = (SpngSection) this.getOrCreateSection(path);
        }
        section.setItemStack(itemstack);
    }

    @NotNull
    default Optional<ItemStack> getItemStack(@NotNull final String path) {
        final SpngSection section;
        if (path.isEmpty()) {
            section = this;
        } else {
            section = (SpngSection) this.getOrCreateSection(path);
        }
        return section.getItemStack();
    }

}
