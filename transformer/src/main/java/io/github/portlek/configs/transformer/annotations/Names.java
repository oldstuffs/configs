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

package io.github.portlek.configs.transformer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

/**
 * an annotation that controls the pathing of field declarations.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Names {

  /**
   * obtains the modifier.
   *
   * @return modifier.
   */
  Modifier modifier() default Modifier.NONE;

  /**
   * obtains the strategy.
   *
   * @return strategy
   */
  Strategy strategy();

  /**
   * an enum class that contains name modifiers.
   */
  enum Modifier implements UnaryOperator<@NotNull String> {
    /**
     * the none.
     */
    NONE {
      @Override
      public String apply(@NotNull final String s) {
        return s;
      }
    },
    /**
     * the to upper case.
     */
    TO_UPPER_CASE {
      @Override
      public String apply(@NotNull final String s) {
        return s.toUpperCase(Locale.ROOT);
      }
    },
    /**
     * the to lower case.
     */
    TO_LOWER_CASE {
      @Override
      public String apply(@NotNull final String s) {
        return s.toLowerCase(Locale.ROOT);
      }
    }
  }

  /**
   * an enum class that contains name strategies.
   */
  enum Strategy implements UnaryOperator<@NotNull String> {
    /**
     * the identity.
     */
    IDENTITY("", ""),
    /**
     * the snake case.
     */
    SNAKE_CASE("$1_$2", "(\\G(?!^)|\\b(?:[A-Z]{2}|[a-zA-Z][a-z]*))(?=[a-zA-Z]{2,}|\\d)([A-Z](?:[A-Z]|[a-z]*)|\\d+)"),
    /**
     * the hyphen case.
     */
    HYPHEN_CASE("$1-$2", "(\\G(?!^)|\\b(?:[A-Z]{2}|[a-zA-Z][a-z]*))(?=[a-zA-Z]{2,}|\\d)([A-Z](?:[A-Z]|[a-z]*)|\\d+)");

    /**
     * the regex.
     */
    @NotNull
    private final Pattern pattern;

    /**
     * the replacement.
     */
    @NotNull
    private final String replacement;

    /**
     * ctor.
     *
     * @param replacement the replacement.
     * @param regex the regex.
     */
    Strategy(@NotNull final String replacement, @NotNull final String regex) {
      this.replacement = replacement;
      this.pattern = Pattern.compile(regex);
    }

    @Override
    public String apply(@NotNull final String s) {
      return this.pattern.matcher(s).replaceAll(this.replacement);
    }
  }
}
