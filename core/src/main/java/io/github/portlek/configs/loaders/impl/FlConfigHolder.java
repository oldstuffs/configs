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

package io.github.portlek.configs.loaders.impl;

import io.github.portlek.configs.ConfigHolder;
import io.github.portlek.configs.FieldLoader;
import io.github.portlek.configs.Loader;
import io.github.portlek.configs.annotation.Route;
import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.loaders.BaseFieldLoader;
import io.github.portlek.reflection.RefField;
import io.github.portlek.reflection.clazz.ClassOf;
import org.jetbrains.annotations.NotNull;

/**
 * an implementation to load {@link ConfigHolder}.
 */
public final class FlConfigHolder extends BaseFieldLoader {

  /**
   * the instance.
   */
  public static final FieldLoader.Func INSTANCE = FlConfigHolder::new;

  /**
   * ctor.
   *
   * @param holder the holder.
   * @param section the section.
   */
  private FlConfigHolder(@NotNull final ConfigHolder holder, @NotNull final ConfigurationSection section) {
    super(holder, section);
  }

  @Override
  public boolean canLoad(@NotNull final Loader loader, @NotNull final RefField field) {
    return ConfigHolder.class.isAssignableFrom(field.getType());
  }

  @Override
  public void onLoad(@NotNull final Loader loader, @NotNull final RefField field) {
    field.of(this.getHolder()).getValue()
      .filter(ConfigHolder.class::isInstance)
      .map(ConfigHolder.class::cast)
      .ifPresent(configHolder ->
        FieldLoader.load(loader, configHolder,
          this.getSection().getSectionOrCreate(field.getAnnotation(Route.class)
            .map(Route::value)
            .orElseGet(() -> new ClassOf<>(field.getType()).getAnnotation(Route.class)
              .map(Route::value)
              .orElse(field.getName())))));
  }
}
