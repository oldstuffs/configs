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

import io.github.portlek.configs.FileType;
import io.github.portlek.configs.Managed;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.util.Basedir;
import io.github.portlek.configs.util.Version;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.io.File;

public final class ConfigProceed implements Proceed<Managed> {

    @NotNull
    private final Config config;

    public ConfigProceed(@NotNull Config config) {
        this.config = config;
    }

    @Override
    public void load(@NotNull Managed managed) throws Exception {
        final FileType fileType = config.type();
        final String fileName;

        if (config.name().endsWith(fileType.suffix)) {
            fileName = config.name();
        } else {
            fileName = config.name() + fileType.suffix;
        }

        final Version version = Version.of(config.version());
        final String versionPath = config.versionPath();
        final String fileLocation = addSeparatorIfHasNot(
            config.location()
                .replace("%basedir%", new Basedir().value().getAbsolutePath())
                .replace("/", File.separator)
        );
        final File file = new File(fileLocation, fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();

            try {
                file.createNewFile();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        final FileConfiguration fileConfiguration = fileType.load(file);

        managed.setup(file, fileConfiguration);
        version.write(versionPath, managed);
        new FieldsProceed(managed, "").load(managed);
        managed.save();
    }

    @NotNull
    private String addSeparatorIfHasNot(@NotNull String raw) {
        if (raw.endsWith(File.separator)) {
            return raw;
        }

        return raw + File.separator;
    }

}
