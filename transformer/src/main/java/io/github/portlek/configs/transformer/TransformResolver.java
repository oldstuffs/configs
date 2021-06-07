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

import io.github.portlek.configs.transformer.declarations.GenericDeclaration;
import io.github.portlek.configs.transformer.resolvers.InMemoryWrappedResolver;
import io.github.portlek.reflection.clazz.ClassOf;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an abstract class that represents transform resolvers.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TransformResolver {

  /**
   * the pool.
   */
  @NotNull
  private final TransformerPool pool;

  /**
   * deserializes the object and converts it into object class.
   *
   * @param object the object to deserialize.
   * @param targetClass the target class to deserialize.
   * @param genericSource the generic source to deserialize.
   * @param genericTarget the generic target to deserialize.
   * @param <T> type of the deserialized object class.
   *
   * @return deserialized object.
   */
  @SuppressWarnings("unchecked")
  @Nullable
  public <T> T deserialize(@Nullable final Object object, @Nullable final GenericDeclaration genericSource,
                           @NotNull final Class<T> targetClass, @Nullable final GenericDeclaration genericTarget) {
    if (object == null) {
      return null;
    }
    final var source = genericSource == null
      ? GenericDeclaration.of(object)
      : genericSource;
    var target = genericTarget == null
      ? GenericDeclaration.of(targetClass)
      : genericTarget;
    if (target.isPrimitive()) {
      target = GenericDeclaration.ofReady(target.toWrapper().orElse(null));
    }
    final var objectClass = object.getClass();
    final var objectClassOf = new ClassOf<>(objectClass);
    try {
      if (object instanceof String && target.isEnum()) {
        final var targetClassOf = new ClassOf<>(targetClass);
        final var stringObject = (String) object;
        try {
          final var valueOf = targetClassOf.getMethod("valueOf", String.class).orElseThrow();
          final var enumValue = valueOf.call(stringObject);
          if (enumValue.isPresent()) {
            return targetClass.cast(enumValue.get());
          }
        } catch (final Exception e) {
          final var enumValues = (Enum<?>[]) targetClass.getEnumConstants();
          for (final var value : enumValues) {
            if (!stringObject.equalsIgnoreCase(value.name())) {
              continue;
            }
            return targetClass.cast(value);
          }
        }
        final var error = String.format("no enum value for name %s (available: %s)",
          stringObject, Arrays.stream(targetClass.getEnumConstants())
            .map(item -> ((Enum<?>) item).name())
            .collect(Collectors.joining(", ")));
        throw new IllegalArgumentException(error);
      }
      if (source.isEnum() && targetClass == String.class) {
        final var name = objectClassOf.getMethodByName("name")
          .orElseThrow()
          .of(object)
          .call()
          .orElseThrow();
        return targetClass.cast(name);
      }
    } catch (final Exception exception) {
      final var error = String.format("Failed to resolve enum %s <> %s",
        object.getClass(), targetClass);
      throw new RuntimeException(error, exception);
    }
    if (TransformedObject.class.isAssignableFrom(targetClass)) {
      final var transformedObject = TransformerPool.createTransformedObject((Class<? extends TransformedObject>) targetClass);
      final var map = this.deserialize(object, source, Map.class, GenericDeclaration.of(Map.class, String.class, Object.class));
      transformedObject.setResolver(new InMemoryWrappedResolver(this.pool, this, map == null ? new HashMap<>() : map));
      return (T) transformedObject.update();
    }
    final var serializer = this.pool.getSerializer(targetClass);
    if (object instanceof Map && serializer.isPresent()) {
      return serializer.get().deserialize(TransformedData.deserialization(this, (Map<String, Object>) object), genericTarget)
        .map(targetClass::cast)
        .orElse(null);
    }
    if (genericTarget != null) {
    }
    return null;
  }
}
