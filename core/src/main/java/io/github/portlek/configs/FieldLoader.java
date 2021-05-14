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

package io.github.portlek.configs;

import io.github.portlek.configs.annotation.Ignore;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.util.FileVersions;
import io.github.portlek.reflection.RefField;
import io.github.portlek.reflection.clazz.ClassOf;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an interface to determine field loaders.
 */
public interface FieldLoader {

  /**
   * creates field loaders.
   *
   * @param loader the loader to create.
   * @param holder the holder to create.
   * @param functions the functions to create.
   * @param parentField the parent field to create.
   * @param section the section to create.
   *
   * @return a newly created field loaders.
   */
  @NotNull
  static List<FieldLoader> createLoaders(@NotNull final Loader loader, @NotNull final ConfigHolder holder,
                                         @NotNull final List<Func> functions, @Nullable final RefField parentField,
                                         @Nullable final ConfigurationSection section) {
    return functions.stream()
      .map(function -> function.apply(holder, Objects.requireNonNullElse(section, loader.getFileConfiguration())))
      .peek(fieldLoader -> {
        if (parentField != null) {
          fieldLoader.setParentField(parentField);
        }
      })
      .collect(Collectors.toList());
  }

  /**
   * loads the given holder class's fields with the given field loaders.
   *
   * @param loader the loader to load.
   * @param holder the holder to load.
   */
  static void load(@NotNull final Loader loader, @NotNull final ConfigHolder holder) {
    FieldLoader.load(loader, holder, loader.getLoaders(), null, null);
  }

  /**
   * loads the given holder class's fields with the given field loaders.
   *
   * @param loader the loader to load.
   * @param holder the holder to load.
   * @param loaders the loaders to load.
   */
  static void load(@NotNull final Loader loader, @NotNull final ConfigHolder holder,
                   @NotNull final List<Func> loaders) {
    FieldLoader.load(loader, holder, loaders, null, null);
  }

  /**
   * loads the given holder class's fields with the given field suppliers.
   *
   * @param loader the loader to load.
   * @param holder the holder to load.
   * @param parentField the parent field to load.
   * @param section the section to load.
   */
  static void load(@NotNull final Loader loader, @NotNull final ConfigHolder holder,
                   @Nullable final RefField parentField, @Nullable final ConfigurationSection section) {
    FieldLoader.load(loader, holder, loader.getLoaders(), parentField, section);
  }

  /**
   * loads the given holder class's fields with the given field functions.
   *
   * @param loader the loader to load.
   * @param holder the holder to load.
   * @param functions the functions to load.
   * @param parentField the parent field to load.
   * @param section the section to load.
   */
  static void load(@NotNull final Loader loader, @NotNull final ConfigHolder holder,
                   @NotNull final List<Func> functions, @Nullable final RefField parentField,
                   @Nullable final ConfigurationSection section) {
    final var loaders = FieldLoader.createLoaders(loader, holder, functions, parentField, section);
    final var fields = new ClassOf<>(holder).getDeclaredFields().stream()
      .filter(field -> !field.hasAnnotation(Ignore.class))
      .flatMap(field -> loaders.stream()
        .filter(fieldLoader -> fieldLoader.canLoad(loader, field))
        .map(fieldLoader -> Map.entry(field, fieldLoader)))
      .collect(Collectors.toSet());
    FileVersions.load(loader, fields);
    holder.onLoad();
  }

  /**
   * checks if the field can be loaded by the loader.
   *
   * @param loader the loader to check.
   * @param field the field to check.
   *
   * @return {@code true} if the field can load by the reader.
   */
  boolean canLoad(@NotNull Loader loader, @NotNull RefField field);

  /**
   * obtains the parent holder.
   *
   * @return parent holder.
   */
  @NotNull
  ConfigHolder getHolder();

  /**
   * obtains the parent field.
   *
   * @return parent field.
   */
  @Nullable
  RefField getParentField();

  /**
   * sets the parent field.
   *
   * @param parentField the parent field to set.
   */
  void setParentField(@NotNull RefField parentField);

  /**
   * obtains the current section.
   *
   * @return current section.
   */
  @NotNull
  ConfigurationSection getSection();

  /**
   * loads the field value.
   *
   * @param loader the loader to load.
   * @param field the field to load.
   */
  void onLoad(@NotNull Loader loader, @NotNull RefField field);

  /**
   * a functional interface to create field loader instances.
   */
  @FunctionalInterface
  interface Func extends BiFunction<ConfigHolder, ConfigurationSection, FieldLoader> {

  }
}
