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

package io.github.portlek.configs.transformer;

import io.github.portlek.configs.transformer.declaration.GenericDeclaration;
import io.github.portlek.configs.transformer.declaration.GenericsPair;
import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * an interface to determine transformers.
 *
 * @param <R> type of the raw value.
 * @param <F> type of the final value.
 */
public interface Transformer<R, F> {

  /**
   * obtains the final type.
   *
   * @return final type.
   */
  @NotNull
  Class<F> getFinalType();

  /**
   * obtains the id.
   *
   * @return id.
   */
  @NotNull
  default String getId() {
    return this.getFinalType().getSimpleName().toLowerCase(Locale.ROOT);
  }

  /**
   * creates a new generic pair.
   *
   * @return a newly created generic pair.
   */
  @NotNull
  default GenericsPair<R, F> getPair() {
    return GenericsPair.of(GenericDeclaration.of(this.getRawType()), GenericDeclaration.of(this.getFinalType()));
  }

  /**
   * obtains the raw type.
   *
   * @return raw type.
   */
  @NotNull
  Class<R> getRawType();

  /**
   * transforms the raw data into the final.
   *
   * @param data the data to transform.
   *
   * @return transformed data.
   */
  @NotNull
  Optional<F> transform(@NotNull R data);
}
