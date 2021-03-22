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
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.reflection.RefField;
import io.github.portlek.reflection.clazz.ClassOf;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an interface to determine field loaders.
 */
public interface FieldLoader {

  /**
   * creates field loaders from the given suppliers when it created sets the given parent field and section into it.
   *
   * @param parentHolder the parent holder to create.
   * @param suppliers the suppliers to create.
   * @param parentField the parent field to create.
   * @param section the section to create.
   *
   * @return a newly created field loaders.
   */
  @NotNull
  static List<FieldLoader> createLoaders(@NotNull final ConfigHolder parentHolder,
                                         @NotNull final List<Supplier<? extends FieldLoader>> suppliers,
                                         @Nullable final RefField parentField,
                                         @Nullable final ConfigurationSection section) {
    return suppliers.stream()
      .map(Supplier::get)
      .peek(loader -> {
        loader.setParentHolder(parentHolder);
        if (section != null) {
          loader.setSection(section);
        }
        if (parentField != null) {
          loader.setParentField(parentField);
        }
      })
      .collect(Collectors.toList());
  }

  /**
   * loads the given holder class's fields with the given field loaders.
   *
   * @param configLoader the config loader to load.
   * @param holder the holder to load.
   * @param loaders the loaders to load.
   */
  static void load(@NotNull final ConfigLoader configLoader, @NotNull final ConfigHolder holder,
                   @NotNull final List<Supplier<? extends FieldLoader>> loaders) {
    FieldLoader.load(configLoader, holder, loaders, null, null);
  }

  /**
   * loads the given holder class's fields with the given field suppliers.
   *
   * @param configLoader the config loader to load.
   * @param holder the holder to load.
   * @param suppliers the suppliers to load.
   * @param parentField the parent field to load.
   * @param section the section to load.
   */
  static void load(@NotNull final ConfigLoader configLoader, @NotNull final ConfigHolder holder,
                   @NotNull final List<Supplier<? extends FieldLoader>> suppliers, @Nullable final RefField parentField,
                   @Nullable final ConfigurationSection section) {
    final var loaders = FieldLoader.createLoaders(holder, suppliers, parentField, section);
    new ClassOf<>(holder).getDeclaredFields().forEach(field -> loaders.stream()
      .filter(loader -> loader.canLoad(configLoader, field))
      .findFirst()
      .ifPresent(loader -> loader.onLoad(configLoader, field)));
  }

  /**
   * checks if the field can be loaded by the loader.
   *
   * @param loader the loader to check.
   * @param field the field to check.
   *
   * @return {@code true} if the field can load by the reader.
   */
  boolean canLoad(@NotNull ConfigLoader loader, @NotNull RefField field);

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
   * obtains the parent holder.
   *
   * @return parent holder.
   */
  @Nullable
  ConfigHolder getParentHolder();

  /**
   * sets the parent holder.
   *
   * @param parentHolder the parent holder to set.
   */
  void setParentHolder(@NotNull ConfigHolder parentHolder);

  /**
   * obtains the current section.
   *
   * @return current section.
   */
  @Nullable
  ConfigurationSection getSection();

  /**
   * sets the section.
   *
   * @param section the section to set.
   */
  void setSection(@NotNull ConfigurationSection section);

  /**
   * loads the field value.
   *
   * @param loader the loader to load.
   * @param field the field to load.
   */
  void onLoad(@NotNull ConfigLoader loader, @NotNull RefField field);
}
