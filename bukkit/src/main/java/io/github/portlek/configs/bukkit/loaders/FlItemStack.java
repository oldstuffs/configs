/*
 * MIT License
 *
 * Copyright (c) 2021 Hasan Demirtaş
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

package io.github.portlek.configs.bukkit.loaders;

import io.github.portlek.bukkititembuilder.util.ItemStackUtil;
import io.github.portlek.configs.loaders.SectionFieldLoader;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an implementation to serialize {@link ItemStack}.
 */
public final class FlItemStack extends SectionFieldLoader<ItemStack> {

  /**
   * the instance.
   */
  public static final Supplier<FlItemStack> INSTANCE = FlItemStack::new;

  @NotNull
  @Override
  public Optional<ItemStack> toFinal(@NotNull final Map<String, Object> rawValue,
                                     @Nullable final ItemStack fieldValue) {
    return ItemStackUtil.from(rawValue);
  }

  @NotNull
  @Override
  public Optional<Map<String, Object>> toRaw(@NotNull final ItemStack finalValue) {
    return Optional.of(ItemStackUtil.to(finalValue));
  }
}
