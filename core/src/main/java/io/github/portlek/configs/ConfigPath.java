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

package io.github.portlek.configs;

import io.github.portlek.configs.tree.FileConfiguration;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * the interface to determine paths.
 *
 * @param <T> type of the path's value.
 */
public interface ConfigPath<T> {

  /**
   * obtains the config.
   *
   * @return config.
   */
  @NotNull
  default FileConfiguration getConfig() {
    return this.getLoader().getConfiguration();
  }

  /**
   * obtains the loader.
   *
   * @return loader.
   */
  @NotNull
  ConfigLoader getLoader();

  /**
   * sets the loader.
   *
   * @param loader the loader to set.
   */
  void setLoader(@NotNull ConfigLoader loader);

  /**
   * obtains the path.
   *
   * @return path.
   */
  @NotNull
  String getPath();

  /**
   * obtains the value.
   *
   * @return value.
   */
  @NotNull
  Optional<T> getValue();

  /**
   * sets the value.
   *
   * @param value the value to set.
   */
  void setValue(@NotNull T value);
}
