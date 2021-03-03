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

package io.github.portlek.configs.paths.comment;

import io.github.portlek.configs.CommentPath;
import io.github.portlek.configs.ConfigPath;
import io.github.portlek.configs.paths.BaseCommentPath;
import io.github.portlek.configs.paths.raw.StringPath;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents string commented path.
 */
@RequiredArgsConstructor
public final class StringCommentPath implements CommentPath<String, String> {

  /**
   * the original.
   */
  @NotNull
  @Delegate
  private final CommentPath<String, String> original;

  /**
   * ctor.
   *
   * @param path the path.
   * @param comment the comment.
   */
  public StringCommentPath(@NotNull final ConfigPath<String, String> path, @NotNull final String comment) {
    this(new BaseCommentPath<>(path, comment));
  }

  /**
   * ctor.
   *
   * @param path the path.
   * @param comment the comment.
   */
  public StringCommentPath(@NotNull final String path, @NotNull final String comment) {
    this(new StringPath(path), comment);
  }
}
