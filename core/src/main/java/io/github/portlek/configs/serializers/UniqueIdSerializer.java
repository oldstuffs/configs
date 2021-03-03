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
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * an implementation for {@link ConfigurationSerializer} of {@link UUID}.
 */
public final class UniqueIdSerializer implements ConfigurationSerializer<String, UUID> {

  @NotNull
  @Override
  public Optional<UUID> convertToFinal(@NotNull final String raw) {
    try {
      return Optional.of(UUID.fromString(raw));
    } catch (final Throwable ignored) {
    }
    return Optional.empty();
  }

  @NotNull
  @Override
  public Optional<String> convertToRaw(@NotNull final UUID fnl) {
    return Optional.of(fnl.toString());
  }

  @NotNull
  @Override
  public Optional<String> getRaw(@NotNull final ConfigPath<String, UUID> path) {
    return Optional.ofNullable(path.getConfig().getString(path.getPath()));
  }
}
