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

import io.github.portlek.configs.Loader;
import io.github.portlek.configs.annotation.Route;
import io.github.portlek.configs.bukkit.data.SentTitle;
import io.github.portlek.configs.loaders.BaseFieldLoader;
import io.github.portlek.reflection.RefField;
import java.util.function.Supplier;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * an implementation to serialize {@link ItemStack}.
 */
public final class FlTitle extends BaseFieldLoader {

  /**
   * the instance.
   */
  public static final Supplier<FlTitle> INSTANCE = FlTitle::new;

  @Override
  public boolean canLoad(final @NotNull Loader loader, @NotNull final RefField field) {
    return SentTitle.class == field.getType();
  }

  @Override
  public void onLoad(final @NotNull Loader loader, @NotNull final RefField field) {
    final var path = field.getAnnotation(Route.class)
      .map(Route::value)
      .orElse(field.getName());
    final var fieldValue = field.getValue();
    final var currentSection = this.getSection(loader);
    var section = currentSection.getConfigurationSection(path);
    if (section == null) {
      section = currentSection.createSection(path);
    }
    final var valueAtPath = SentTitle.deserialize(section);
    if (fieldValue.isPresent()) {
      final var sentTitle = (SentTitle) fieldValue.get();
      if (valueAtPath.isPresent()) {
        field.setValue(valueAtPath.get());
      } else {
        SentTitle.serialize(sentTitle, section);
      }
    } else {
      valueAtPath.ifPresent(field::setValue);
    }
  }
}
