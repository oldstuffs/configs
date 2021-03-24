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

package io.github.portlek.configs.lang;

import io.github.portlek.configs.ConfigLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents language file loaders.
 */
@RequiredArgsConstructor
public final class LangLoader {

  /**
   * the config builders.
   */
  @NotNull
  private final Map<String, ConfigLoader.Builder> builders;

  /**
   * the keys.
   */
  @Nullable
  private List<String> keys;

  @NotNull
  public Optional<String> getDefaultLanguage() {
    if (this.builders.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(this.getKeys().get(0));
  }

  /**
   * obtains the lang keys.
   *
   * @return lang keys.
   */
  @NotNull
  public List<String> getKeys() {
    if (this.keys == null) {
      this.keys = new ArrayList<>(this.builders.keySet());
    }
    return this.keys;
  }
}
