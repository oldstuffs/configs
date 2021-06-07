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

package io.github.portlek.configs.transformer.serializers;

import io.github.portlek.configs.transformer.TransformedData;
import io.github.portlek.configs.transformer.declarations.GenericDeclaration;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an interface to determine object serializers.
 *
 * @param <T> type of the object.
 */
public interface ObjectSerializer<T> {

  /**
   * deserializes the object.
   *
   * @param transformedData the transformed data to deserialize.
   * @param declaration the declaration to deserialize.
   *
   * @return deserialized object.
   */
  @NotNull
  Optional<T> deserialize(@NotNull TransformedData transformedData, @Nullable GenericDeclaration declaration);

  /**
   * deserializes the object.
   *
   * @param field the field to serialize.
   * @param transformedData the transformed data to deserialize.
   * @param declaration the declaration to deserialize.
   *
   * @return deserialized object.
   */
  @NotNull
  default Optional<T> deserialize(@NotNull final T field, @NotNull final TransformedData transformedData,
                                  @NotNull final GenericDeclaration declaration) {
    return this.deserialize(transformedData, declaration);
  }

  /**
   * serializes the object.
   *
   * @param t the t to serialize.
   * @param transformedData the transformed data to serialize.
   */
  void serialize(@NotNull T t, @NotNull TransformedData transformedData);

  /**
   * checks if the given class supports the serialization.
   *
   * @param cls the cls to check.
   *
   * @return {@code true} if the class supports the serialization.
   */
  boolean supports(@NotNull Class<?> cls);
}
