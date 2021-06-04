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

package io.github.portlek.configs.transformer.generics;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * an interface to determine generic holders.
 *
 * @param <L> type of the left object class.
 * @param <R> type of the right object class.
 */
public interface GenericHolder<L, R> {

  /**
   * creates a simple instance of {@code this}.
   *
   * @param left the left to create.
   * @param right the right to create.
   * @param <L> type of the left object class.
   * @param <R> type of the right object class.
   *
   * @return a newly created {@code this} instance.
   */
  @NotNull
  static <L, R> GenericHolder<L, R> create(@NotNull final Class<L> left, @NotNull final Class<R> right) {
    return new Impl<>(left, right);
  }

  /**
   * obtains the left type.
   *
   * @return left type.
   */
  @NotNull
  Class<L> getLeftType();

  /**
   * creates a new generic pair.
   *
   * @return a newly created generic pair.
   */
  @NotNull
  default GenericPair getPair() {
    return GenericPair.of(this.getLeftType(), this.getRightType());
  }

  /**
   * obtains the right type.
   *
   * @return right type.
   */
  @NotNull
  Class<R> getRightType();

  /**
   * a simple implementation of {@link GenericHolder}.
   *
   * @param <L> type of the left object class.
   * @param <R> type of the right object class.
   */
  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  final class Impl<L, R> implements GenericHolder<L, R> {

    /**
     * the left.
     */
    @NotNull
    private final Class<L> leftType;

    /**
     * the right.
     */
    @NotNull
    private final Class<R> rightType;
  }
}
