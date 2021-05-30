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

import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * an interface to determine transformers.
 *
 * @param <F> type of the final value.
 */
public interface Transformer<F> {

  /**
   * checks if the declaration supports for the transformer.
   *
   * @param declaration the declaration to check.
   *
   * @return {@code true} if the declaration supports for the transformer.
   */
  default boolean canTransform(@NotNull final TransformerDeclaration<?> declaration) {
    return this.getType().isAssignableFrom(declaration.getType());
  }

  /**
   * deserializes the given data into the final value.
   *
   * @param data the data to deserialize.
   *
   * @return deserialized value.
   */
  @NotNull
  Optional<F> deserialize(@NotNull TransformedData data);

  /**
   * obtains the id.
   *
   * @return id.
   */
  @NotNull
  default String getId() {
    return this.getType().getSimpleName().toLowerCase(Locale.ROOT);
  }

  /**
   * obtains the type.
   *
   * @return type.
   */
  @NotNull
  Class<F> getType();

  /**
   * serializes the given value into the data.
   *
   * @param value the value to serialize.
   * @param data the data to serialize.
   */
  void serialize(@NotNull F value, @NotNull TransformedData data);
}
