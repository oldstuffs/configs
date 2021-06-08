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
import io.github.portlek.configs.transformer.exceptions.TransformException;
import io.github.portlek.configs.transformer.resolvers.InMemoryWrappedResolver;
import io.github.portlek.reflection.RefConstructed;
import io.github.portlek.reflection.clazz.ClassOf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.transform.TransformerException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
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
   * creates a new instance of the given class.
   *
   * @param cls the cls to create.
   *
   * @return a new instance of the class.
   *
   * @throws TransformerException if something goes wrong when creating the instance.
   */
  @NotNull
  private static Object createInstance(@NotNull final Class<?> cls) throws TransformerException {
    try {
      if (Collection.class.isAssignableFrom(cls)) {
        if (cls == Set.class) {
          return new HashSet<>();
        }
        if (cls == List.class) {
          return new ArrayList<>();
        }
        return new ClassOf<>(cls).getConstructor()
          .map(RefConstructed::create)
          .orElseThrow();
      }
      if (Map.class.isAssignableFrom(cls)) {
        if (cls == Map.class) {
          return new LinkedHashMap<>();
        }
        return new ClassOf<>(cls).getConstructor()
          .map(RefConstructed::create)
          .orElseThrow();
      }
      throw new TransformerException(String.format("Cannot create instance of %s", cls));
    } catch (final Exception exception) {
      throw new TransformerException(String.format("Failed to create instance of %s", cls), exception);
    }
  }

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
  @Contract("null, _, _, _ -> null; !null, _, _, _ -> !null")
  public <T> T deserialize(@Nullable final Object object, @Nullable final GenericDeclaration genericSource,
                           @NotNull final Class<T> targetClass, @Nullable final GenericDeclaration genericTarget)
    throws TransformerException {
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
      transformedObject.setResolver(new InMemoryWrappedResolver(
        this.pool,
        this,
        this.deserialize(object, source, Map.class, GenericDeclaration.of(Map.class, String.class, Object.class))));
      return (T) transformedObject.update();
    }
    final var serializer = this.pool.getSerializer(targetClass);
    if (object instanceof Map && serializer.isPresent()) {
      return serializer.get().deserialize(TransformedData.deserialization(this, (Map<String, Object>) object), genericTarget)
        .map(targetClass::cast)
        .orElse(null);
    }
    if (genericTarget != null) {
      if (object instanceof Collection<?> && Collection.class.isAssignableFrom(targetClass)) {
        final var sourceList = (Collection<?>) object;
        final var targetList = (Collection<Object>) TransformResolver.createInstance(targetClass);
        final var declaration = genericTarget.getSubTypeAt(0).orElseThrow(() ->
          new TransformException(String.format("Something went wrong when getting sub types(0) of %s", genericTarget)));
        if (declaration.getType() == null) {
          throw new TransformerException(String.format("Something went wrong when getting type of %s", genericTarget));
        }
        for (final var item : sourceList) {
          targetList.add(this.deserialize(item, GenericDeclaration.of(item), declaration.getType(), declaration));
        }
        return targetClass.cast(targetList);
      }
      if (object instanceof Map<?, ?> && Map.class.isAssignableFrom(targetClass)) {
        final var values = (Map<Object, Object>) object;
        final var keyDeclaration = genericTarget.getSubTypeAt(0).orElseThrow(() ->
          new TransformException(String.format("Something went wrong when getting sub types(0) of %s", genericTarget)));
        final var valueDeclaration = genericTarget.getSubTypeAt(1).orElseThrow(() ->
          new TransformException(String.format("Something went wrong when getting sub types(1) of %s", genericTarget)));
        if (keyDeclaration.getType() == null) {
          throw new TransformerException(String.format("Something went wrong when getting type of %s", keyDeclaration));
        }
        if (valueDeclaration.getType() == null) {
          throw new TransformerException(String.format("Something went wrong when getting type of %s", valueDeclaration));
        }
        final var map = (Map<Object, Object>) TransformResolver.createInstance(targetClass);
        for (final var entry : values.entrySet()) {
          map.put(
            this.deserialize(entry.getKey(), GenericDeclaration.of(entry.getKey()), keyDeclaration.getType(), keyDeclaration),
            this.deserialize(entry.getValue(), GenericDeclaration.of(entry.getValue()), valueDeclaration.getType(), valueDeclaration));
        }
        return targetClass.cast(map);
      }
    }
    return null;
  }
}
