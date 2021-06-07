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

import io.github.portlek.configs.transformer.declarations.TransformedObjectDeclaration;
import io.github.portlek.configs.transformer.generics.GenericPair;
import io.github.portlek.configs.transformer.serializers.ObjectSerializer;
import io.github.portlek.configs.transformer.transformers.Transformer;
import io.github.portlek.configs.transformer.transformers.TransformerPack;
import io.github.portlek.configs.transformer.transformers.TwoSideTransformer;
import io.github.portlek.configs.transformer.transformers.defaults.TransformerObjectToString;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents transformer pools.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransformerPool {

  /**
   * the transformed declaration.
   */
  @NotNull
  private final TransformedObjectDeclaration declaration;

  /**
   * the serializers.
   */
  @NotNull
  private final Set<ObjectSerializer<?>> serializers = new HashSet<>();

  /**
   * the transformers by id.
   */
  @NotNull
  private final Map<GenericPair, Transformer<?, ?>> transformers = new ConcurrentHashMap<>();

  /**
   * the resolver.
   */
  @NotNull
  @Setter
  private TransformResolver resolver;

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   * @param resolver the resolver to create.
   * @param registerDefaultTransformers the register default transformers to create.
   * @param initializer the initializer to create.
   * @param transformers the transformers to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final TransformedObject object,
                                       @NotNull final TransformResolver resolver,
                                       final boolean registerDefaultTransformers,
                                       @NotNull final UnaryOperator<@NotNull TransformerPool> initializer,
                                       @NotNull final Transformer<?, ?>... transformers) {
    final var pool = new TransformerPool(TransformedObjectDeclaration.of(object), resolver);
    initializer.apply(registerDefaultTransformers
      ? pool.registerDefaultTransformers()
      : pool);
    Arrays.stream(transformers).forEach(pool::registerTransformer);
    return pool;
  }

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   * @param resolver the resolver to create.
   * @param initializer the initializer to create.
   * @param transformers the transformers to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final TransformedObject object,
                                       @NotNull final TransformResolver resolver,
                                       @NotNull final UnaryOperator<@NotNull TransformerPool> initializer,
                                       @NotNull final Transformer<?, ?>... transformers) {
    return TransformerPool.create(object, resolver, true, initializer, transformers);
  }

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   * @param resolver the resolver to create.
   * @param registerDefaultTransformers the register default transformers to create.
   * @param transformers the transformers to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final TransformedObject object,
                                       @NotNull final TransformResolver resolver,
                                       final boolean registerDefaultTransformers,
                                       @NotNull final Transformer<?, ?>... transformers) {
    return TransformerPool.create(object, resolver, registerDefaultTransformers, UnaryOperator.identity(),
      transformers);
  }

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   * @param resolver the resolver to create.
   * @param transformers the transformers to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final TransformedObject object,
                                       @NotNull final TransformResolver resolver,
                                       @NotNull final Transformer<?, ?>... transformers) {
    return TransformerPool.create(object, resolver, true, transformers);
  }

  @NotNull
  public static TransformerPool createUnsafe(@NotNull final Class<? extends TransformedObject> targetClass) {
    return null;
  }

  /**
   * gets the serializer from class.
   *
   * @param cls the cls to get.
   *
   * @return obtains object serializer.
   */
  @NotNull
  public Optional<ObjectSerializer<?>> getSerializer(@NotNull final Class<?> cls) {
    return this.serializers.stream()
      .filter(serializer -> serializer.supports(cls))
      .findFirst();
  }

  /**
   * registers the default transformers.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerDefaultTransformers() {
    TransformerPack.DEFAULT.accept(this);
    return this;
  }

  /**
   * registers the serializers.
   *
   * @param serializers the serializers to register.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerSerializers(@NotNull final ObjectSerializer<?>... serializers) {
    Collections.addAll(this.serializers, serializers);
    return this;
  }

  /**
   * registers the given transformer into the pool.
   *
   * @param transformer the transformer to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTransformer(@NotNull final Transformer<?, ?> transformer) {
    return this.registerTransformer(transformer.getPair(), transformer);
  }

  /**
   * registers the given transformer into the pool.
   *
   * @param transformer the transformer to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTransformer(@NotNull final TwoSideTransformer<?, ?> transformer) {
    return this.registerTransformer((Transformer<?, ?>) transformer)
      .registerTransformer((Transformer<?, ?>) transformer.reverse());
  }

  /**
   * registers the given transformer into the pool.
   *
   * @param pair the pair to add.
   * @param transformer the transformer to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTransformer(@NotNull final GenericPair pair,
                                             @NotNull final Transformer<?, ?> transformer) {
    this.transformers.put(pair, transformer);
    return this;
  }

  /**
   * registers the given transformer into the pool.
   *
   * @param transformer the transformer to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTransformerReversedToString(@NotNull final Transformer<?, ?> transformer) {
    return this.registerTransformer(transformer)
      .registerTransformer(transformer.getPair().reverse(), new TransformerObjectToString());
  }

  /**
   * updates the declarations.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool update() {
    return this;
  }
}
