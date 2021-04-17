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
import io.github.portlek.configs.loaders.DataSerializer;
import io.github.portlek.replaceable.RpString;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that helps developers to send title to players easily.
 */
public final class SentTitle implements DataSerializer {

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
  private final RpString subTitle;

  /**
   * the title.
   */
  @Nullable
  private final RpString title;

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
    this(title == null ? null : RpString.from(title),
      subTitle == null ? null : RpString.from(subTitle),
      fadeIn, stay, fadeOut);
  }

  /**
   * ctor.
   *
   * @param title the title.
   * @param subTitle the sub title.
   * @param fadeIn the fade in.
   * @param stay the stay.
   * @param fadeOut the fade out.
   */
  public SentTitle(@Nullable final RpString title, @Nullable final RpString subTitle, final int fadeIn, final int stay,
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
   * @param fieldValue the field value to deserialize.
   *
   * @return a sent title instance at the section path.
   */
  @NotNull
  public static Optional<SentTitle> deserialize(@NotNull final ConfigurationSection section,
                                                @Nullable final SentTitle fieldValue) {
    final var title = section.getString("title");
    final var subTitle = section.getString("sub-title");
    if (title == null && subTitle == null) {
      return Optional.empty();
    }
    final var fadeIn = section.getInt("fade-in", 20);
    final var stay = section.getInt("stay", 20);
    final var fadeOut = section.getInt("fade-out", 20);
    final RpString fieldTitle;
    final RpString fieldSubTitle;
    if (fieldValue == null || fieldValue.title == null) {
      fieldTitle = title == null ? null : RpString.from(title);
    } else {
      fieldTitle = title == null ? null : fieldValue.title.value(title);
    }
    if (fieldValue == null || fieldValue.subTitle == null) {
      fieldSubTitle = subTitle == null ? null : RpString.from(subTitle);
    } else {
      fieldSubTitle = subTitle == null ? null : fieldValue.subTitle.value(subTitle);
    }
    return Optional.of(new SentTitle(fieldTitle, fieldSubTitle, fadeIn, stay, fadeOut));
  }

  /**
   * sends the title to the given player.
   *
   * @param player the player to send.
   * @param entries the entries to send.
   */
  @SafeVarargs
  public final void send(@NotNull final Player player,
                         @NotNull final Map.Entry<String, Supplier<String>>... entries) {
    Titles.sendTitle(player, this.fadeIn, this.stay, this.fadeOut,
      this.title == null ? null : this.title.build(entries),
      this.subTitle == null ? null : this.subTitle.build(entries));
  }

  /**
   * sends the title to the given player.
   *
   * @param player the player to send.
   * @param title the title function to send.
   * @param subTitle the sub title function to send.
   * @param entries the entries to send.
   */
  @SafeVarargs
  public final void send(@NotNull final Player player, @NotNull final UnaryOperator<String> title,
                         @NotNull final UnaryOperator<String> subTitle,
                         @NotNull final Map.Entry<String, Supplier<String>>... entries) {
    Titles.sendTitle(player, this.fadeIn, this.stay, this.fadeOut,
      this.title == null ? null : title.apply(this.title.build(entries)),
      this.subTitle == null ? null : subTitle.apply(this.subTitle.build(entries)));
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
