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

package io.github.portlek.configs;

import java.io.File;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.json.JSONConfigurationLoader;
import ninja.leaping.configurate.loader.AbstractConfigurationLoader;
import ninja.leaping.configurate.xml.XMLConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public enum FileType {

    YAML(".yml", file -> YAMLConfigurationLoader.builder().setFile(file).build()),
    JSON(".json", file -> JSONConfigurationLoader.builder().setFile(file).build()),
    XML(".xml", file -> XMLConfigurationLoader.builder().setFile(file).build()),
    HOCON(".hocon", file -> HoconConfigurationLoader.builder().setFile(file).build());

    @NotNull
    @Getter
    public final String suffix;

    @NotNull
    private final Function<File, AbstractConfigurationLoader<?>> loadfunction;

    @NotNull
    public AbstractConfigurationLoader<?> load(@NotNull final File file) {
        return this.loadfunction.apply(file);
    }

}
