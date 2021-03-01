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

package io.github.portlek.configs.test;

import static io.github.portlek.configs.Pth.comment;
import static io.github.portlek.configs.Pth.string;
import io.github.portlek.configs.Config;
import io.github.portlek.configs.Configs;
import io.github.portlek.configs.JsonType;
import io.github.portlek.configs.Pth;
import java.io.File;

public final class TestConfig implements Config {

  public static final Pth<String> TEST_STRING = comment(string("path.to.string",
    "default-value"), "Top comment");

  public static void main(final String[] args) {
    Configs.builder()
      .setFileName("test")
      .setFilePath(new File("."))
      .setConfigType(JsonType.get())
      .setToSave(new TestConfig())
      .build();
  }
}
