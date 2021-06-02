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

package io.github.portlek.configs.transformer.declaration;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents generic pairs.
 *
 * @param <L> type of the left value.
 * @param <R> type of the right value.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenericPair<L, R> {

  /**
   * the left.
   */
  @NotNull
  private final GenericDeclaration left;

  /**
   * the right.
   */
  @NotNull
  private final GenericDeclaration right;

  /**
   * creates a new generic pair.
   *
   * @param left the left to create.
   * @param right the right to creat.
   * @param <L> type of the left value.
   * @param <R> type of the right value.
   *
   * @return a newly created generic pair.
   */
  @NotNull
  public static <L, R> GenericPair<L, R> of(@NotNull final GenericDeclaration left,
                                            @NotNull final GenericDeclaration right) {
    return new GenericPair<>(left, right);
  }

  /**
   * creates a new generic pair.
   *
   * @param left the left to create.
   * @param right the right to creat.
   * @param <L> type of the left value.
   * @param <R> type of the right value.
   *
   * @return a newly created generic pair.
   */
  @NotNull
  public static <L, R> GenericPair<L, R> of(@NotNull final Class<L> left, @NotNull final Class<R> right) {
    return GenericPair.of(GenericDeclaration.of(left), GenericDeclaration.of(right));
  }

  /**
   * reverses the pair.
   *
   * @return reversed pair.
   */
  @NotNull
  public GenericPair<R, L> reverse() {
    return GenericPair.of(this.right, this.left);
  }
}
