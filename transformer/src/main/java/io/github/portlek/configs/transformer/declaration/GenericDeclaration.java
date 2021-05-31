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

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents generic declarations.
 *
 * @param <T> type of the value.
 */
@Getter
@RequiredArgsConstructor(staticName = "of")
public final class GenericDeclaration<T> implements Declaration {

  /**
   * the primitives.
   */
  private static final Collection<Class<?>> PRIMITIVES = Set.of(
    Boolean.TYPE,
    Byte.TYPE,
    Character.TYPE,
    Double.TYPE,
    Float.TYPE,
    Integer.TYPE,
    Long.TYPE,
    Short.TYPE);

  /**
   * the primitives by name.
   */
  private static final Map<String, Class<?>> NAME_TO_PRIMITIVE = GenericDeclaration.PRIMITIVES.stream()
    .collect(Collectors.toUnmodifiableMap(Class::getName, Function.identity()));

  /**
   * the primitives by wrappers.
   */
  private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER = Map.of(
    Boolean.TYPE, Boolean.class,
    Byte.TYPE, Byte.class,
    Character.TYPE, Character.class,
    Double.TYPE, Double.class,
    Float.TYPE, Float.class,
    Integer.TYPE, Integer.class,
    Long.TYPE, Long.class,
    Short.TYPE, Short.class);

  /**
   * the is enum.
   */
  private final boolean isEnum;

  /**
   * the is primitive.
   */
  private final boolean isPrimitive;

  /**
   * the sub types.
   */
  @NotNull
  private final List<GenericDeclaration<?>> subTypes;

  /**
   * the type.
   */
  @NotNull
  private final Class<? extends T> type;

  /**
   * ctor.
   *
   * @param subTypes the sub types.
   * @param type the type.
   */
  private GenericDeclaration(@NotNull final List<GenericDeclaration<?>> subTypes, @NotNull final Class<? extends T> type) {
    this(type.isEnum(), type.isPrimitive(), subTypes, type);
  }

  /**
   * ctor.
   *
   * @param type the type.
   */
  private GenericDeclaration(@NotNull final Class<? extends T> type) {
    this(Collections.emptyList(), type);
  }

  /**
   * checks if the given primitive's wrapper is the given wrapper.
   *
   * @param primitive the primitive to check.
   * @param wrapper the wrapper to check.
   *
   * @return {@code true} if the given primitive's wrapper is the given wrapper.
   */
  public static boolean isWrapper(@NotNull final Class<?> primitive, @NotNull final Class<?> wrapper) {
    return Objects.equals(GenericDeclaration.PRIMITIVE_TO_WRAPPER.get(primitive), wrapper);
  }

  /**
   * checks if the given right's/left's wrappers is the given left's/right's wrapper.
   *
   * @param left the left to check.
   * @param right the right to check.
   *
   * @return {@code true} if the given primitive's wrapper is the given wrapper.
   */
  public static boolean isWrapperBoth(@NotNull final Class<?> left, @NotNull final Class<?> right) {
    return GenericDeclaration.isWrapper(left, right) || GenericDeclaration.isWrapper(right, left);
  }

  /**
   * creates a new generic declaration.
   *
   * @param type the type to create.
   * @param <T> type of the value.
   *
   * @return a newly created generic declaration.
   */
  @NotNull
  public static <T> GenericDeclaration<T> of(@NotNull final Class<T> type) {
    return new GenericDeclaration<>(type);
  }

  /**
   * creates a new generic declaration.
   *
   * @param subTypes the sub type to create.
   * @param type the type to create.
   * @param <T> type of the value.
   *
   * @return a newly created generic declaration.
   */
  @NotNull
  public static <T> GenericDeclaration<T> of(@NotNull final List<GenericDeclaration<?>> subTypes,
                                             @NotNull final Class<T> type) {
    return new GenericDeclaration<>(subTypes, type);
  }

  /**
   * creates a new generic declaration.
   *
   * @param object the object to create.
   * @param <T> type of the value.
   *
   * @return a newly created generic declaration.
   */
  @NotNull
  public static <T> GenericDeclaration<T> of(@NotNull final T object) {
    if (object instanceof Class<?>) {
      //noinspection unchecked
      return new GenericDeclaration<>((Class<? extends T>) object);
    }
    if (object instanceof Type) {
      return GenericDeclaration.from(((Type) object).getTypeName());
    }
    //noinspection unchecked
    return new GenericDeclaration<>((Class<? extends T>) object.getClass());
  }

  /**
   * creates a new generic declaration.
   *
   * @param typeName the type name to create.
   * @param <T> type of the value.
   *
   * @return a newly created generic declaration.
   */
  private static <T> GenericDeclaration<T> from(@NotNull final String typeName) {
    return null;
  }
}
