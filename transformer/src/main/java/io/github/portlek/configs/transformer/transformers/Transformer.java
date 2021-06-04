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

package io.github.portlek.configs.transformer.transformers;

import io.github.portlek.configs.transformer.generics.GenericHolder;
import java.util.Optional;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an interface to determine transformers.
 *
 * @param <R> type of the raw value.
 * @param <F> type of the final value.
 */
public interface Transformer<R, F> extends Function<@NotNull R, @NotNull Optional<F>>, GenericHolder<R, F> {

  /**
   * creates a simple transformer.
   *
   * @param rawType the raw type to create.
   * @param finalType the final type to create.
   * @param transformation the transformation to create.
   * @param <R> type of the raw value.
   * @param <F> type of the final value.
   *
   * @return a newly created transformer.
   */
  @NotNull
  static <R, F> Transformer<R, F> create(@NotNull final Class<R> rawType, @NotNull final Class<F> finalType,
                                         @NotNull final Function<@NotNull R, @Nullable F> transformation) {
    return new Impl<>(rawType, finalType, transformation);
  }

  /**
   * an abstract that envelopes to {@link Transformer}.
   *
   * @param <R> type of the raw value.
   * @param <F> type of the final value.
   */
  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  abstract class Base<R, F> implements Transformer<R, F> {

    /**
     * the holder.
     */
    @NotNull
    @Delegate
    private final GenericHolder<R, F> holder;

    /**
     * the transformation.
     */
    @NotNull
    private final Function<@NotNull R, @Nullable F> transformation;

    /**
     * ctor.
     *
     * @param rawType the raw type to create.
     * @param finalType the final type to create.
     * @param transformation the transformation.
     */
    protected Base(@NotNull final Class<R> rawType, @NotNull final Class<F> finalType,
                   @NotNull final Function<@NotNull R, @Nullable F> transformation) {
      this(GenericHolder.create(rawType, finalType), transformation);
    }

    @NotNull
    @Override
    public final Optional<F> apply(@NotNull final R r) {
      return Optional.ofNullable(this.transformation.apply(r));
    }
  }

  /**
   * a simple implementation of {@link Transformer}.
   *
   * @param <R> type of the raw value.
   * @param <F> type of the final value.
   */
  @Getter
  final class Impl<R, F> extends Base<R, F> {

    /**
     * ctor.
     *
     * @param rawType the raw type to create.
     * @param finalType the final type to create.
     * @param transformation the transformation.
     */
    protected Impl(@NotNull final Class<R> rawType, @NotNull final Class<F> finalType,
                   @NotNull final Function<@NotNull R, @Nullable F> transformation) {
      super(GenericHolder.create(rawType, finalType), transformation);
    }
  }
}
