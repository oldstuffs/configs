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
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public final class ConfigProceed implements Proceed<Managed> {

    @NotNull
    private final Config config;

    @NotNull
    private final BiFunction<Object, String, Optional<?>> get;

    @NotNull
    private final BiPredicate<Object, String> set;

    public ConfigProceed(@NotNull Config config, @NotNull BiFunction<Object, String, Optional<?>> get,
                         @NotNull BiPredicate<Object, String> set) {
        this.config = config;
        this.get = get;
        this.set = set;
    }

    public ConfigProceed(@NotNull Config config) {
        this(config, (o, s) -> Optional.empty(), (o, s) -> false);
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
        final Optional<File> baseDirOptional = new Basedir().value();

        if (!baseDirOptional.isPresent()) {
            return;
        }

        final File baseDir = baseDirOptional.get();
        final String fileLocation = addSeparatorIfHasNot(
            config.location()
                .replace("%basedir%", baseDir.getAbsolutePath())
                .replace("/", File.separator)
        );
        final File file = new File(fileLocation, fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
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

        new FieldsProceed(managed, "", get, set).load(managed);
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
