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

package io.github.portlek.configs.util;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class Version {

    private final int major;

    private final int minor;

    public Version(final int mjr, final int mnr) {
        this.major = mjr;
        this.minor = mnr;
    }

    @NotNull
    public static Version from(@NotNull final String version) {
        if (!version.contains(".")) {
            throw new UnsupportedOperationException("Make sure the string that you want to convert as a " +
                "'io.github.portlek.configs.util.Version' class have '.' between version numbers!");
        }

        final String[] split = version.split("\\.");

        if (split.length != 2) {
            throw new UnsupportedOperationException("Make sure pattern of the string that you want to convert as a " +
                "'io.github.portlek.configs.util.Version' class is like '<any-number>.<any-number>'");
        }

        try {
            return new Version(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1])
            );
        } catch (final NumberFormatException e) {
            throw new UnsupportedOperationException("Make sure pattern of the string that you want to convert as a " +
                "'io.github.portlek.configs.util.Version' class is like '<any-number>.<any-number>'");
        }
    }

    public boolean afterThan(@NotNull final Version version) {
        return this.major > version.getMajor() || this.major == version.getMajor() && this.minor > version.getMinor();
    }

    public int getMajor() {
        return this.major;
    }

    public int getMinor() {
        return this.minor;
    }

    public boolean beforeThan(@NotNull final Version version) {
        return this.major < version.getMajor() || this.getMajor() == version.getMajor() && this.getMinor() < version.getMinor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.major, this.minor);
    }

    @Override
    public boolean equals(final Object vrsn) {
        if (this == vrsn) {
            return true;
        }
        if (vrsn == null || !this.getClass().equals(vrsn.getClass())) {
            return false;
        }
        final Version version = (Version) vrsn;
        return this.major == version.getMajor() &&
            this.minor == version.getMinor();
    }

    public void write(@NotNull final String path, @NotNull final ConfigSection managed) {
        managed.set(path, this.major + "." + this.minor);
    }

}
