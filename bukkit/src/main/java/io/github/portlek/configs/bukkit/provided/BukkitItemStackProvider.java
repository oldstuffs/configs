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

package io.github.portlek.configs.bukkit.provided;

import io.github.portlek.bukkititembuilder.util.ItemStackUtil;
import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.Provided;
import io.github.portlek.configs.util.GeneralUtilities;
import java.util.Map;
import java.util.Optional;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class BukkitItemStackProvider implements Provided<ItemStack> {

  @NotNull
  @Override
  public Optional<ItemStack> get(@NotNull final CfgSection section, @NotNull final String path) {
    final String fnlpath = GeneralUtilities.putDot(path);
    if (!fnlpath.isEmpty()) {
      return ItemStackUtil.from(
        section.getOrCreateSection(fnlpath.substring(0, fnlpath.length() - 1))
          .getConfigurationSection().getValues(false));
    }
    return ItemStackUtil.from(section.getConfigurationSection().getValues(false));
  }

  @Override
  public void set(@NotNull final ItemStack itemStack, @NotNull final CfgSection section,
                  @NotNull final String path) {
    final String fnlpath = GeneralUtilities.putDot(path);
    if (!fnlpath.isEmpty()) {
      this.to(section.getOrCreateSection(
        fnlpath.substring(0, fnlpath.length() - 1)),
        ItemStackUtil.to(itemStack));
      return;
    }
    this.to(section, ItemStackUtil.to(itemStack));
  }

  private void to(@NotNull final CfgSection section, @NotNull final Map<String, Object> map) {
    map.forEach((key, value) -> {
      if (value instanceof Map<?, ?>) {
        this.to(section.createSection(key), (Map<String, Object>) value);
      } else {
        section.set(key, value);
      }
    });
  }
}
