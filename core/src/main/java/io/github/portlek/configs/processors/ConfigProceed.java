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

package io.github.portlek.configs.processors;

import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.files.FileType;
import io.github.portlek.configs.files.configuration.FileConfiguration;
import io.github.portlek.configs.structure.managed.FlManaged;
import io.github.portlek.configs.util.GeneralUtilities;
import io.github.portlek.configs.util.Version;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class ConfigProceed implements Proceed {

    @NotNull
    private final FlManaged managed;

    @NotNull
    private final Config config;

    @Override
    public void load() {
        final FileType type = this.config.type();
        final String name;
        if (this.config.name().endsWith(type.suffix)) {
            name = this.config.name();
        } else {
            name = this.config.name() + type.suffix;
        }
        final Version version = Version.from(this.config.version());
        final String versionpath = this.config.versionPath();
        GeneralUtilities.basedir(this.managed.getClass()).ifPresent(basedir -> {
            final String filelocation = GeneralUtilities.addSeparator(
                this.config.location()
                    .replace("%basedir%", basedir.getParentFile().getAbsolutePath())
                    .replace("/", File.separator)
            );
            final String jarlocation = GeneralUtilities.addSeparator(
                this.config.location()
                    .replace("%basedir%", basedir.getAbsolutePath())
                    .replace("/", File.separator)
            );
            final File file;
            if (this.config.copyDefault()) {
                file = GeneralUtilities.saveResource(
                    jarlocation,
                    GeneralUtilities.addSeparator(this.config.resourcePath()) + name
                );
            } else {
                file = new File(filelocation, name);
            }
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            final FileConfiguration configuration = type.load(file);
            this.managed.setup(file, configuration);
            final Optional<String> versionoptional = this.managed.getString(versionpath);
            if (versionoptional.isPresent()) {
                final Version fileversion = Version.from(versionoptional.get());
                if (!version.is(fileversion)) {
                    // TODO: 29/01/2020
                }
            } else {
                version.write(versionpath, this.managed);
            }
            new FieldsProceed(this.managed, this.managed).load();
            this.managed.save();
        });
    }

}
