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

package io.github.portlek.configs.structure;

import io.github.portlek.configs.annotations.LinkedConfig;
import io.github.portlek.configs.processors.LinkedConfigProceed;
import io.github.portlek.configs.structure.managed.FlManaged;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public interface LnkdFlManaged extends FlManaged {

    @NotNull <T> T match(@NotNull Function<String, Optional<T>> function);

    @NotNull
    Supplier<String> getChosen();

    @Override
    default void load() {
        final LinkedConfig linked = this.getClass().getDeclaredAnnotation(LinkedConfig.class);
        if (linked != null) {
            new LinkedConfigProceed(this, linked).load();
            return;
        }
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " has not `LinkedConfig` annotation!");
    }

}
