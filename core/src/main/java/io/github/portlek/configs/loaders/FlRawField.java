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

package io.github.portlek.configs.loaders;

import io.github.portlek.configs.ConfigHolder;
import io.github.portlek.configs.ConfigLoader;
import io.github.portlek.configs.annotation.Comment;
import io.github.portlek.configs.annotation.Route;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.util.StringList;
import io.github.portlek.reflection.RefField;
import java.lang.reflect.Field;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an implementation to serialize raw fields.
 */
@NoArgsConstructor
@AllArgsConstructor
public final class FlRawField implements FieldLoader {

  /**
   * the raw classes.
   */
  private static final List<Class<?>> RAWS = List.of(String.class, Integer.class, int.class, Boolean.class,
    boolean.class, StringList.class, ConfigHolder.class);

  /**
   * the parent field.
   */
  @Nullable
  private Field parentField;

  /**
   * the parent section.
   */
  @Nullable
  private ConfigurationSection parentSection;

  @Override
  public boolean canLoad(@NotNull final ConfigLoader loader, @NotNull final RefField field) {
    return FlRawField.RAWS.contains(field.getType());
  }

  @Override
  public void onLoad(@NotNull final ConfigLoader loader, @NotNull final RefField field) {
    if (ConfigHolder.class.isAssignableFrom(field.getType())) {
      this.loadSection(loader, field);
    } else {
      this.loadRawField(loader, field);
    }
  }

  /**
   * loads the raw field.
   *
   * @param loader the loader to load.
   * @param field the field to load.
   */
  private void loadRawField(@NotNull final ConfigLoader loader, @NotNull final RefField field) {
    final var path = field.getAnnotation(Route.class)
      .map(Route::value)
      .orElse(field.getName());
    @Nullable final var comment = field.getAnnotation(Comment.class)
      .map(Comment::value)
      .orElse(null);
  }

  /**
   * loads the config holder as a configuration section.
   *
   * @param loader the loader to load.
   * @param field the field to load.
   */
  private void loadSection(@NotNull final ConfigLoader loader, @NotNull final RefField field) {
  }
}
