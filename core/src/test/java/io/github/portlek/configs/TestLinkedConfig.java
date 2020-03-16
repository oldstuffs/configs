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
import io.github.portlek.configs.annotations.Value;
import io.github.portlek.configs.util.MapEntry;
import io.github.portlek.configs.util.Replaceable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

@LinkedConfig(configs = {
    @Config(
        name = "en"
    ),
    @Config(
        name = "tr"
    )
})
public final class TestLinkedConfig extends LinkedManagedBase {

    @Value
    public Replaceable<String> test_2 = this.match(s -> {
        if (s.equals("en")) {
            return Optional.of(
                Replaceable.of("%prefix% English words!")
                    .replace(this.getPrefix())
            );
        } else if (s.equals("tr")) {
            return Optional.of(
                Replaceable.of("%prefix% Turkish words!")
                    .replace(this.getPrefix())
            );
        }

        return Optional.empty();
    });

    @Value
    public Replaceable<String> test = this.match(s -> {
        if (s.equals("en")) {
            return Optional.of(
                Replaceable.of("English words!")
            );
        } else if (s.equals("tr")) {
            return Optional.of(
                Replaceable.of("Turkish words!")
            );
        }

        return Optional.empty();
    });

    public TestLinkedConfig(@NotNull final TestConfig testConfig) {
        super(testConfig.plugin_language, MapEntry.from("config", testConfig));
    }

    @NotNull
    public Map<String, Supplier<String>> getPrefix() {
        final Map<String, Supplier<String>> prefix = new HashMap<>();
        this.pull("config").ifPresent(o ->
            prefix.put("%prefix%", () -> ((TestConfig) o).plugin_language));
        return prefix;
    }

}
