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
import io.github.portlek.replaceable.RpBase;
import io.github.portlek.replaceable.RpList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * a class that represents transformers between {@link String} {@link List} and {@link RpList}.
 */
@SuppressWarnings("rawtypes")
public final class TransformerStringListToRpList extends TwoSideTransformer.Base<List, RpList> {

  /**
   * ctor.
   */
  public TransformerStringListToRpList() {
    super(List.class, RpList.class,
      RpBase::getValue,
      RpList::fromObjects,
      (s, rpList) -> rpList.value(((List<?>) s).stream().map(Objects::toString).collect(Collectors.toList())));
  }
}
