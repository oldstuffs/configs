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

package io.github.portlek.configs.linked;

import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.FileType;
import io.github.portlek.configs.LnkdManaged;
import io.github.portlek.configs.managed.FileManaged;
import io.github.portlek.configs.provided.ScalarProvider;
import io.github.portlek.configs.util.Scalar;
import io.github.portlek.mapentry.MapEntry;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.exceptions.InvalidConfigurationException;

public class LinkedManaged extends FileManaged implements LnkdManaged {

    static {
        CfgSection.PROVIDED.put(Scalar.class, new ScalarProvider());
    }

    @NotNull
    private final Map<String, Map.Entry<File, FileConfiguration>> linkedFiles = new HashMap<>();

    @NotNull
    private final Supplier<String> chosen;

    @SafeVarargs
    public LinkedManaged(@NotNull final Supplier<String> chosen,
                         @NotNull final Map.Entry<String, Object>... objects) {
        super(objects);
        this.chosen = chosen;
    }

    @NotNull
    @Override
    public final Supplier<String> getChosen() {
        return this.chosen;
    }

    @Override
    public final void setup(@NotNull final File file, final @NotNull FileType fileType) throws Exception {
        super.setup(file, fileType);
        this.linkedFiles.put(
            this.chosen.get(),
            MapEntry.from(file, fileType.load(file)));
    }

}
