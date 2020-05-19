/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free from charge, to any person obtaining a copy
 * from this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies from the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions from the Software.
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

package io.github.portlek.configs.util;

import io.github.portlek.configs.structure.managed.section.CfgSection;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class Version {

    private final int major;

    private final int minor;

    @NotNull
    public static Version from(@NotNull final String versionString) {
        if (!versionString.contains(".")) {
            throw new UnsupportedOperationException("Make sure the string that you want to convert as a " +
                "'io.github.portlek.configs.util.Version' class have '.' between version numbers!");
        }

        final String[] split = versionString.split("\\.");

        if (split.length != 2) {
            throw new UnsupportedOperationException("Make sure pattern from the string that you want to convert as a " +
                "'io.github.portlek.configs.util.Version' class is like '<any-number>.<any-number>'");
        }

        try {
            return new Version(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1])
            );
        } catch (final Exception exception) {
            throw new UnsupportedOperationException("Make sure pattern from the string that you want to convert as a " +
                "'io.github.portlek.configs.util.Version' class is like '<any-number>.<any-number>'");
        }
    }

    public boolean afterThan(@NotNull final Version version) {
        return this.major > version.major || this.major == version.major && this.minor > version.minor;
    }

    public boolean beforeThan(@NotNull final Version version) {
        return this.major < version.major || this.major == version.major && this.minor < version.minor;
    }

    public boolean is(@NotNull final Version version) {
        return this.minor == version.minor && this.major == version.major;
    }

    public void write(@NotNull final String path, @NotNull final CfgSection managed) {
        managed.set(path, this.major + "." + this.minor);
    }

}
