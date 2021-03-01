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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;

class LanguageableTest {

  private final Supplier<String> def = () -> "Default value!";

  private final Supplier<Map<String, String>> values = () -> {
    final Map<String, String> map = new HashMap<>();
    map.put("en_US", "English words!");
    map.put("tr_TR", "Türkçe kelimeler!");
    return map;
  };

  @Test
  void apply() {
    final Languageable<String> languageable = new Languageable<>(this.def, this.values);
    MatcherAssert.assertThat(
      "Languageable couldn't parse the correct value!",
      languageable.apply("en_US"),
      new IsEqual<>("English words!")
    );
    MatcherAssert.assertThat(
      "Languageable couldn't parse the correct value!",
      languageable.apply("tr_TR"),
      new IsEqual<>("Türkçe kelimeler!")
    );
  }
}