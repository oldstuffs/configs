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

package io.github.portlek.configs.serializers;

import io.github.portlek.configs.ConfigPath;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * an implementation for {@link ConfigurationSerializer} of {@link Locale}.
 */
public final class LocaleSerializer implements ConfigurationSerializer<String, Locale> {

  @NotNull
  @Override
  public Optional<Locale> convertToFinal(@NotNull final String raw) {
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

  @NotNull
  @Override
  public Optional<String> convertToRaw(@NotNull final Locale fnl) {
    return Optional.of(fnl.getLanguage() + "_" + fnl.getCountry());
  }

  @NotNull
  @Override
  public Optional<String> getRaw(@NotNull final ConfigPath<String, Locale> path) {
    return Optional.ofNullable(path.getConfig().getString(path.getPath()));
  }
}
