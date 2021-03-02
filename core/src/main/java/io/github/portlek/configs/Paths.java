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
import io.github.portlek.configs.paths.BooleanPath;
import io.github.portlek.configs.paths.IntegerPath;
import io.github.portlek.configs.paths.StringListPath;
import io.github.portlek.configs.paths.StringPath;
import java.util.List;
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
   * creates a boolean path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created boolean path.
   */
  @NotNull
  public static BooleanPath bool(@NotNull final String path, final boolean def) {
    return new BooleanPath(path, def);
  }

  /**
   * creates a boolean path.
   *
   * @param path the path to create.
   *
   * @return a newly created boolean path.
   */
  @NotNull
  public static BooleanPath bool(@NotNull final String path) {
    return new BooleanPath(path);
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
  @NotNull
  public static <T> ConfigPath<T> comment(@NotNull final ConfigPath<T> path, @NotNull final String comment) {
    return new BaseCommentPath<>(path);
  }

  /**
   * creates a integer path.
   *
   * @param path the path to create.
   *
   * @return a newly created integer path.
   */
  @NotNull
  public static IntegerPath integer(@NotNull final String path) {
    return new IntegerPath(path);
  }

  /**
   * creates a integer path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created integer path.
   */
  @NotNull
  public static IntegerPath integer(@NotNull final String path, final int def) {
    return new IntegerPath(path, def);
  }

  /**
   * creates a string path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created string path.
   */
  @NotNull
  public static StringPath string(@NotNull final String path, @Nullable final String def) {
    return new StringPath(path, def);
  }

  /**
   * creates a string list path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created string list path.
   */
  @NotNull
  public static StringListPath stringList(@NotNull final String path, @Nullable final List<String> def) {
    return new StringListPath(path, def);
  }

  /**
   * creates a string list path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created string list path.
   */
  @NotNull
  public static StringListPath stringList(@NotNull final String path, @NotNull final String... def) {
    return new StringListPath(path, def);
  }
}
