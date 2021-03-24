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

import io.github.portlek.configs.Loader;
import io.github.portlek.configs.annotation.Route;
import io.github.portlek.reflection.RefField;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an implementation to load {@link Locale}.
 */
public final class FlLocale extends BaseFieldLoader {

  /**
   * the instance.
   */
  public static final Supplier<FlLocale> INSTANCE = FlLocale::new;

  /**
   * converts the given raw string to a {@link Locale}.
   *
   * @param raw the raw to convert.
   *
   * @return converted locale from string.
   */
  @NotNull
  private static Optional<Locale> convertToLocale(@Nullable final String raw) {
    if (raw == null) {
      return Optional.empty();
    }
    final var trim = raw.trim();
    final var strings = trim.split("_");
    if (trim.contains("_") && strings.length != 2) {
      return Optional.of(Locale.ROOT);
    }
    if (strings.length != 2) {
      return Optional.empty();
    }
    return Optional.of(new Locale(strings[0], strings[1]));
  }

  @Override
  public boolean canLoad(@NotNull final Loader loader, @NotNull final RefField field) {
    return Locale.class == field.getType();
  }

  @Override
  public void onLoad(@NotNull final Loader loader, @NotNull final RefField field) {
    final var path = field.getAnnotation(Route.class)
      .map(Route::value)
      .orElse(field.getName());
    final var fieldValue = field.getValue();
    final var section = this.getSection(loader);
    final var valueAtPath = FlLocale.convertToLocale(section.getString(path));
    if (fieldValue.isPresent()) {
      final var locale = (Locale) fieldValue.get();
      if (valueAtPath.isPresent()) {
        field.setValue(valueAtPath.get());
      } else {
        section.set(path, locale.getLanguage() + "_" + locale.getCountry());
      }
    } else {
      valueAtPath.ifPresent(field::setValue);
    }
  }
}
