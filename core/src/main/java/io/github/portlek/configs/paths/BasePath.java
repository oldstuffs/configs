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

package io.github.portlek.configs.paths;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.portlek.configs.ConfigLoader;
import io.github.portlek.configs.ConfigPath;
import io.github.portlek.configs.serializers.ConfigurationSerializer;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents base paths.
 *
 * @param <R> type of the raw.
 * @param <F> type of the final.
 */
@RequiredArgsConstructor
public final class BasePath<R, F> implements ConfigPath<R, F> {

  /**
   * the path.
   */
  @NotNull
  @Getter
  private final String path;

  /**
   * the serializer.
   */
  @NotNull
  @Getter
  private final ConfigurationSerializer<R, F> serializer;

  /**
   * the type reference.
   */
  private final TypeReference<F> typeReference = new TypeReference<>() {
  };

  /**
   * the config loader.
   */
  @Nullable
  @Setter
  private ConfigLoader loader;

  @NotNull
  @Override
  public Optional<F> convertToFinal(@NotNull final R raw) {
    return this.getSerializer().convertToFinal(raw);
  }

  @NotNull
  @Override
  public Optional<R> convertToRaw(@NotNull final F fnl) {
    return this.getSerializer().convertToRaw(fnl);
  }

  @NotNull
  @Override
  public ConfigLoader getLoader() {
    return Objects.requireNonNull(this.loader,
      "Use ConfigLoader#load() method before use the getLoader() method!");
  }

  @NotNull
  @Override
  public Optional<R> getRaw() {
    return this.getSerializer().getRaw(this);
  }

  @NotNull
  @Override
  public Optional<F> getValue() {
    final var value = this.getConfig().get(this.getPath());
    try {
      //noinspection unchecked
      final var type = (Class<F>) this.typeReference.getType();
      if (value == null || !type.isAssignableFrom(value.getClass())) {
        return Optional.empty();
      }
      return Optional.of(type.cast(value));
    } catch (final Throwable t) {
      t.printStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public void setValue(@NotNull final F value) {
    this.convertToRaw(value).ifPresent(r ->
      this.getConfig().set(this.getPath(), r));
  }
}
