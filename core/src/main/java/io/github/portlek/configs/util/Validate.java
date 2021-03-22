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

package io.github.portlek.configs.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an utility class that helps developer to write simple expressions.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validate {

  /**
   * checks if the text is empty.
   *
   * @param text the text to check.
   * @param errorMessage the error message to check.
   *
   * @throws IllegalStateException if the given text is empty.
   */
  public static void checkEmpty(final String text, final String errorMessage) {
    if (text.isEmpty()) {
      throw new IllegalStateException(errorMessage);
    }
  }

  /**
   * checks if the given object is not null.
   *
   * @param object the object to check.
   * @param errorMessage the error message to check.
   * @param args the args to check.
   *
   * @throws IllegalStateException if the given object is null.
   */
  public static void checkNull(@Nullable final Object object, @NotNull final String errorMessage,
                               @NotNull final Object... args) {
    if (object == null) {
      throw new IllegalStateException(String.format(errorMessage, args));
    }
  }
}
