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

package io.github.portlek.configs.processors;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import io.github.portlek.configs.FileType;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.util.Basedir;
import io.github.portlek.configs.util.Copied;
import io.github.portlek.configs.util.CreateStorage;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class ConfigProceed implements Proceed {

    @NotNull
    private final Config config;

    public ConfigProceed(@NotNull Config config) {
        this.config = config;
    }

    @Override
    public void load(@NotNull Object instance) {
        final FileType fileType = config.fileType();
        final String fileName;

        if (config.fileName().endsWith(fileType.getSuffix())) {
            fileName = config.fileName();
        } else {
            fileName = config.fileName() + fileType.getSuffix();
        }

        final String fileVersion = config.fileVersion();
        final String fileLocation = addSeparatorIfHasNot(
            config.fileLocation()
                .replace("%basedir%", new Basedir().value().getAbsolutePath())
                .replace("/", File.separator)
        );
        final String[] comment = config.comment();
        final File file = new File(fileLocation, fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();

            try {
                file.createNewFile();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        // TODO: 22/01/2020
    }

    @NotNull
    private String addSeparatorIfHasNot(@NotNull String rawFileLocation) {
        final String fileLocation;

        if (rawFileLocation.endsWith(File.separator)) {
            fileLocation = rawFileLocation;
        } else {
            fileLocation = rawFileLocation + File.separator;
        }

        return fileLocation;
    }

}
