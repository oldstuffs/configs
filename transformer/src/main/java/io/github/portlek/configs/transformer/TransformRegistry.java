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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TransformRegistry {

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
  public TransformRegistry withDefaultTransformers() {
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
  public TransformRegistry withSerializers(@NotNull final ObjectSerializer<?>... serializers) {
    Collections.addAll(this.serializers, serializers);
    return this;
  }

  /**
   * accepts the packs.
   *
   * @param packs the packs to accepts.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformRegistry withTransformPacks(@NotNull final TransformerPack... packs) {
    for (final var pack : packs) {
      pack.accept(this);
    }
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
  public TransformRegistry withTransformer(@NotNull final GenericPair pair,
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
  public TransformRegistry withTransformers(@NotNull final Transformer<?, ?>... transformers) {
    return this.withTransformers(Set.of(transformers));
  }

  /**
   * registers the given transformers into the pool.
   *
   * @param transformers the transformers to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformRegistry withTransformers(@NotNull final Collection<Transformer<?, ?>> transformers) {
    for (final var transformer : transformers) {
      if (transformer instanceof TwoSideTransformer<?, ?>) {
        this.withTwoSideTransformers((TwoSideTransformer<?, ?>) transformer);
      } else {
        this.withTransformer(transformer.getPair(), transformer);
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
  public TransformRegistry withTransformersReversedToString(@NotNull final Transformer<?, ?>... transformers) {
    return this.withTransformersReversedToString(Set.of(transformers));
  }

  /**
   * registers the given transformers into the pool.
   *
   * @param transformers the transformers to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformRegistry withTransformersReversedToString(@NotNull final Collection<Transformer<?, ?>> transformers) {
    this.withTransformers(transformers);
    for (final var transformer : transformers) {
      this.withTransformer(transformer.getPair().reverse(), new TransformerObjectToString());
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
  public TransformRegistry withTwoSideTransformers(@NotNull final TwoSideTransformer<?, ?>... transformers) {
    for (final var transformer : transformers) {
      this.withTransformer(transformer.getPair(), transformer);
    }
    Arrays.stream(transformers)
      .map(TwoSideTransformer::reverse)
      .forEach(transformer -> this.withTransformer(transformer.getPair(), transformer));
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
  public TransformRegistry withTwoSideTransformers(@NotNull final Collection<TwoSideTransformer<?, ?>> transformers) {
    for (final var transformer : transformers) {
      this.withTransformer(transformer.getPair(), transformer);
    }
    transformers.stream()
      .map(TwoSideTransformer::reverse)
      .forEach(transformer -> this.withTransformer(transformer.getPair(), transformer));
    return this;
  }
}
