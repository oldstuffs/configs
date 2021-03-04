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

import io.github.portlek.configs.ConfigLoader;
import io.github.portlek.configs.annotation.Route;
import io.github.portlek.reflection.RefField;
import java.util.List;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

/**
 * an implementation to serialize raw fields.
 */
@SuppressWarnings("rawtypes")
public final class FlRawField extends BaseFileLoader {

  /**
   * the instance.
   */
  public static final Supplier<FlConfigHolder> INSTANCE = FlConfigHolder::new;

  /**
   * the generic classes.
   */
  private static final List<Class<?>> GENERICS = List.of(List.class);

  /**
   * the raw classes.
   */
  private static final List<Class<?>> RAWS = List.of(String.class, Integer.class, int.class, Boolean.class,
    boolean.class);

  @Override
  public boolean canLoad(@NotNull final ConfigLoader loader, @NotNull final RefField field) {
    return FlRawField.RAWS.contains(field.getType()) ||
      FlRawField.GENERICS.contains(field.getType());
  }

  @Override
  public void onLoad(@NotNull final ConfigLoader loader, @NotNull final RefField field) {
    final var path = field.getAnnotation(Route.class)
      .map(Route::value)
      .orElse(field.getName());
    final var value = field.getValue();
    final var section = this.getSection(loader);
    final var valueAtPath = section.get(path);
    if (value.isPresent() && valueAtPath == null) {
      section.set(path, value);
      return;
    }
    if (value.isPresent()) {
      return;
    }
    if (valueAtPath == null) {
      return;
    }
    if (!FlRawField.GENERICS.contains(field.getType())) {
      field.setValue(valueAtPath);
      return;
    }
    if (valueAtPath instanceof List) {
      final var list = (List) valueAtPath;
      if (!list.isEmpty()) {
        final var listObject = list.get(0);
        if (FlRawField.RAWS.contains(listObject.getClass())) {

        }
      }
    }
  }
}
