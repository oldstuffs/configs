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

import io.github.portlek.configs.tree.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * the interface to determine paths.
 *
 * @param <T> type of the path's value
 */
public interface Pth<T> {

  /**
   * adds comment to the path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   * @param <T> type of the path.
   *
   * @return commented path.
   */
  static <T> Pth<T> comment(@NotNull final Pth<T> path, @NotNull final String comment) {
    return path;
  }

  /**
   * creates a string path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created string path.
   */
  static Pth<String> string(@NotNull final String path, @Nullable final String def) {
    return new DefaultPath<>(path, def) {
    };
  }

  /**
   * obtains the config.
   *
   * @return config.
   */
  @Nullable
  FileConfiguration getConfig();

  /**
   * sets the config.
   *
   * @param config the config to set.
   */
  void setConfig(@NotNull FileConfiguration config);

  /**
   * obtains the path.
   *
   * @return path.
   */
  @NotNull
  String getPath();
}
