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

package io.github.portlek.configs.transformer.declarations;

import io.github.portlek.configs.transformer.annotations.Comment;
import io.github.portlek.configs.transformer.annotations.Names;
import io.github.portlek.configs.transformer.annotations.Variable;
import io.github.portlek.configs.transformer.exceptions.TransformException;
import io.github.portlek.reflection.RefField;
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
  @Nullable
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
   * the starting value.
   */
  @Nullable
  @Setter
  private Object startingValue;

  /**
   * ctor.
   *
   * @param comment the comment.
   * @param field the field.
   * @param genericDeclaration the generic declaration.
   * @param object the object.
   * @param path the path.
   * @param variable the variable.
   * @param startingValue the starting value.
   */
  private FieldDeclaration(@Nullable final Comment comment, @NotNull final RefField field,
                           @NotNull final GenericDeclaration genericDeclaration, @Nullable final Object object,
                           @NotNull final String path, @Nullable final Variable variable,
                           @Nullable final Object startingValue) {
    this.comment = comment;
    this.field = field;
    this.genericDeclaration = genericDeclaration;
    this.object = object;
    this.path = path;
    this.variable = variable;
    this.startingValue = startingValue;
  }

  /**
   * creates a new field declaration.
   *
   * @param parent the parent to create.
   * @param object the object to create
   * @param cls the cls to create.
   * @param field the field to create.
   *
   * @return a newly created field declaration.
   */
  @NotNull
  public static FieldDeclaration of(@Nullable final Names parent, @Nullable final Object object,
                                    @NotNull final Class<?> cls, @NotNull final RefField field) {
    return FieldDeclaration.CACHES.computeIfAbsent(Key.of(cls, field.getName()), cache ->
      new FieldDeclaration(
        field.getAnnotation(Comment.class).orElse(null),
        field,
        GenericDeclaration.of(field),
        object,
        Names.Calculated.calculatePath(parent, field),
        field.getAnnotation(Variable.class).orElse(null),
        Optional.ofNullable(object)
          .flatMap(o -> field.of(o).getValue())
          .orElse(null)));
  }

  /**
   * gets the value of the field.
   *
   * @return value of the field.
   */
  @Nullable
  public Object getValue() throws TransformException {
    if (this.hideVariable) {
      return this.startingValue;
    }
    try {
      return this.field.of(this.object).getValue().orElseThrow();
    } catch (final Exception exception) {
      throw new TransformException("Failed to use #getValue", exception);
    }
  }

  /**
   * sets the field value to the value.
   *
   * @param value the value to set.
   */
  public void setValue(@NotNull final Object value) {
    this.field.of(this.object).setValue(value);
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
    private final Class<?> cls;

    /**
     * the field name.
     */
    @NotNull
    private final String fieldName;
  }
}
