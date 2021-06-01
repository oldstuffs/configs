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

import io.github.portlek.configs.transformer.TransformedObject;
import io.github.portlek.reflection.RefField;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents field declarations.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class FieldDeclaration {

  /**
   * the caches.
   */
  private static final Map<Key, FieldDeclaration> CACHES = new ConcurrentHashMap<>();

  /**
   * the field.
   */
  @NotNull
  private final RefField field;

  /**
   * creates a new field declaration from transformed object and field.
   *
   * @param transformedObject the transformed object to create.
   * @param field the field to create.
   *
   * @return a newly created field declaration.
   */
  @NotNull
  public static FieldDeclaration of(@NotNull final TransformedObject transformedObject, @NotNull final RefField field) {
    final var key = Key.of(transformedObject, field);
    final var declaration = FieldDeclaration.CACHES.computeIfAbsent(key, cache -> {
      if (field.hasAnnotation())
    });
    return declaration;
  }

  /**
   * a class that represents keys.
   */
  @Getter
  @ToString
  @EqualsAndHashCode
  @RequiredArgsConstructor(staticName = "of")
  private static final class Key {

    /**
     * the class.
     */
    @NotNull
    private final Class<?> cls;

    /**
     * the field name.
     */
    @NotNull
    private final String fieldName;

    /**
     * creates a new key from transformed object and field.
     *
     * @param transformedObject the transformed object to create.
     * @param field the field to create.
     *
     * @return a newly created key.
     */
    @NotNull
    public static Key of(@NotNull final TransformedObject transformedObject, @NotNull final RefField field) {
      return Key.of(transformedObject.getImplementation(), field.getName());
    }
  }
}
