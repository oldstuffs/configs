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

import io.github.portlek.configs.transformer.declaration.TransformedObjectDeclaration;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
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
   * the default transformers.
   */
  private static final Set<Transformer<?, ?>> DEFAULT_TRANSFORMERS = Set.of(
    Transformer.create(String.class, String.class, Function.identity()));

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
  private final Map<Class<?>, Transformer<?, ?>> transformersByClass = new ConcurrentHashMap<>();

  /**
   * the transformers by id.
   */
  @NotNull
  private final Map<String, Transformer<?, ?>> transformersById = new ConcurrentHashMap<>();

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   * @param registerDefaultTransformers the register default transformers to create.
   * @param initializer the initializer to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final Object object, final boolean registerDefaultTransformers,
                                       @NotNull final UnaryOperator<@NotNull TransformerPool> initializer) {
    final var pool = new TransformerPool(TransformedObjectDeclaration.of(object));
    return initializer.apply(registerDefaultTransformers
      ? pool.registerDefaultTransformers()
      : pool);
  }

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   * @param registerDefaultTransformers the register default transformers to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final Object object, final boolean registerDefaultTransformers) {
    final var pool = new TransformerPool(TransformedObjectDeclaration.of(object));
    return registerDefaultTransformers
      ? pool.registerDefaultTransformers()
      : pool;
  }

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final Object object) {
    return new TransformerPool(TransformedObjectDeclaration.of(object))
      .registerDefaultTransformers();
  }

  /**
   * creates a new instance of transformer pool.
   *
   * @param object the object to create.
   * @param initializer the initializer to create.
   *
   * @return a newly created transformer pool.
   */
  @NotNull
  public static TransformerPool create(@NotNull final Object object,
                                       @NotNull final UnaryOperator<@NotNull TransformerPool> initializer) {
    return initializer.apply(new TransformerPool(TransformedObjectDeclaration.of(object))
      .registerDefaultTransformers());
  }

  /**
   * gets the transformer from the final value's type.
   *
   * @param cls the cls to get.
   * @param <R> type tof the raw value.
   * @param <F> type of the final value.
   * @param <T> type of the transformer.
   *
   * @return transformer by class.
   */
  @NotNull
  public <R, F, T extends Transformer<R, F>> Optional<T> getTransformerByClass(@NotNull final Class<F> cls) {
    //noinspection unchecked
    return Optional.ofNullable(this.transformersByClass.get(cls))
      .filter(transformer -> Objects.equals(transformer.getFinalType(), cls))
      .map(transformer -> (T) transformer);
  }

  /**
   * gets the transformer from the id.
   *
   * @param id the id to get.
   *
   * @return transformer by id.
   */
  @NotNull
  public Optional<Transformer<?, ?>> getTransformerById(@NotNull final String id) {
    return Optional.ofNullable(this.transformersById.get(id));
  }

  /**
   * registers the default transformers.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public TransformerPool registerDefaultTransformers() {
    TransformerPool.DEFAULT_TRANSFORMERS.forEach(this::registerTransformer);
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
    this.transformersById.put(transformer.getId(), transformer);
    this.transformersByClass.put(transformer.getFinalType(), transformer);
    return this;
  }
}
