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
import io.github.portlek.configs.util.Version;
import io.github.portlek.configs.yaml.configuration.file.FileConfiguration;
import java.io.*;
import java.net.URLConnection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    private static InputStream getResource(@NotNull final String path) {
        return Optional.ofNullable(ConfigProceed.class.getClassLoader().getResource(path)).map(url -> {
            try {
                final URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
            return null;
        }).orElse(null);
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
            file = this.saveResource(
                jarlocation,
                ConfigProceed.addSeparatorIfHasNot(this.config.resourcePath()) + name
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

    @NotNull
    public File saveResource(@NotNull final String datafolder, @NotNull final String path) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be empty");
        }
        final String replace = path.replace('\\', '/');
        final int lastindex = replace.lastIndexOf(47);
        final File outdir = new File(datafolder, replace.substring(0, Math.max(lastindex, 0)));
        if (!outdir.exists()) {
            outdir.getParentFile().mkdirs();
            outdir.mkdirs();
        }
        final File outfile = new File(datafolder, replace);
        if (!outfile.exists()) {
            try (final OutputStream out = new FileOutputStream(outfile);
                 final InputStream input = ConfigProceed.getResource(replace)) {
                if (input == null) {
                    throw new IllegalArgumentException("The embedded resource '" + replace + "' cannot be found!");
                }
                final byte[] buf = new byte[1024];
                int len;
                while ((len = input.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } catch (final IOException exception) {
                exception.printStackTrace();
            }
        }
        return outfile;
    }

}
