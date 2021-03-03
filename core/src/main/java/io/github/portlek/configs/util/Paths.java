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

package io.github.portlek.configs.util;

import io.github.portlek.configs.paths.comment.BooleanCommentPath;
import io.github.portlek.configs.paths.comment.IntegerCommentPath;
import io.github.portlek.configs.paths.comment.LocaleCommentPath;
import io.github.portlek.configs.paths.comment.StringCommentPath;
import io.github.portlek.configs.paths.comment.StringListCommentPath;
import io.github.portlek.configs.paths.comment.UniqueIdCommentPath;
import io.github.portlek.configs.paths.def.BooleanDefaultPath;
import io.github.portlek.configs.paths.def.IntegerDefaultPath;
import io.github.portlek.configs.paths.def.LocaleDefaultPath;
import io.github.portlek.configs.paths.def.StringDefaultPath;
import io.github.portlek.configs.paths.def.StringListDefaultPath;
import io.github.portlek.configs.paths.def.UniqueIdDefaultPath;
import io.github.portlek.configs.paths.path.LocalePath;
import io.github.portlek.configs.paths.path.UniqueIdPath;
import io.github.portlek.configs.paths.raw.BooleanPath;
import io.github.portlek.configs.paths.raw.IntegerPath;
import io.github.portlek.configs.paths.raw.StringListPath;
import io.github.portlek.configs.paths.raw.StringPath;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * a class that contains list of config path implementations.
 */
@UtilityClass
public class Paths {

  /**
   * creates a boolean default path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created boolean default path.
   */
  @NotNull
  public BooleanDefaultPath bool(@NotNull final String path, final boolean def) {
    return new BooleanDefaultPath(path, def);
  }

  /**
   * creates a boolean path.
   *
   * @param path the path to create.
   *
   * @return a newly created boolean path.
   */
  @NotNull
  public BooleanPath bool(@NotNull final String path) {
    return new BooleanPath(path);
  }

  /**
   * creates a boolean commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return boolean commented path.
   */
  @NotNull
  public BooleanCommentPath comment(@NotNull final BooleanDefaultPath path, @NotNull final String comment) {
    return new BooleanCommentPath(path, comment);
  }

  /**
   * creates a boolean commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return boolean commented path.
   */
  @NotNull
  public BooleanCommentPath comment(@NotNull final BooleanPath path, @NotNull final String comment) {
    return new BooleanCommentPath(path, comment);
  }

  /**
   * creates a integer commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return integer commented path.
   */
  @NotNull
  public IntegerCommentPath comment(@NotNull final IntegerPath path, @NotNull final String comment) {
    return new IntegerCommentPath(path, comment);
  }

  /**
   * creates a integer commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return integer commented path.
   */
  @NotNull
  public IntegerCommentPath comment(@NotNull final IntegerDefaultPath path, @NotNull final String comment) {
    return new IntegerCommentPath(path, comment);
  }

  /**
   * creates a locale commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return locale commented path.
   */
  @NotNull
  public LocaleCommentPath comment(@NotNull final LocalePath path, @NotNull final String comment) {
    return new LocaleCommentPath(path, comment);
  }

  /**
   * creates a locale commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return locale commented path.
   */
  @NotNull
  public LocaleCommentPath comment(@NotNull final LocaleDefaultPath path, @NotNull final String comment) {
    return new LocaleCommentPath(path, comment);
  }

  /**
   * creates a string list commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return string list commented path.
   */
  @NotNull
  public StringListCommentPath comment(@NotNull final StringListPath path, @NotNull final String comment) {
    return new StringListCommentPath(path, comment);
  }

  /**
   * creates a string commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return string commented path.
   */
  @NotNull
  public StringListCommentPath comment(@NotNull final StringListDefaultPath path, @NotNull final String comment) {
    return new StringListCommentPath(path, comment);
  }

  /**
   * creates a string commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return string commented path.
   */
  @NotNull
  public StringCommentPath comment(@NotNull final StringPath path, @NotNull final String comment) {
    return new StringCommentPath(path, comment);
  }

  /**
   * creates a string commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return string commented path.
   */
  @NotNull
  public StringCommentPath comment(@NotNull final StringDefaultPath path, @NotNull final String comment) {
    return new StringCommentPath(path, comment);
  }

  /**
   * creates a unique id commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return unique id commented path.
   */
  @NotNull
  public UniqueIdCommentPath comment(@NotNull final UniqueIdPath path, @NotNull final String comment) {
    return new UniqueIdCommentPath(path, comment);
  }

  /**
   * creates a unique id commented path.
   *
   * @param path the path to add.
   * @param comment the comment to add.
   *
   * @return unique id commented path.
   */
  @NotNull
  public UniqueIdCommentPath comment(@NotNull final UniqueIdDefaultPath path, @NotNull final String comment) {
    return new UniqueIdCommentPath(path, comment);
  }

  /**
   * creates a integer default path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created integer default path.
   */
  @NotNull
  public IntegerDefaultPath integer(@NotNull final String path, final int def) {
    return new IntegerDefaultPath(path, def);
  }

  /**
   * creates a integer path.
   *
   * @param path the path to create.
   *
   * @return a newly created integer path.
   */
  @NotNull
  public IntegerPath integer(@NotNull final String path) {
    return new IntegerPath(path);
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
  public LocaleDefaultPath locale(@NotNull final String path, @NotNull final Locale def) {
    return new LocaleDefaultPath(path, def);
  }

  /**
   * creates a locale path.
   *
   * @param path the path to create.
   *
   * @return a newly created locale path.
   */
  @NotNull
  public LocalePath locale(@NotNull final String path) {
    return new LocalePath(path);
  }

  /**
   * creates a string default path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created string default path.
   */
  @NotNull
  public StringDefaultPath string(@NotNull final String path, @NotNull final String def) {
    return new StringDefaultPath(path, def);
  }

  /**
   * creates a string path.
   *
   * @param path the path to create.
   *
   * @return a newly created string path.
   */
  @NotNull
  public StringPath string(@NotNull final String path) {
    return new StringPath(path);
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
  public StringListDefaultPath stringList(@NotNull final String path, @NotNull final List<String> def) {
    return new StringListDefaultPath(path, def);
  }

  /**
   * creates a string list path.
   *
   * @param path the path to create.
   *
   * @return a newly created string list path.
   */
  @NotNull
  public StringListPath stringList(@NotNull final String path) {
    return new StringListPath(path);
  }

  /**
   * creates a unique id default path.
   *
   * @param path the path to create.
   * @param def the default value to create.
   *
   * @return a newly created unique id default path.
   */
  @NotNull
  public UniqueIdDefaultPath uniqueId(@NotNull final String path, @NotNull final UUID def) {
    return new UniqueIdDefaultPath(path, def);
  }

  /**
   * creates a boolean path.
   *
   * @param path the path to create.
   *
   * @return a newly created unique id path.
   */
  @NotNull
  public UniqueIdPath uniqueId(@NotNull final String path) {
    return new UniqueIdPath(path);
  }
}
