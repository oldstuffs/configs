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

package io.github.portlek.configs.bukkit.provided;

import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.Provided;
import io.github.portlek.configs.bukkit.util.SentTitle;
import io.github.portlek.configs.util.GeneralUtilities;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class BukkitTitleProvider implements Provided<SentTitle> {

  @NotNull
  @Override
  public Optional<SentTitle> get(@NotNull final CfgSection section, @NotNull final String path) {
    return Optional.empty();
  }

  @NotNull
  @Override
  public Optional<SentTitle> getWithField(@NotNull final SentTitle sentTitle,
                                          @NotNull final CfgSection section, @NotNull final String path) {
    final String fnlpath = GeneralUtilities.putDot(path);
    final Optional<String> title = section.getString(fnlpath + "title");
    final Optional<String> subTitle = section.getString(fnlpath + "sub-title");
    final Optional<Integer> fadeIn = section.getInteger(fnlpath + "fade-in");
    final Optional<Integer> showTime = section.getInteger(fnlpath + "show-time");
    final Optional<Integer> fadeOut = section.getInteger(fnlpath + "fade-out");
    if (!title.isPresent() || !subTitle.isPresent() || !fadeIn.isPresent() || !showTime.isPresent() ||
      !fadeOut.isPresent()) {
      return Optional.empty();
    }
    return Optional.of(new SentTitle(sentTitle.getTitle().value(title.get()),
      sentTitle.getSubTitle().value(subTitle.get()), fadeIn.get(), showTime.get(), fadeOut.get()));
  }

  @Override
  public void set(@NotNull final SentTitle title, @NotNull final CfgSection section,
                  @NotNull final String path) {
    final String fnlpath = GeneralUtilities.putDot(path);
    section.set(fnlpath + "title", title.getTitle().getValue());
    section.set(fnlpath + "sub-title", title.getSubTitle().getValue());
    section.set(fnlpath + "fade-in", title.getFadeIn());
    section.set(fnlpath + "show-time", title.getShowTime());
    section.set(fnlpath + "fade-out", title.getFadeOut());
  }
}
