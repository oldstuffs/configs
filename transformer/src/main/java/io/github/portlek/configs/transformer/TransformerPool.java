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
import io.github.portlek.configs.transformer.declarations.TransformedObjectDeclaration;
import io.github.portlek.configs.transformer.generics.GenericPair;
import io.github.portlek.configs.transformer.serializers.ObjectSerializer;
import io.github.portlek.configs.transformer.transformers.Transformer;
import io.github.portlek.configs.transformer.transformers.TransformerPack;
import io.github.portlek.configs.transformer.transformers.TwoSideTransformer;
import io.github.portlek.configs.transformer.transformers.defaults.TransformerObjectToString;
import java.util.Arrays;
import java.util.Collection;
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
import org.jetbrains.annotations.Nullable;

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
    pool.registerTransformers(transformers);
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
   * gets the transformers.
   *
   * @param from the from to get.
   * @param to the to to get.
   *
   * @return transformer instance for from to.
   */
  @NotNull
  public Optional<Transformer<?, ?>> getTransformer(@Nullable final GenericDeclaration from,
                                                    @Nullable final GenericDeclaration to) {
    return Optional.ofNullable(this.transformers.get(GenericPair.of(from, to)));
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
   * registers the given transformers into the pool.
   *
   * @param transformers the transformers to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTransformers(@NotNull final Transformer<?, ?>... transformers) {
    return this.registerTransformers(Set.of(transformers));
  }

  /**
   * registers the given transformers into the pool.
   *
   * @param transformers the transformers to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTransformers(@NotNull final Collection<Transformer<?, ?>> transformers) {
    for (final var transformer : transformers) {
      if (transformer instanceof TwoSideTransformer<?, ?>) {
        this.registerTwoSideTransformers((TwoSideTransformer<?, ?>) transformer);
      } else {
        this.registerTransformer(transformer.getPair(), transformer);
      }
    }
    return this;
  }

  /**
   * registers the given transformers into the pool.
   *
   * @param transformers the transformers to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTransformersReversedToString(@NotNull final Transformer<?, ?>... transformers) {
    return this.registerTransformersReversedToString(Set.of(transformers));
  }

  /**
   * registers the given transformers into the pool.
   *
   * @param transformers the transformers to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTransformersReversedToString(
    @NotNull final Collection<Transformer<?, ?>> transformers) {
    this.registerTransformers(transformers);
    for (final var transformer : transformers) {
      this.registerTransformer(transformer.getPair().reverse(), new TransformerObjectToString());
    }
    return this;
  }

  /**
   * registers the given two side transformers into the pool.
   *
   * @param transformers the transformers to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTwoSideTransformers(@NotNull final TwoSideTransformer<?, ?>... transformers) {
    for (final var transformer : transformers) {
      this.registerTransformer(transformer.getPair(), transformer);
    }
    Arrays.stream(transformers)
      .map(TwoSideTransformer::reverse)
      .forEach(transformer -> this.registerTransformer(transformer.getPair(), transformer));
    return this;
  }

  /**
   * registers the given two side transformers into the pool.
   *
   * @param transformers the transformers to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTwoSideTransformers(@NotNull final Collection<TwoSideTransformer<?, ?>> transformers) {
    for (final var transformer : transformers) {
      this.registerTransformer(transformer.getPair(), transformer);
    }
    transformers.stream()
      .map(TwoSideTransformer::reverse)
      .forEach(transformer -> this.registerTransformer(transformer.getPair(), transformer));
    return this;
  }
}
