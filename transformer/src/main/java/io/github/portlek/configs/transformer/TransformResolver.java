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

import io.github.portlek.configs.transformer.declarations.FieldDeclaration;
import io.github.portlek.configs.transformer.declarations.GenericDeclaration;
import io.github.portlek.configs.transformer.declarations.TransformedObjectDeclaration;
import io.github.portlek.configs.transformer.exceptions.TransformException;
import io.github.portlek.configs.transformer.resolvers.InMemoryWrappedResolver;
import io.github.portlek.configs.transformer.serializers.ObjectSerializer;
import io.github.portlek.configs.transformer.transformers.Transformer;
import io.github.portlek.configs.transformer.transformers.TransformerPack;
import io.github.portlek.reflection.clazz.ClassOf;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an abstract class that represents transform resolvers.
 */
public abstract class TransformResolver {

  /**
   * the parent object.
   */
  @Nullable
  private TransformedObject parentObject;

  /**
   * the registry.
   */
  @NotNull
  private TransformRegistry registry = new TransformRegistry()
    .withDefaultTransformers();

  /**
   * obtains the parent object.
   *
   * @return parent object.
   */
  @Nullable
  public final TransformedObject getParentObject() {
    return this.parentObject;
  }

  /**
   * obtains the registry.
   *
   * @return registry.
   */
  @NotNull
  public final TransformRegistry getRegistry() {
    return this.registry;
  }

  /**
   * sets the parent object.
   *
   * @param parentObject the parent object to set.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public final TransformResolver withParentObject(@Nullable final TransformedObject parentObject) {
    this.parentObject = parentObject;
    return this;
  }

  /**
   * sets the registry.
   *
   * @param registry the registry to set.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public final TransformResolver withRegistry(@NotNull final TransformRegistry registry) {
    this.registry = registry;
    return this;
  }

  /**
   * registers the pack.
   *
   * @param packs the packs to register.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public final TransformResolver withTransformerPacks(@NotNull final TransformerPack... packs) {
    this.registry.withTransformPacks(packs);
    return this;
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
    throws TransformException {
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
        throw new TransformException(error);
      }
      if (source.isEnum() && targetClass == String.class) {
        final var name = objectClassOf.getMethodByName("name")
          .orElseThrow()
          .of(object)
          .call()
          .orElseThrow(() ->
            new TransformException(String.format("Something went wrong when getting method called name in %s",
              objectClass)));
        return targetClass.cast(name);
      }
    } catch (final Exception exception) {
      final var error = String.format("Failed to resolve enum %s <> %s",
        object.getClass(), targetClass);
      throw new RuntimeException(error, exception);
    }
    if (TransformedObject.class.isAssignableFrom(targetClass)) {
      final var transformedObject = TransformerPool.create((Class<? extends TransformedObject>) targetClass);
      transformedObject.withResolver(new InMemoryWrappedResolver(
        this,
        this.deserialize(object, source, Map.class, GenericDeclaration.of(Map.class, String.class, Object.class))));
      return (T) transformedObject.update();
    }
    final var serializer = this.registry.getSerializer(targetClass);
    if (object instanceof Map && serializer.isPresent()) {
      return serializer.get().deserialize(TransformedData.deserialization(this, (Map<String, Object>) object), genericTarget)
        .map(targetClass::cast)
        .orElse(null);
    }
    if (genericTarget != null) {
      if (object instanceof Collection<?> && Collection.class.isAssignableFrom(targetClass)) {
        final var sourceList = (Collection<?>) object;
        final var targetList = (Collection<Object>) TransformerPool.createInstance(targetClass);
        final var declaration = genericTarget.getSubTypeAt(0).orElseThrow(() ->
          new TransformException(String.format("Something went wrong when getting sub types(0) of %s", genericTarget)));
        if (declaration.getType() == null) {
          throw new TransformException(String.format("Something went wrong when getting type of %s", genericTarget));
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
          throw new TransformException(String.format("Something went wrong when getting type of %s", keyDeclaration));
        }
        if (valueDeclaration.getType() == null) {
          throw new TransformException(String.format("Something went wrong when getting type of %s", valueDeclaration));
        }
        final var map = (Map<Object, Object>) TransformerPool.createInstance(targetClass);
        for (final var entry : values.entrySet()) {
          map.put(
            this.deserialize(entry.getKey(), GenericDeclaration.of(entry.getKey()), keyDeclaration.getType(), keyDeclaration),
            this.deserialize(entry.getValue(), GenericDeclaration.of(entry.getValue()), valueDeclaration.getType(), valueDeclaration));
        }
        return targetClass.cast(map);
      }
    }
    final var transformerOptional = this.registry.getTransformer(source, target);
    if (transformerOptional.isEmpty()) {
      if (targetClass.isPrimitive() && GenericDeclaration.isWrapperBoth(targetClass, objectClass)) {
        return (T) GenericDeclaration.toPrimitive(object);
      }
      if (targetClass.isPrimitive() || GenericDeclaration.of(targetClass).hasWrapper()) {
        final var simplified = this.serialize(object, GenericDeclaration.of(objectClass), false);
        return this.deserialize(simplified, GenericDeclaration.of(simplified), targetClass, GenericDeclaration.of(targetClass));
      }
      try {
        return targetClass.cast(object);
      } catch (final ClassCastException exception) {
        throw new TransformException(String.format("Cannot resolve %s to %s (%s => %s): %s",
          object.getClass(), targetClass, source, target, object), exception);
      }
    }
    //noinspection rawtypes
    final Transformer transformer = transformerOptional.get();
    if (targetClass.isPrimitive()) {
      final var transformed = transformer.transform(object);
      return (T) GenericDeclaration.toPrimitive(transformed);
    }
    return targetClass.cast(transformer.transform(object).orElse(null));
  }

  /**
   * obtains all keys of the parent object.
   *
   * @return all keys.
   */
  public List<String> getAllKeys() {
    if (this.parentObject == null) {
      return Collections.emptyList();
    }
    final var declaration = this.parentObject.getDeclaration();
    if (declaration == null) {
      throw new TransformException("Something went wrong when getting all keys of the parent object.");
    }
    return new ArrayList<>(declaration.getFields().keySet());
  }

  /**
   * gets value at path.
   *
   * @param path the path to get.
   *
   * @return value at path.
   */
  @NotNull
  public abstract Optional<Object> getValue(@NotNull String path);

  /**
   * gets value at path.
   *
   * @param path the path to get.
   * @param cls the cls to get.
   * @param genericType the generic type to get.
   * @param <T> type of the value.
   *
   * @return value at path.
   */
  @NotNull
  public <T> Optional<T> getValue(@NotNull final String path, @NotNull final Class<T> cls,
                                  @Nullable final GenericDeclaration genericType) {
    return this.getValue(path)
      .map(value -> this.deserialize(value, GenericDeclaration.of(value), cls, genericType));
  }

  /**
   * checks if the object can transform to string.
   *
   * @param object the object to check.
   * @param declaration the generic declaration to check.
   */
  public boolean isToStringObject(@NotNull final Object object, @Nullable final GenericDeclaration declaration) {
    if (object instanceof Class<?>) {
      final var cls = (Class<?>) object;
      return cls.isEnum() ||
        this.registry.getTransformer(declaration, GenericDeclaration.of(String.class)).isPresent();
    }
    return object.getClass().isEnum() ||
      this.isToStringObject(object.getClass(), declaration);
  }

  /**
   * checks if the value is valid or not.
   *
   * @param declaration the declaration to check.
   * @param value the value to check.
   *
   * @return {@code true} if the value is valid.
   */
  public boolean isValid(@NotNull final FieldDeclaration declaration, @Nullable final Object value) {
    return true;
  }

  /**
   * loads the values into stream.
   *
   * @param inputStream the input stream to load.
   * @param declaration the declaration to load.
   *
   * @throws Exception if something goes wrong when loading the values.
   */
  public abstract void load(@NotNull InputStream inputStream, @NotNull TransformedObjectDeclaration declaration)
    throws Exception;

  /**
   * checks if the path exists.
   *
   * @param path the field path to check.
   *
   * @return {@code true} if the path exists.
   */
  public boolean pathExists(@NotNull final String path) {
    return this.getValue(path).isPresent();
  }

  /**
   * serializes the object.
   *
   * @param value the value to serialize.
   * @param genericType the generic type to serialize.
   * @param conservative the conservative to serialize.
   *
   * @return serialized object.
   *
   * @throws TransformException if something goes wrong when serializing the object.
   */
  @SuppressWarnings("unchecked")
  @Nullable
  @Contract("null, _, _ -> null; !null, _, _ -> !null")
  public Object serialize(@Nullable final Object value, @Nullable final GenericDeclaration genericType,
                          final boolean conservative) throws TransformException {
    if (value == null) {
      return null;
    }
    if (TransformedObject.class.isAssignableFrom(value.getClass())) {
      return ((TransformedObject) value).asMap(this, conservative);
    }
    final var serializerType = genericType != null ? genericType.getType() : value.getClass();
    if (serializerType == null) {
      throw new TransformException(String.format("Something went wrong when getting type of %s or %s",
        genericType, value));
    }
    final var serializerOptional = this.registry.getSerializer(serializerType);
    if (serializerOptional.isEmpty()) {
      if (conservative && (serializerType.isPrimitive() || GenericDeclaration.of(serializerType).hasWrapper())) {
        return value;
      }
      if (serializerType.isPrimitive()) {
        final var wrappedPrimitive = GenericDeclaration.of(serializerType).toWrapper().orElseThrow();
        return this.serialize(wrappedPrimitive.cast(value), GenericDeclaration.of(wrappedPrimitive), false);
      }
      if (genericType == null) {
        final var valueDeclaration = GenericDeclaration.of(value);
        if (this.isToStringObject(serializerType, valueDeclaration)) {
          return this.deserialize(value, null, String.class, null);
        }
      }
      if (this.isToStringObject(serializerType, genericType)) {
        return this.deserialize(value, genericType, String.class, null);
      }
      if (value instanceof Collection<?>) {
        return this.serializeCollection((Collection<?>) value, genericType, conservative);
      }
      if (value instanceof Map<?, ?>) {
        return this.serializeMap((Map<Object, Object>) value, genericType, conservative);
      }
      throw new TransformException(String.format("Cannot simplify type %s (%s): '%s' [%s]",
        serializerType, genericType, value, value.getClass()));
    }
    //noinspection rawtypes
    final ObjectSerializer serializer = serializerOptional.get();
    final var serializationData = TransformedData.serialization(this);
    serializer.serialize(value, serializationData);
    final var serializationMap = serializationData.getSerializedMap();
    if (!conservative) {
      final var newSerializationMap = new LinkedHashMap<String, Object>();
      serializationMap.forEach((mKey, mValue) ->
        newSerializationMap.put(mKey, this.serialize(mValue, GenericDeclaration.of(mValue), false)));
      return newSerializationMap;
    }
    return serializationMap;
  }

  /**
   * serializes collection.
   *
   * @param value the value to simplify.
   * @param genericType the generic type to simplify.
   * @param conservative the conservative to simplify.
   *
   * @return simplified collection.
   *
   * @throws TransformException if something goes wrong when simplifying the value.
   */
  public List<?> serializeCollection(@NotNull final Collection<?> value, @Nullable final GenericDeclaration genericType,
                                     final boolean conservative) throws TransformException {
    final var collectionSubtype = genericType == null
      ? null
      : genericType.getSubTypeAt(0).orElse(null);
    return value.stream()
      .map(collectionElement -> this.serialize(collectionElement, collectionSubtype, conservative))
      .collect(Collectors.toList());
  }

  /**
   * serializes map.
   *
   * @param value the value to simplify.
   * @param genericType the generic type to simplify.
   * @param conservative the conservative to simplify.
   *
   * @return simplified map.
   *
   * @throws TransformException if something goes wrong when simplifying the value.
   */
  @NotNull
  public Map<Object, Object> serializeMap(@NotNull final Map<Object, Object> value,
                                          @Nullable final GenericDeclaration genericType,
                                          final boolean conservative) throws TransformException {
    final var keyDeclaration = genericType == null
      ? null
      : genericType.getSubTypeAt(0).orElse(null);
    final var valueDeclaration = genericType == null
      ? null
      : genericType.getSubTypeAt(1).orElse(null);
    return value.entrySet().stream()
      .map(entry -> Map.entry(
        this.serialize(entry.getKey(), keyDeclaration, conservative),
        this.serialize(entry.getValue(), valueDeclaration, conservative)
      ))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
  }

  /**
   * sets the value to path.
   *
   * @param path the path to set.
   * @param value the value to set.
   * @param genericType the generic type to set.
   * @param field the field to set.
   */
  public abstract void setValue(@NotNull String path, @Nullable Object value, @Nullable GenericDeclaration genericType,
                                @Nullable FieldDeclaration field);

  /**
   * writes the steam.
   *
   * @param outputStream the output steam to write.
   * @param declaration the declaration to write.
   *
   * @throws Exception if something goes wrong when writing the stream.
   */
  public abstract void write(@NotNull OutputStream outputStream, @NotNull TransformedObjectDeclaration declaration)
    throws Exception;
}
