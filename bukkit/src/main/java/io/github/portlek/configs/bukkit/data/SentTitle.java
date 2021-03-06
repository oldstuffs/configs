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

package io.github.portlek.configs.bukkit.data;

import com.cryptomorin.xseries.messages.Titles;
import io.github.portlek.configs.configuration.ConfigurationSection;
import java.util.Optional;
import java.util.function.UnaryOperator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that helps developers to send title to players easily.
 */
public final class SentTitle {

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
  public SentTitle(@Nullable final String title, @Nullable final String subTitle, final int fadeIn, final int stay,
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
  public static Optional<SentTitle> deserialize(@NotNull final ConfigurationSection section) {
    final var title = section.getString("title");
    final var subTitle = section.getString("sub-title");
    if (title == null && subTitle == null) {
      return Optional.empty();
    }
    final var fadeIn = section.getInt("fade-in", 20);
    final var stay = section.getInt("stay", 20);
    final var fadeOut = section.getInt("fade-out", 20);
    return Optional.of(new SentTitle(title, subTitle, fadeIn, stay, fadeOut));
  }

  /**
   * sets the given sent title to the given section.
   *
   * @param sentTitle the sent title to set.
   * @param section the section to set.
   */
  public static void serialize(@NotNull final SentTitle sentTitle, @NotNull final ConfigurationSection section) {
    section.set("title", sentTitle.title);
    section.set("sub-title", sentTitle.subTitle);
    section.set("fade-in", sentTitle.fadeIn);
    section.set("stay", sentTitle.stay);
    section.set("fade-out", sentTitle.fadeOut);
  }

  /**
   * sends the title to the given player.
   *
   * @param player the player to send.
   */
  public void send(@NotNull final Player player) {
    Titles.sendTitle(player, this.fadeIn, this.stay, this.fadeOut, this.title, this.subTitle);
  }

  /**
   * sends the title to the given player.
   *
   * @param player the player to send.
   * @param title the title function to send.
   * @param subTitle the sub title function to send.
   */
  public void send(@NotNull final Player player, @NotNull final UnaryOperator<String> title,
                   @NotNull final UnaryOperator<String> subTitle) {
    Titles.sendTitle(player, this.fadeIn, this.stay, this.fadeOut, title.apply(this.title),
      subTitle.apply(this.subTitle));
  }
}
