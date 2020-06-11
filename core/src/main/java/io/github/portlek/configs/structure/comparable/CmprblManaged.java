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

package io.github.portlek.configs.structure.comparable;

import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.processors.ComparableConfigProceed;
import io.github.portlek.configs.structure.managed.FlManaged;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public interface CmprblManaged extends FlManaged {

    @Override
    default void load() {
        this.onCreate();
        new ComparableConfigProceed(
            Optional.ofNullable(this.getClass().getDeclaredAnnotation(LinkedConfig.class)).orElseThrow(() ->
                new UnsupportedOperationException(this.getClass().getSimpleName() + " has not `LinkedConfig` annotation!")),
            this
        ).load();
        this.onLoad();
    }

    @Override
    @NotNull
    CmprblManaged base();

    default void setup(@NotNull final String key, @NotNull final FlManaged managed) {
        this.base().setup(key, managed);
    }

}
