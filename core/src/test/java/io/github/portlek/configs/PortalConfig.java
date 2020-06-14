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

package io.github.portlek.configs;

import io.github.portlek.configs.annotations.ComparableConfig;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.languageable.Languageable;
import io.github.portlek.configs.replaceable.Replaceable;
import io.github.portlek.configs.replaceable.ReplaceableString;
import io.github.portlek.configs.structure.comparable.ComparableManaged;
import io.github.portlek.configs.util.MapEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

@ComparableConfig({
    @Config(
        value = "portal",
        copyDefault = true,
        location = "%basedir%/MinecraftEvi/languages",
        resourcePath = "tr"
    ),
    @Config(
        value = "portal",
        copyDefault = true,
        location = "%basedir%/MinecraftEvi/languages",
        resourcePath = "en"
    )
})
public final class PortalConfig extends ComparableManaged<PortalConfig> {

    private static final String[] LANGUAGES = {
        "TR", "EN"
    };

    @Property
    public Languageable<ReplaceableString> test = this.languageable(
        ReplaceableString.class,
        PortalConfig.getReplaces(replaceableString ->
            replaceableString.replaces("%player_name%")));

    @Property
    public Languageable<ReplaceableString> test_2 = this.languageable(
        ReplaceableString.class,
        PortalConfig.getReplaces(replaceableString ->
            replaceableString.replaces("%player_name%")));

    @NotNull
    private static List<Map.Entry<Object, ReplaceableString>> getReplaces(
        @NotNull final Function<ReplaceableString, ReplaceableString> func) {
        return Arrays.stream(PortalConfig.LANGUAGES)
            .map(s -> MapEntry.from((Object) s, func.apply(Replaceable.from(""))))
            .collect(Collectors.toList());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onLoad() {
        this.save();
        this.setAutoSave(false);
        this.createSection("test")
            .createSection("test");
    }

    @NotNull
    @Override
    public PortalConfig self() {
        return this;
    }

}
