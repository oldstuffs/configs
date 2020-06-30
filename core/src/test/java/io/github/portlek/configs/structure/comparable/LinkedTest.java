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

package io.github.portlek.configs.structure.comparable;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.annotations.LinkedFile;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.replaceable.Replaceable;
import io.github.portlek.configs.replaceable.ReplaceableString;
import io.github.portlek.configs.structure.linked.LinkedManaged;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(@LinkedFile(
    key = "en",
    config = @Config(
        value = "en",
        location = "%basedir%/Test"
    )
))
public final class LinkedTest extends LinkedManaged {

    @Property
    public ReplaceableString help_messages = this.match(s -> {
        if ("en".equals(s)) {
            return Optional.of(
                Replaceable.from(
                    new StringBuilder()
                        .append("&a====== %prefix% Player Commands &a======")
                        .append('\n')
                        .append("&7/kekorank &r> &eShows help message.")
                        .append('\n')
                        .append("&7/kekorank help &r> &eShows help message.")
                        .append('\n')
                        .append("&7/rank menu &r> &eOpens your profile menu.")
                        .append('\n')
                        .append("&7/rank check &r> &eCheck and promote your rank.")
                        .append('\n')
                        .append("&7/rank list &r> &eShows all ranks.")
                        .append('\n')
                        .append("&a====== %prefix% Admin Commands &a======")
                        .append('\n')
                        .append("&7/kekorank reload &r> &eReloads the plugin.")
                        .append('\n')
                        .append("&7/rank version &r> &eChecks for update.")
                        .append('\n')
                        .append("&7/rank promote <player> &r> &eInstantly promote the player.")
                        .append('\n')
                        .append("&7/rank set <player> <rank> &r> &eChange the player rank.")
                        .append('\n')
                        .append("&7/rank menu <target> &r> &eShows profile menu of the target.")));
        }
        return Optional.empty();
    });

    public LinkedTest(@NotNull final Supplier<String> chosen) {
        super(chosen);
    }

}
