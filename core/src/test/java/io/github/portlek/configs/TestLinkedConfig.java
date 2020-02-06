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
import io.github.portlek.configs.util.Replaceable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@LinkedConfig(configs = {
    @Config(
        name = "en"
    ),
    @Config(
        name = "tr"
    )
})
public final class TestLinkedConfig extends LinkedManagedBase {

    @NotNull
    private final Map<String, Supplier<String>> prefix = new HashMap<>();

    public TestLinkedConfig(@NotNull TestConfig testConfig) {
        super(testConfig.plugin_language);
    }

    @NotNull
    public Map<String, Supplier<String>> getPrefix() {
        if (!prefix.containsKey("%prefix%")) {
            prefix.put("%prefix%", () -> "Test");
        }

        return prefix;
    }

    @Value
    public Replaceable<String> test_2 = match(s -> {
        if (s.equals("en")) {
            return Optional.of(
                Replaceable.of("%prefix% English words!")
                    .replace(getPrefix())
            );
        } else if (s.equals("tr")) {
            return Optional.of(
                Replaceable.of("%prefix% Turkish words!")
                    .replace(getPrefix())
            );
        }

        return Optional.empty();
    });

    @Value
    public Replaceable<String> test = match(s -> {
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

}
