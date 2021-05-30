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

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * a class that represents transformer pools.
 */
@Getter
public final class TransformerPool {

  /**
   * the default transformers.
   */
  private static final Set<Transformer<?>> DEFAULT_TRANSFORMERS = Set.of(
  );

  /**
   * the data.
   */
  @NotNull
  private final TransformedData transformedData = new TransformedData(this);

  /**
   * the transformers by id.
   */
  @NotNull
  private final Map<Class<?>, Transformer<?>> transformersByClass = new ConcurrentHashMap<>();

  /**
   * the transformers by id.
   */
  @NotNull
  private final Map<String, Transformer<?>> transformersById = new ConcurrentHashMap<>();

  /**
   * gets the transformer from the final value's type.
   *
   * @param cls the cls to get.
   * @param <F> type of the final value.
   * @param <T> type of the transformer.
   *
   * @return transformer by class.
   */
  @NotNull
  public <F, T extends Transformer<F>> Optional<T> getTransformerByClass(@NotNull final Class<F> cls) {
    //noinspection unchecked
    return Optional.ofNullable(this.transformersByClass.get(cls))
      .filter(transformer -> Objects.equals(transformer.getType(), cls))
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
  public Optional<Transformer<?>> getTransformerById(@NotNull final String id) {
    return Optional.ofNullable(this.transformersById.get(id));
  }

  /**
   * registers the default transformers.
   */
  public void registerDefaultTransformers() {
    TransformerPool.DEFAULT_TRANSFORMERS.forEach(this::registerTransformer);
  }

  /**
   * registers the given transformer into the pool.
   *
   * @param transformer the transformer to add.
   */
  public void registerTransformer(@NotNull final Transformer<?> transformer) {
    this.transformersById.put(transformer.getId(), transformer);
    this.transformersByClass.put(transformer.getType(), transformer);
  }
}
