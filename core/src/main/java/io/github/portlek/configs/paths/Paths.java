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

import io.github.portlek.configs.util.Serializers;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

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
  public static DefaultPath<Boolean> bool(@NotNull final String path, final boolean def) {
    return new BaseDefaultPath<>(def, new BasePath<>(path));
  }

  /**
   * creates a boolean path.
   *
   * @param path the path to create.
   *
   * @return a newly created boolean path.
   */
  @NotNull
  public static ConfigPath<Boolean> bool(@NotNull final String path) {
    return new BasePath<>(path);
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
  public static <T> CommentPath<T> comment(@NotNull final ConfigPath<T> path, @NotNull final String comment) {
    return new BaseCommentPath<>(path, comment);
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
  public static DefaultPath<Integer> integer(@NotNull final String path, final int def) {
    return new BaseDefaultPath<>(def, new BasePath<>(path));
  }

  /**
   * creates a integer path.
   *
   * @param path the path to create.
   *
   * @return a newly created integer path.
   */
  @NotNull
  public static ConfigPath<Integer> integer(@NotNull final String path) {
    return new BasePath<>(path);
  }

  /**
   * creates a locale path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created locale path.
   */
  @NotNull
  public static DefaultPath<Locale> locale(@NotNull final String path, @NotNull final Locale def) {
    return new BaseDefaultPath<>(def, new BaseAdvancedPath<>(path, Serializers.LOCALE_SERIALIZER));
  }

  /**
   * creates a locale path.
   *
   * @param path the path to create.
   *
   * @return a newly created locale path.
   */
  @NotNull
  public static ConfigPath<Locale> locale(@NotNull final String path) {
    return new BaseAdvancedPath<>(path, Serializers.LOCALE_SERIALIZER);
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
  public static DefaultPath<String> string(@NotNull final String path, @NotNull final String def) {
    return new BaseDefaultPath<>(def, new BasePath<>(path));
  }

  /**
   * creates a string path.
   *
   * @param path the path to create.
   *
   * @return a newly created string path.
   */
  @NotNull
  public static ConfigPath<String> string(@NotNull final String path) {
    return new BasePath<>(path);
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
  public static DefaultPath<List<String>> stringList(@NotNull final String path, @NotNull final List<String> def) {
    return new BaseDefaultPath<>(def, new BasePath<>(path));
  }

  /**
   * creates a string list path.
   *
   * @param path the path to create.
   *
   * @return a newly created string list path.
   */
  @NotNull
  public static ConfigPath<List<String>> stringList(@NotNull final String path) {
    return new BasePath<>(path);
  }
}
