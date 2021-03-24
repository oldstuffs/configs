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

package io.github.portlek.configs;

import io.github.portlek.configs.annotation.Route;
import io.github.portlek.configs.lang.LangLoader;
import io.github.portlek.configs.lang.LangValue;
import io.github.portlek.configs.yaml.YamlType;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.cactoos.map.MapEntry;

@RequiredArgsConstructor
public final class LangTest implements ConfigHolder {

  @Route("test-1")
  public static final LangValue<String> test1 = LangValue.create(String.class,
    new MapEntry<>("en_US", "English."),
    new MapEntry<>("tr_TR", "Türkçe."));

  public static void main(final String[] args) {
    final var here = Path.of(System.getProperty("user.dir"));
    final var loader = LangLoader.builder()
      .addBuilder("en_US", "en", here, YamlType.get())
      .addBuilder("tr_TR", "tr", here, YamlType.get())
      .build();
  }
}
