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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents generic declarations.
 */
@Getter
@RequiredArgsConstructor(staticName = "of")
public final class GenericDeclaration {

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
   * the primitive wrappers.
   */
  private static final Collection<Class<?>> PRIMITIVE_WRAPPERS = Set.of(
    Boolean.class,
    Byte.class,
    Character.class,
    Double.class,
    Float.class,
    Integer.class,
    Long.class,
    Short.class);

  /**
   * the primitive wrapper by name.
   */
  private static final Map<String, Class<?>> NAME_TO_PRIMITIVE_WRAPPER = GenericDeclaration.PRIMITIVE_WRAPPERS.stream()
    .collect(Collectors.toUnmodifiableMap(Class::getName, Function.identity()));

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
  private final List<GenericDeclaration> subTypes;

  /**
   * the type.
   */
  @Nullable
  private final Class<?> type;

  /**
   * ctor.
   *
   * @param subTypes the sub types.
   * @param type the type.
   */
  private GenericDeclaration(@NotNull final List<GenericDeclaration> subTypes, @Nullable final Class<?> type) {
    this(type != null && type.isEnum(), type != null && type.isPrimitive(), subTypes, type);
  }

  /**
   * ctor.
   *
   * @param type the type.
   */
  private GenericDeclaration(@Nullable final Class<?> type) {
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
   *
   * @return a newly created generic declaration.
   */
  @NotNull
  public static GenericDeclaration of(@Nullable final Class<?> type) {
    return new GenericDeclaration(type);
  }

  /**
   * creates a new generic declaration.
   *
   * @param subTypes the sub type to create.
   * @param type the type to create.
   *
   * @return a newly created generic declaration.
   */
  @NotNull
  public static GenericDeclaration of(@NotNull final List<GenericDeclaration> subTypes, @Nullable final Class<?> type) {
    return new GenericDeclaration(subTypes, type);
  }

  /**
   * creates a new generic declaration.
   *
   * @param object the object to create.
   *
   * @return a newly created generic declaration.
   */
  @NotNull
  public static GenericDeclaration of(@NotNull final Object object) {
    if (object instanceof Class<?>) {
      return new GenericDeclaration((Class<?>) object);
    }
    if (object instanceof Type) {
      return GenericDeclaration.from(((Type) object).getTypeName());
    }
    return new GenericDeclaration(object.getClass());
  }

  /**
   * gets the primitive object of the given object.
   *
   * @param object the object to get.
   *
   * @return obtained primitive object.
   */
  @SuppressWarnings("UnnecessaryUnboxing")
  @NotNull
  public static Object toPrimitive(@NotNull final Object object) {
    if (object instanceof Boolean) {
      return ((Boolean) object).booleanValue();
    }
    if (object instanceof Byte) {
      return ((Byte) object).byteValue();
    }
    if (object instanceof Character) {
      return ((Character) object).charValue();
    }
    if (object instanceof Double) {
      return ((Double) object).doubleValue();
    }
    if (object instanceof Float) {
      return ((Float) object).floatValue();
    }
    if (object instanceof Integer) {
      return ((Integer) object).intValue();
    }
    if (object instanceof Long) {
      return ((Long) object).longValue();
    }
    if (object instanceof Short) {
      return ((Short) object).shortValue();
    }
    return object;
  }

  /**
   * creates a new generic declaration.
   *
   * @param typeName the type name to create
   *
   * @return a newly created generic declaration.
   */
  @NotNull
  private static GenericDeclaration from(@NotNull final String typeName) {
    final var builder = new StringBuilder();
    final var chars = typeName.toCharArray();
    for (var index = 0; index < chars.length; index++) {
      final var ch = chars[index];
      if (ch != '<') {
        builder.append(ch);
        continue;
      }
      final var className = builder.toString();
      final var type = GenericDeclaration.getPrimitiveOrClass(className);
      final var genericType = typeName.substring(index + 1, typeName.length() - 1);
      final var subTypes = GenericDeclaration.getSeparateTypes(genericType).stream()
        .map(GenericDeclaration::from)
        .collect(Collectors.toList());
      return GenericDeclaration.of(subTypes, type);
    }
    return GenericDeclaration.of(GenericDeclaration.getPrimitiveOrClass(builder.toString()));
  }

  /**
   * gets primitive class from the type name or class.
   *
   * @param type the type to get.
   *
   * @return obtained class.
   */
  @Nullable
  private static Class<?> getPrimitiveOrClass(@NotNull final String type) {
    if (GenericDeclaration.NAME_TO_PRIMITIVE.containsKey(type)) {
      return GenericDeclaration.NAME_TO_PRIMITIVE.get(type);
    }
    try {
      return Class.forName(type);
    } catch (final ClassNotFoundException ignored) {
    }
    return null;
  }

  /**
   * gets separated types.
   *
   * @param types the types to get.
   *
   * @return obtained separated types.
   */
  @NotNull
  private static List<String> getSeparateTypes(@NotNull final String types) {
    final var builder = new StringBuilder();
    final var charArray = types.toCharArray();
    var skip = false;
    final var out = new ArrayList<String>();
    for (var index = 0; index < charArray.length; index++) {
      final var c = charArray[index];
      if (c == '<') {
        skip = true;
      }
      if (c == '>') {
        skip = false;
      }
      if (skip) {
        builder.append(c);
        continue;
      }
      if (c == ',') {
        out.add(builder.toString());
        builder.setLength(0);
        index++;
        continue;
      }
      builder.append(c);
    }
    out.add(builder.toString());
    return out;
  }

  /**
   * gets sub type at the index.
   *
   * @param index the index to get.
   *
   * @return obtained sub type.
   */
  @NotNull
  public Optional<GenericDeclaration> getSubTypeAt(final int index) {
    return index >= this.subTypes.size()
      ? Optional.empty()
      : Optional.ofNullable(this.subTypes.get(index));
  }

  /**
   * gets wrapped class.
   *
   * @return wrapped class.
   */
  @NotNull
  public Optional<Class<?>> getWrapped() {
    return this.type != null
      ? Optional.ofNullable(GenericDeclaration.PRIMITIVE_TO_WRAPPER.get(this.type))
      : Optional.empty();
  }

  /**
   * checks if {@link #type} has wrapper type.
   *
   * @return {@code true} if {@link #type} has wrapper type.
   */
  public boolean hasWrapper() {
    return this.type != null &&
      GenericDeclaration.PRIMITIVE_WRAPPERS.contains(this.type);
  }
}
