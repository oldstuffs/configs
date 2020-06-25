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

import io.github.portlek.configs.annotations.*;
import io.github.portlek.configs.structure.section.ConfigSection;
import io.github.portlek.configs.util.Languageable;
import org.jetbrains.annotations.NotNull;

@LinkedConfig({
    @LinkedFile(
        key = "TR",
        config = @Config(
            value = "tr",
            copyDefault = true,
            location = "%basedir%/Test"
        )
    ),
    @LinkedFile(
        key = "EN",
        config = @Config(
            value = "en",
            copyDefault = true,
            location = "%basedir%/Test"
        )
    )
})
public final class ComparableConfigTest extends ComparableManaged<ComparableConfigTest> {

    @Instance
    public final ComparableConfigTest.TestSection testSection = new ComparableConfigTest.TestSection();

    @Property
    public Languageable<String> test_1 = this.languageable(() -> "", (s, s2) ->
        s);

    @Property
    public Languageable<String> test_2 = this.languageable(() -> "", (s, s2) ->
        s);

    @NotNull
    @Override
    public ComparableConfigTest self() {
        return this;
    }

    @Section("test-section")
    public final class TestSection extends ConfigSection {

        @Property
        public Languageable<String> test_1 = ComparableConfigTest.this.languageable(() -> "", (s, s2) ->
            s);

        @Property
        public Languageable<String> test_2 = ComparableConfigTest.this.languageable(() -> "", (s, s2) ->
            s);

    }

}
