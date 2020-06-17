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

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.annotations.LinkedFile;
import io.github.portlek.configs.annotations.Property;
import io.github.portlek.configs.replaceable.Replaceable;
import io.github.portlek.configs.replaceable.ReplaceableString;
import io.github.portlek.configs.structure.comparable.ComparableManaged;
import io.github.portlek.configs.util.Languageable;
import org.jetbrains.annotations.NotNull;

@LinkedConfig({
    @LinkedFile(
        key = "TR",
        config = @Config(
            value = "tr",
            location = "%basedir%/Test",
            resourcePath = "tr",
            copyDefault = true
        )
    ),
    @LinkedFile(
        key = "EN",
        config = @Config(
            value = "en",
            location = "%basedir%/Test",
            resourcePath = "en",
            copyDefault = true
        )
    )
})
public final class PortalConfig extends ComparableManaged<PortalConfig> {

    @Property
    public Languageable<ReplaceableString> test = this.languageable(
        () -> Replaceable.from(""),
        (s, replaceable) -> replaceable
            .replaces("%player_name%"));

    @Property
    public Languageable<ReplaceableString> test_2 = this.languageable(
        () -> Replaceable.from(""),
        (s, replaceable) -> replaceable
            .replaces("%player_name%"));

    public static void main(String[] args) {
        final PortalConfig portalConfig = new PortalConfig();
        portalConfig.load();

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
