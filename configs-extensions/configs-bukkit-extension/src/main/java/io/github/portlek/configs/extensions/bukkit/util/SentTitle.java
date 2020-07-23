/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
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

package io.github.portlek.configs.extensions.bukkit.util;

import com.cryptomorin.xseries.messages.Titles;
import io.github.portlek.mapentry.MapEntry;
import io.github.portlek.replaceable.rp.RpString;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
public final class SentTitle {

    @NotNull
    private final RpString title;

    @NotNull
    private final RpString subTitle;

    private final int fadeIn;

    private final int showTime;

    private final int fadeOut;

    @NotNull
    public void buildAndSend(@NotNull final Player player, @NotNull final String regex, @NotNull final Supplier<String> replace) {
        this.buildAndSend(player, MapEntry.from(regex, replace));
    }

    @SafeVarargs
    @NotNull
    public final void buildAndSend(@NotNull final Player player, @NotNull final Map.Entry<String, Supplier<String>>... entries) {
        this.buildAndSend(player, Arrays.asList(entries));
    }

    @NotNull
    public void buildAndSend(@NotNull final Player player,
                             @NotNull final Iterable<Map.Entry<String, Supplier<String>>> entries) {
        this.buildAndSend(player, new HashMap<String, Supplier<String>>() {{
            entries.forEach(entry ->
                this.put(entry.getKey(), entry.getValue()));
        }});
    }

    @NotNull
    public void buildAndSend(@NotNull final Player player) {
        this.buildAndSend(player, Collections.emptyMap());
    }

    @NotNull
    public void buildAndSend(@NotNull final Player player, @NotNull final Map<String, Supplier<String>> replaces) {
        this.send(player, this.title.build(replaces), this.subTitle.build(replaces));
    }

    private void send(@NotNull final Player player, @NotNull final String builttitle,
                      @NotNull final String builtsubtitle) {
        Titles.sendTitle(player, this.fadeIn, this.showTime, this.fadeOut,
            builttitle.isEmpty() ? null : builttitle,
            builtsubtitle.isEmpty() ? null : builttitle);
    }

}
