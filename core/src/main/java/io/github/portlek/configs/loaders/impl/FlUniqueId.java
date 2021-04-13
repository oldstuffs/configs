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

import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.loaders.GenericFieldLoader;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an implementation to load {@link Locale}.
 */
public final class FlUniqueId extends GenericFieldLoader<String, UUID> {

  /**
   * the instance.
   */
  public static final Supplier<FlUniqueId> INSTANCE = FlUniqueId::new;

  /**
   * converts the given raw string to a {@link UUID}.
   *
   * @param raw the raw to convert.
   *
   * @return converted unique id from string.
   */
  @NotNull
  private static Optional<UUID> convertToUniqueId(@Nullable final String raw) {
    if (raw == null) {
      return Optional.empty();
    }
    try {
      return Optional.of(UUID.fromString(raw));
    } catch (final Throwable ignored) {
    }
    return Optional.empty();
  }

  @NotNull
  @Override
  public Optional<String> toConfigObject(@NotNull final ConfigurationSection section, @NotNull final String path) {
    return Optional.ofNullable(section.getString(path));
  }

  @NotNull
  @Override
  public Optional<UUID> toFinal(@NotNull final String rawValue) {
    return FlUniqueId.convertToUniqueId(rawValue);
  }

  @NotNull
  @Override
  public Optional<String> toRaw(final @NotNull UUID finalValue) {
    return Optional.ofNullable(finalValue.toString());
  }
}
