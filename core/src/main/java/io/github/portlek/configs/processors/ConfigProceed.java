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

import io.github.portlek.configs.FlManaged;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.util.Basedir;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.configs.util.SaveResource;
import io.github.portlek.configs.util.Version;
import io.github.portlek.configs.yaml.FileConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class ConfigProceed implements Proceed<FlManaged> {

    @NotNull
    private final Config config;

    public ConfigProceed(@NotNull final Config cnfg) {
        this.config = cnfg;
    }

    @NotNull
    private static String addSeparatorIfHasNot(@NotNull final String raw) {
        final String fnl;
        if (raw.isEmpty()) {
            fnl = "";
        } else if (raw.charAt(raw.length() - 1) == File.separatorChar) {
            fnl = raw;
        } else {
            fnl = raw + File.separatorChar;
        }
        return fnl;
    }

    @Override
    public void load(@NotNull final FlManaged managed) {
        final FileType type = this.config.type();
        final String name;
        if (this.config.name().endsWith(type.suffix)) {
            name = this.config.name();
        } else {
            name = this.config.name() + type.suffix;
        }
        final Version version = Version.of(this.config.version());
        final String versionpath = this.config.versionPath();
        final Optional<File> optional = new Basedir(managed.getClass()).value();
        if (!optional.isPresent()) {
            return;
        }
        final File basedir = optional.get();
        final String filelocation = ConfigProceed.addSeparatorIfHasNot(
            this.config.location()
                .replace("%basedir%", basedir.getParentFile().getAbsolutePath())
                .replace("/", File.separator)
        );
        final String jarlocation = ConfigProceed.addSeparatorIfHasNot(
            this.config.location()
                .replace("%basedir%", basedir.getAbsolutePath())
                .replace("/", File.separator)
        );
        final File file;
        if (this.config.copyDefault()) {
            file = new SaveResource(
                jarlocation,
                ConfigProceed.addSeparatorIfHasNot(this.config.resourcePath()) + name
            ).value();
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
        managed.setup(file, configuration);
        final Optional<String> versionoptional = managed.getString(versionpath);
        if (versionoptional.isPresent()) {
            final Version fileversion = Version.of(versionoptional.get());
            if (!version.is(fileversion)) {
                // TODO: 29/01/2020
            }
        } else {
            version.write(versionpath, managed);
        }
        new FieldsProceed(managed).load(managed);
        managed.save();
    }

}
