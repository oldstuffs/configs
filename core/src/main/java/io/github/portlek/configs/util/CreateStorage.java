/*
 * MIT License
 *
 * Copyright (c) 2019 Hasan Demirtaş
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

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public final class CreateStorage {

    @NotNull
    private final String path;

    @NotNull
    private final String fileName;

    public CreateStorage(@NotNull String path, @NotNull String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    @NotNull
    public File value() {
        final File storage = new File(path, fileName);

        if (!storage.exists()) {
            storage.getParentFile().mkdirs();

            try {
                storage.createNewFile();
            } catch (IOException exception) {
                throw new IllegalStateException(exception);
            }
        }

        if (!storage.exists()) {
            throw new IllegalStateException(storage.getName() + " cannot be created, please check file permissions!");
        }

        return storage;
    }

}