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

import io.github.portlek.configs.transformer.transformers.TransformerObjectToString;
import io.github.portlek.configs.transformer.transformers.TransformerStringListToRpList;
import io.github.portlek.configs.transformer.transformers.TransformerStringToAddress;
import io.github.portlek.configs.transformer.transformers.TransformerStringToBigDecimal;
import io.github.portlek.configs.transformer.transformers.TransformerStringToBigInteger;
import io.github.portlek.configs.transformer.transformers.TransformerStringToBoolean;
import io.github.portlek.configs.transformer.transformers.TransformerStringToByte;
import io.github.portlek.configs.transformer.transformers.TransformerStringToCharacter;
import io.github.portlek.configs.transformer.transformers.TransformerStringToDouble;
import io.github.portlek.configs.transformer.transformers.TransformerStringToFloat;
import io.github.portlek.configs.transformer.transformers.TransformerStringToInteger;
import io.github.portlek.configs.transformer.transformers.TransformerStringToLocale;
import io.github.portlek.configs.transformer.transformers.TransformerStringToLong;
import io.github.portlek.configs.transformer.transformers.TransformerStringToRpString;
import io.github.portlek.configs.transformer.transformers.TransformerStringToShort;
import io.github.portlek.configs.transformer.transformers.TransformerStringToString;
import io.github.portlek.configs.transformer.transformers.TransformerStringToUniqueId;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

/**
 * an interface to determine transform packs.
 */
public interface TransformPack extends Consumer<@NotNull TransformRegistry> {

  /**
   * the default transformers.
   */
  Collection<Transformer<?, ?>> DEFAULT_TRANSFORMERS = Set.of(
    new TransformerObjectToString(),
    new TransformerStringToString(),
    new TransformerStringToAddress(),
    new TransformerStringToRpString(),
    new TransformerStringListToRpList());

  /**
   * the default transformers reversed to string.
   */
  Collection<Transformer<?, ?>> DEFAULT_TRANSFORMERS_REVERSED_TO_STRING = Set.of(
    new TransformerStringToBigDecimal(),
    new TransformerStringToBigInteger(),
    new TransformerStringToBoolean(),
    new TransformerStringToByte(),
    new TransformerStringToCharacter(),
    new TransformerStringToDouble(),
    new TransformerStringToFloat(),
    new TransformerStringToInteger(),
    new TransformerStringToLocale(),
    new TransformerStringToLong(),
    new TransformerStringToShort(),
    new TransformerStringToUniqueId());

  /**
   * the default transform pack.
   */
  TransformPack DEFAULT = TransformPack.create(registry -> registry
    .withTransformers(TransformPack.DEFAULT_TRANSFORMERS)
    .withTransformersReversedToString(TransformPack.DEFAULT_TRANSFORMERS_REVERSED_TO_STRING));

  /**
   * creates a simple transform pack instance.
   *
   * @param consumer the consumer to create.
   *
   * @return a newly created transform pack.
   */
  @NotNull
  static TransformPack create(@NotNull final Consumer<@NotNull TransformRegistry> consumer) {
    return new Impl(consumer);
  }

  /**
   * a simple implementation of {@link TransformPack}.
   */
  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  final class Impl implements TransformPack {

    /**
     * the delegation.
     */
    @NotNull
    @Delegate
    private final Consumer<@NotNull TransformRegistry> delegation;
  }
}
