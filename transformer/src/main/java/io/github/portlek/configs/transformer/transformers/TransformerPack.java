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

package io.github.portlek.configs.transformer.transformers;

import io.github.portlek.configs.transformer.TransformerPool;
import io.github.portlek.configs.transformer.transformers.defaults.TransformerObjectToString;
import io.github.portlek.configs.transformer.transformers.defaults.TransformerStringToString;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

/**
 * an interface to determine transformer packs.
 */
public interface TransformerPack extends Consumer<@NotNull TransformerPool> {

  /**
   * the default transformer pack.
   */
  TransformerPack DEFAULT = TransformerPack.create(pool -> {
    pool.registerTransformer(new TransformerObjectToString());
    pool.registerTransformer(new TransformerStringToString());
  });

  /**
   * creates a simple transformer pack instance.
   *
   * @param consumer the consumer to create.
   *
   * @return a newly created transformer pack.
   */
  @NotNull
  static TransformerPack create(@NotNull final Consumer<@NotNull TransformerPool> consumer) {
    return new Impl(consumer);
  }

  /**
   * a simple implementation of {@link TransformerPack}.
   */
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  final class Impl implements TransformerPack {

    /**
     * the delegation.
     */
    @NotNull
    @Delegate
    private final Consumer<@NotNull TransformerPool> delegation;
  }
}
