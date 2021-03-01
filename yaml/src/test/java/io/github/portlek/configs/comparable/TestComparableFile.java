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

package io.github.portlek.configs.comparable;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.annotations.LinkedFile;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.type.YamlFileType;
import io.github.portlek.configs.util.Languageable;
import org.jetbrains.annotations.NotNull;

@LinkedConfig({
  @LinkedFile(
    key = "en",
    config = @Config(
      type = YamlFileType.class,
      name = "en_US"
    )
  ),
  @LinkedFile(
    key = "tr",
    config = @Config(
      type = YamlFileType.class,
      name = "tr_TR"
    )
  )
})
public final class TestComparableFile extends ComparableManaged<TestComparableFile> {

  @Property
  public Languageable<String> test = this.languageable(() -> "default-value", (key, defaultValue) -> {
    if ("en".equals(key)) {
      return "english words!";
    }
    if ("tr".equals(key)) {
      return "türkçe kelimeler!";
    }
    return defaultValue;
  });

  @NotNull
  @Override
  public TestComparableFile self() {
    return this;
  }
}
