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

import io.github.portlek.configs.configuration.ConfigurationSection;
import io.github.portlek.configs.loaders.DataSerializer;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TestData implements DataSerializer {

  /**
   * the fade in time.
   */
  private final int fadeIn;

  /**
   * the fade out time.
   */
  private final int fadeOut;

  /**
   * the stay time.
   */
  private final int stay;

  /**
   * the sub title.
   */
  @Nullable
  private final String subTitle;

  /**
   * the title.
   */
  @Nullable
  private final String title;

  /**
   * ctor.
   *
   * @param title the title.
   * @param subTitle the sub title.
   * @param fadeIn the fade in.
   * @param stay the stay.
   * @param fadeOut the fade out.
   */
  public TestData(@Nullable final String title, @Nullable final String subTitle, final int fadeIn, final int stay,
                  final int fadeOut) {
    this.title = title;
    this.subTitle = subTitle;
    this.fadeIn = fadeIn;
    this.stay = stay;
    this.fadeOut = fadeOut;
  }

  /**
   * gets the sent title from the given section.
   *
   * @param section the section to get.
   *
   * @return a sent title instance at the section path.
   */
  @NotNull
  public static Optional<TestData> deserialize(@NotNull final ConfigurationSection section) {
    final var title = section.getString("title");
    final var subTitle = section.getString("sub-title");
    if (title == null && subTitle == null) {
      return Optional.empty();
    }
    final var fadeIn = section.getInt("fade-in", 20);
    final var stay = section.getInt("stay", 20);
    final var fadeOut = section.getInt("fade-out", 20);
    return Optional.of(new TestData(title, subTitle, fadeIn, stay, fadeOut));
  }

  /**
   * sets the given sent title to the given section.
   *
   * @param section the section to set.
   */
  @Override
  public void serialize(@NotNull final ConfigurationSection section) {
    section.set("title", this.title);
    section.set("sub-title", this.subTitle);
    section.set("fade-in", this.fadeIn);
    section.set("stay", this.stay);
    section.set("fade-out", this.fadeOut);
  }
}
