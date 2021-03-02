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

import io.github.portlek.configs.paths.BaseCommentPath;
import io.github.portlek.configs.paths.BaseDefaultPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that contains list of config path implementations.
 */
public final class Paths {

  /**
   * ctor.
   */
  private Paths() {
  }

  /**
   * adds comment to the path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   * @param <T> type of the path.
   *
   * @return commented path.
   */
  public static <T> ConfigPath<T> comment(@NotNull final ConfigPath<T> path, @NotNull final String comment) {
    return new BaseCommentPath<>(path);
  }

  /**
   * creates a string path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created string path.
   */
  public static DefaultPath<String> string(@NotNull final String path, @Nullable final String def) {
    return new BaseDefaultPath<>(path, def) {
    };
  }
}
