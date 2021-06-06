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
import io.github.portlek.configs.transformer.transformers.Transformer;
import io.github.portlek.configs.transformer.transformers.TransformerPack;
import io.github.portlek.configs.transformer.transformers.TwoSideTransformer;
import io.github.portlek.configs.transformer.transformers.defaults.TransformerObjectToString;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
   * the data.
   */
  @NotNull
  private final TransformedData transformedData = new TransformedData(this);

  /**
   * the transformers by id.
   */
  @NotNull
  private final Map<GenericPair, Transformer<?, ?>> transformers = new ConcurrentHashMap<>();

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   * @param registerDefaultTransformers the register default transformers to create.
   * @param initializer the initializer to create.
   * @param transformers the transformers to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final Object object, final boolean registerDefaultTransformers,
                                       @NotNull final UnaryOperator<@NotNull TransformerPool> initializer,
                                       @NotNull final Transformer<?, ?>... transformers) {
    final var pool = new TransformerPool(TransformedObjectDeclaration.of(object));
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
   * @param initializer the initializer to create.
   * @param transformers the transformers to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final Object object,
                                       @NotNull final UnaryOperator<@NotNull TransformerPool> initializer,
                                       @NotNull final Transformer<?, ?>... transformers) {
    return TransformerPool.create(object, true, initializer, transformers);
  }

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   * @param registerDefaultTransformers the register default transformers to create.
   * @param transformers the transformers to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final Object object, final boolean registerDefaultTransformers,
                                       @NotNull final Transformer<?, ?>... transformers) {
    return TransformerPool.create(object, registerDefaultTransformers, UnaryOperator.identity(), transformers);
  }

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   * @param transformers the transformers to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final Object object, @NotNull final Transformer<?, ?>... transformers) {
    return TransformerPool.create(object, true, transformers);
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
   * @param transformer the transformer to add.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerTransformerReversedToString(@NotNull final Transformer<?, ?> transformer) {
    return this.registerTransformer(transformer)
      .registerTransformer(transformer.getPair().reverse(), new TransformerObjectToString());
  }
}
