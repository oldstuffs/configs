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
public interface TwoSideTransformer<R, F> extends GenericHolder<R, F> {

  /**
   * creates a simple transformer.
   *
   * @param rawType the raw type to create.
   * @param finalType the final type to create.
   * @param toRaw the to raw to create.
   * @param toFinal the to final to create.
   * @param <R> type of the raw value.
   * @param <F> type of the final value.
   *
   * @return a newly created transformer.
   */
  @NotNull
  static <R, F> TwoSideTransformer<R, F> create(@NotNull final Class<R> rawType, @NotNull final Class<F> finalType,
                                                @NotNull final Function<@NotNull F, @Nullable R> toRaw,
                                                @NotNull final Function<@NotNull R, @Nullable F> toFinal) {
    return new Impl<>(finalType, rawType, toRaw, toFinal);
  }

  /**
   * reverses the two side transformer.
   *
   * @return reversed two side transformer.
   */
  @NotNull
  default TwoSideTransformer<F, R> reverse() {
    return TwoSideTransformer.create(this.getRightType(), this.getLeftType(), this::toFinalOrNull, this::toRawOrNull);
  }

  /**
   * converts the {@link R} into {@link F}.
   *
   * @param r the {@link R} to convert.
   *
   * @return {@link F} value.
   */
  @NotNull
  Optional<F> toFinal(@NotNull R r);

  /**
   * converts the {@link R} into {@link F}.
   *
   * @param r the {@link R} to convert.
   *
   * @return {@link F} value.
   */
  @Nullable
  default F toFinalOrNull(@NotNull final R r) {
    return this.toFinal(r).orElse(null);
  }

  /**
   * converts the {@link F} into {@link R}.
   *
   * @param f the {@link F} to convert.
   *
   * @return {@link R} value.
   */
  @NotNull
  Optional<R> toRaw(@NotNull F f);

  /**
   * converts the {@link F} into {@link R}.
   *
   * @param f the {@link F} to convert.
   *
   * @return {@link R} value.
   */
  @Nullable
  default R toRawOrNull(@NotNull final F f) {
    return this.toRaw(f).orElse(null);
  }

  /**
   * an abstract that envelopes to {@link TwoSideTransformer}.
   *
   * @param <R> type of the raw value.
   * @param <F> type of the final value.
   */
  @Getter
  @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
  abstract class Base<R, F> implements TwoSideTransformer<R, F> {

    /**
     * the holder.
     */
    @NotNull
    @Delegate
    private final GenericHolder<R, F> holder;
  }

  /**
   * a simple implementation of {@link TwoSideTransformer}.
   *
   * @param <R> type of the raw value.
   * @param <F> type of the final value.
   */
  @Getter
  final class Impl<R, F> extends Base<R, F> {

    /**
     * the transformation.
     */
    @NotNull
    private final Function<@NotNull R, @Nullable F> toFinal;

    /**
     * the transformation.
     */
    @NotNull
    private final Function<@NotNull F, @Nullable R> toRaw;

    /**
     * ctor.
     *
     * @param finalType the final type.
     * @param rawType the raw type.
     * @param toRaw the to raw.
     * @param toFinal the to final.
     */
    private Impl(@NotNull final Class<F> finalType, @NotNull final Class<R> rawType,
                 @NotNull final Function<@NotNull F, @Nullable R> toRaw,
                 @NotNull final Function<@NotNull R, @Nullable F> toFinal) {
      super(GenericHolder.create(rawType, finalType));
      this.toRaw = toRaw;
      this.toFinal = toFinal;
    }

    @NotNull
    @Override
    public Optional<F> toFinal(@NotNull final R r) {
      return Optional.ofNullable(this.toFinal.apply(r));
    }

    @NotNull
    @Override
    public Optional<R> toRaw(@NotNull final F f) {
      return Optional.ofNullable(this.toRaw.apply(f));
    }
  }
}
