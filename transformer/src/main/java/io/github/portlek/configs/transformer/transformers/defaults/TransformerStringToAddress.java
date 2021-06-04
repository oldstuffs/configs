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

package io.github.portlek.configs.transformer.transformers.defaults;

import io.github.portlek.configs.transformer.transformers.TwoSideTransformer;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents transformers between {@link String} and {@link InetSocketAddress}.
 */
public final class TransformerStringToAddress extends TwoSideTransformer.Base<String, InetSocketAddress> {

  /**
   * ctor.
   */
  public TransformerStringToAddress() {
    super(String.class, InetSocketAddress.class, TransformerStringToAddress::toAddress,
      TransformerStringToAddress::toAddress);
  }

  @NotNull
  private static String toAddress(@NotNull final InetSocketAddress address) {
    return address.getHostName() + ":" + address.getPort();
  }

  /**
   * converts the given string into {@link InetSocketAddress}.
   *
   * @param address the address to convert.
   *
   * @return converted {@link InetSocketAddress} instance.
   */
  @Nullable
  private static InetSocketAddress toAddress(@NotNull final String address) {
    final var trim = address.trim();
    final var strings = trim.split(":");
    if (strings.length != 2) {
      return null;
    }
    return new InetSocketAddress(strings[0], new BigDecimal(strings[1]).intValueExact());
  }
}
