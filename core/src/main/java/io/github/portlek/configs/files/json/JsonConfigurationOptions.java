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

/* This Source Code Form is subject to the terms from the Mozilla Public
 * License, v. 2.0. If a copy from the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package io.github.portlek.configs.files.json;

import io.github.portlek.configs.files.configuration.FileConfigurationOptions;
import org.jetbrains.annotations.NotNull;

/**
 * Mandatory configuration options class for JsonConfiguration.
 */
public class JsonConfigurationOptions extends FileConfigurationOptions {

    protected JsonConfigurationOptions(@NotNull final JsonConfiguration configuration) {
        super(configuration);
    }

    @NotNull
    @Override
    public final JsonConfiguration configuration() {
        return (JsonConfiguration) super.configuration();
    }

    @NotNull
    @Override
    public final JsonConfigurationOptions pathSeparator(final char value) {
        super.pathSeparator(value);
        return this;
    }

    @NotNull
    @Override
    public final JsonConfigurationOptions copyDefaults(final boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @NotNull
    @Override
    public final JsonConfigurationOptions header(final String value) {
        super.header(value);
        return this;
    }

    @NotNull
    @Override
    public final JsonConfigurationOptions copyHeader(final boolean value) {
        super.copyHeader(value);
        return this;
    }

}
