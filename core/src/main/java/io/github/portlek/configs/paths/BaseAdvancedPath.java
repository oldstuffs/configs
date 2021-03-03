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

import io.github.portlek.configs.ConfigLoader;
import io.github.portlek.configs.serializers.ConfigurationSerializer;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * an class that represents advanced paths.
 *
 * @param <R> type of the raw.
 * @param <F> type of the final.
 */
public final class BaseAdvancedPath<R, F> implements AdvancedPath<R, F> {

  /**
   * the original.
   */
  @NotNull
  private final ConfigPath<F> original;

  /**
   * the serializer.
   */
  @NotNull
  private final ConfigurationSerializer<R, F> serializer;

  /**
   * ctor.
   *
   * @param path the path.
   * @param serializer the serializer.
   */
  public BaseAdvancedPath(@NotNull final String path, @NotNull final ConfigurationSerializer<R, F> serializer) {
    this(new BaseConfigPath<>(path), serializer);
  }

  /**
   * ctor.
   *
   * @param original the original.
   * @param serializer the serializer.
   */
  public BaseAdvancedPath(@NotNull final ConfigPath<F> original,
                          @NotNull final ConfigurationSerializer<R, F> serializer) {
    this.original = original;
    this.serializer = serializer;
  }

  @NotNull
  @Override
  public ConfigLoader getLoader() {
    return this.original.getLoader();
  }

  @Override
  public void setLoader(@NotNull final ConfigLoader loader) {
    this.original.setLoader(loader);
  }

  @NotNull
  @Override
  public String getPath() {
    return this.original.getPath();
  }

  @NotNull
  @Override
  public Optional<F> getValue() {
    return this.serializer.getRaw(this)
      .flatMap(this.serializer::convertToFinal);
  }

  @Override
  public void setValue(@NotNull final F value) {
    this.serializer.convertToRaw(value).ifPresent(r ->
      this.getConfig().set(this.getPath(), r));
  }

  @NotNull
  @Override
  public ConfigurationSerializer<R, F> getSerializer() {
    return this.serializer;
  }
}
