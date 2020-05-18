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

package io.github.portlek.configs.util;

import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.FlManaged;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class Feature<X, Y> {

    private final Map<X, Y> cache = new ConcurrentHashMap<>();

    @NotNull
    private final FlManaged managed;

    @NotNull
    private final CfgSection parent;

    @NotNull
    private final Class<X> keyClass;

    @NotNull
    private final Class<Y> valueClass;

    public void refresh() {
        this.cache.clear();
        this.parent.getKeys(false).stream()
            .map(this.parent::getSection)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(section -> {
                final String key = section.getName();
                this.managed.getCustomValue(this.valueClass)
                    .flatMap(yProvided -> yProvided.get(section, ""))
                    .ifPresent(y ->
                        this.managed.convert(this.keyClass, key).ifPresent(converted ->
                            this.cache.put(converted, y)));
            });
    }

}
