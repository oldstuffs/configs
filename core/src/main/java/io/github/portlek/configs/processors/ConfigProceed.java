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

import io.github.portlek.configs.Managed;
import io.github.portlek.configs.Proceed;
import io.github.portlek.configs.annotations.Config;
import io.github.portlek.configs.util.Basedir;
import io.github.portlek.configs.util.FileType;
import io.github.portlek.configs.util.Version;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.file.FileConfiguration;

public final class ConfigProceed implements Proceed<Managed> {

    @NotNull
    private final Config config;

    public ConfigProceed(@NotNull final Config config) {
        this.config = config;
    }

    @Override
    public void load(@NotNull final Managed managed) {
        final FileType fileType = this.config.type();
        final String fileName;
        if (this.config.name().endsWith(fileType.suffix)) {
            fileName = this.config.name();
        } else {
            fileName = this.config.name() + fileType.suffix;
        }
        final Version version = Version.of(this.config.version());
        final String versionPath = this.config.versionPath();
        final Optional<File> baseDirOptional = new Basedir(managed.getClass()).value();
        if (!baseDirOptional.isPresent()) {
            return;
        }
        final File baseDir = baseDirOptional.get();
        final String fileLocation = this.addSeparatorIfHasNot(
            this.config.location()
                .replace("%basedir%", baseDir.getParentFile().getAbsolutePath())
                .replace("/", File.separator)
        );
        final String jarLocation = this.addSeparatorIfHasNot(
            this.config.location()
                .replace("%basedir%", baseDir.getAbsolutePath())
                .replace("/", File.separator)
        );
        final File file;
        if (this.config.copyDefault()) {
            file = this.saveResource(
                jarLocation,
                this.addSeparatorIfHasNot(this.config.resourcePath()) + fileName
            );
        } else {
            file = new File(fileLocation, fileName);
        }
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        final FileConfiguration fileConfiguration = fileType.load(file);
        managed.setup(file, fileConfiguration);
        final Optional<String> fileVersionOptional = managed.getString(versionPath);
        if (!fileVersionOptional.isPresent()) {
            version.write(versionPath, managed);
        } else {
            final Version fileVersion = Version.of(fileVersionOptional.get());
            if (!version.is(fileVersion)) {
                // TODO: 29/01/2020
            }
        }
        new FieldsProceed(managed, "").load(managed);
        managed.save();
    }

    @NotNull
    private String addSeparatorIfHasNot(@NotNull final String raw) {
        if (raw.isEmpty()) {
            return "";
        }
        if (raw.endsWith(File.separator)) {
            return raw;
        }

        return raw + File.separator;
    }

    @NotNull
    public File saveResource(@NotNull final String dataFolder, @NotNull String resourcePath) {
        if (!resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            final InputStream in = this.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found!");
            } else {
                final File outFile = new File(dataFolder, resourcePath);
                final int lastIndex = resourcePath.lastIndexOf(47);
                final File outDir = new File(dataFolder, resourcePath.substring(0, Math.max(lastIndex, 0)));
                if (!outDir.exists()) {
                    outDir.getParentFile().mkdirs();
                    outDir.mkdirs();
                }
                try {
                    if (!outFile.exists()) {
                        final OutputStream out = new FileOutputStream(outFile);
                        final byte[] buf = new byte[1024];

                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (final IOException var10) {
                    var10.printStackTrace();
                }
                return outFile;
            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be empty");
        }
    }

    @Nullable
    public InputStream getResource(@NotNull final String path) {
        try {
            final URL url = this.getClass().getClassLoader().getResource(path);
            if (url == null) {
                return null;
            } else {
                final URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        } catch (final IOException var4) {
            return null;
        }
    }

}
