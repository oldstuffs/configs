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
import io.github.portlek.configs.ConfigPath;
import io.github.portlek.configs.DefaultPath;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents default paths.
 *
 * @param <R> type of the raw.
 * @param <F> type of the final.
 */
@RequiredArgsConstructor
public final class BaseDefaultPath<R, F> implements DefaultPath<R, F> {

  /**
   * the default value.
   */
  @NotNull
  private final F def;

  /**
   * the original.
   */
  @NotNull
  @Delegate(excludes = Exclusions.class)
  private final ConfigPath<R, F> original;

  /**
   * obtains the default.
   *
   * @return default.
   */
  @NotNull
  @Override
  public F getDefault() {
    return this.def;
  }

  /**
   * obtains the value, if it's null returns the default value.
   *
   * @return value or default value.
   */
  @NotNull
  @Override
  public F getValueOrDefault() {
    return this.original.getValue().orElse(this.getDefault());
  }

  @Override
  public void setLoader(@NotNull final ConfigLoader loader) {
    this.original.setLoader(loader);
    this.original.convertToRaw(this.getDefault()).ifPresent(r ->
      this.getConfig().addDefault(this.getPath(), r));
  }

  @NotNull
  @Override
  public Optional<F> getValue() {
    return Optional.of(this.getValueOrDefault());
  }

  /**
   * an interface to determine delegate exclusions for default path.
   */
  private final class Exclusions {

    /**
     * obtains the value.
     *
     * @return value.
     */
    public Optional<F> getValue() {
      return Optional.empty();
    }

    /**
     * sets the loader.
     *
     * @param loader the loader to set.
     */
    public void setLoader(@NotNull final ConfigLoader loader) {
    }
  }
}
