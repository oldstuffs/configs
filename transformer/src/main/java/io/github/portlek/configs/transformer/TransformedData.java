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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents transformed data.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransformedData {

  /**
   * the deserialized map.
   */
  private final Map<String, Object> deserializedMap;

  /**
   * the resolver.
   */
  @NotNull
  private final TransformResolver resolver;

  /**
   * the serialization.
   */
  private final boolean serialization;

  /**
   * the serialized map.
   */
  private final Map<String, Object> serializedMap;

  /**
   * creates a new transformed data instance for deserialization.
   *
   * @param resolver the resolver to create.
   *
   * @return a transformed data instance for deserialization.
   */
  @NotNull
  public static TransformedData deserialization(@NotNull final TransformResolver resolver,
                                                @NotNull final Map<String, Object> map) {
    return new TransformedData(new ConcurrentHashMap<>(map), resolver, false, new ConcurrentHashMap<>());
  }

  /**
   * creates a new transformed data instance for serialization.
   *
   * @param resolver the resolver to create.
   *
   * @return a transformed data instance for serialization.
   */
  @NotNull
  public static TransformedData serialization(@NotNull final TransformResolver resolver) {
    return new TransformedData(new ConcurrentHashMap<>(), resolver, true, new ConcurrentHashMap<>());
  }

  /**
   * checks if the deserialized map contains the key.
   *
   * @param key the key to check.
   *
   * @return {@code true} if the deserialized map contains the key.
   */
  public boolean containsKey(@NotNull final String key) {
    return this.canDeserialize() && this.deserializedMap.containsKey(key);
  }

  /**
   * gets a value from deserialized map.
   *
   * @param key the key to get.
   * @param objectClass the object class to get.
   * @param <T> type of the value.
   *
   * @return obtained value.
   */
  @NotNull
  public <T> Optional<T> get(@NotNull final String key, @NotNull final Class<T> objectClass) {
    if (this.canSerialize()) {
      return Optional.empty();
    }
    final var object = this.deserializedMap.get(key);
    return Optional.ofNullable(this.resolver.deserialize(
      object,
      GenericDeclaration.of(object),
      objectClass,
      null));
  }

  /**
   * gets a value from deserialized map as list.
   *
   * @param key the key to get.
   * @param elementClass the element class to get.
   * @param <T> type of the elements of list.
   *
   * @return obtained list value.
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public <T> Optional<List<T>> getAsList(@NotNull final String key, @NotNull final Class<T> elementClass) {
    if (this.canSerialize()) {
      return Optional.empty();
    }
    final var object = this.deserializedMap.get(key);
    return Optional.ofNullable(this.resolver.deserialize(
      object,
      GenericDeclaration.of(object),
      List.class,
      GenericDeclaration.of(List.class, elementClass)));
  }

  /**
   * gets a value from deserialized map as map.
   *
   * @param key the key to get.
   * @param keyClass the key class to get.
   * @param valueClass the value class to get.
   * @param <K> type of the keys of map.
   * @param <V> type of the values of map.
   *
   * @return obtained map value.
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public <K, V> Optional<Map<K, V>> getAsMap(@NotNull final String key, @NotNull final Class<K> keyClass,
                                             @NotNull final Class<V> valueClass) {
    if (this.canSerialize()) {
      return Optional.empty();
    }
    final var object = this.deserializedMap.get(key);
    return Optional.ofNullable(this.resolver.deserialize(
      object,
      GenericDeclaration.of(object),
      Map.class,
      GenericDeclaration.of(Map.class, keyClass, valueClass)));
  }

  /**
   * checks if the data can deserialize.
   *
   * @return {@code true} if data can deserialize.
   */
  private boolean canDeserialize() {
    return !this.serialization;
  }

  /**
   * checks if the data can serialize.
   *
   * @return {@code true} if data can serialize.
   */
  private boolean canSerialize() {
    return this.serialization;
  }
}
