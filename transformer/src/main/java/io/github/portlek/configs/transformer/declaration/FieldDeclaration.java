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
import io.github.portlek.configs.transformer.annotations.Comment;
import io.github.portlek.configs.transformer.annotations.Names;
import io.github.portlek.configs.transformer.annotations.Variable;
import io.github.portlek.reflection.RefField;
import io.github.portlek.reflection.clazz.ClassOf;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents field declarations.
 */
@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
public final class FieldDeclaration {

  /**
   * the caches.
   */
  private static final Map<Key, FieldDeclaration> CACHES = new ConcurrentHashMap<>();

  /**
   * the comment.
   */
  @Nullable
  private final Comment comment;

  /**
   * the default value.
   */
  @Nullable
  private final Object defaultValue;

  /**
   * the field.
   */
  @NotNull
  private final RefField field;

  /**
   * the generic declaration.
   */
  @NotNull
  private final GenericDeclaration genericDeclaration;

  /**
   * the object.
   */
  @NotNull
  private final Object object;

  /**
   * the path.
   */
  @NotNull
  private final String path;

  /**
   * the variable.
   */
  @Nullable
  private final Variable variable;

  /**
   * the hide variable.
   */
  @Setter
  private boolean hideVariable;

  /**
   * creates a new field declaration from transformed object and field.
   *
   * @param transformedObject the transformed object to create.
   * @param field the field to create.
   * @param object the object to create.
   *
   * @return a newly created field declaration.
   */
  @NotNull
  public static Optional<FieldDeclaration> of(@NotNull final TransformedObject transformedObject,
                                              @NotNull final RefField field, @NotNull final Object object) {
    return Optional.of(FieldDeclaration.CACHES.computeIfAbsent(Key.of(transformedObject, field.getName()), cache ->
      FieldDeclaration.of(
        field.getAnnotation(Comment.class).orElse(null),
        field.of(object).getValue().orElse(null),
        field,
        GenericDeclaration.of(field),
        object,
        Names.Calculated.calculatePath(new ClassOf<>(transformedClass), field),
        field.getAnnotation(Variable.class).orElse(null))));
  }

  /**
   * a class that represents keys.
   */
  @Getter
  @ToString
  @EqualsAndHashCode
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE, staticName = "of")
  private static final class Key {

    /**
     * the class.
     */
    @NotNull
    private final Class<? extends TransformedObject> cls;

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
    private static Key of(@NotNull final TransformedObjectDeclaration transformedObject,
                          @NotNull final RefField field) {
      return Key.of(transformedObject.getImplementation(), field.getName());
    }
  }
}
