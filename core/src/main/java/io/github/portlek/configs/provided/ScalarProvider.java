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

package io.github.portlek.configs.provided;

import io.github.portlek.configs.CfgSection;
import io.github.portlek.configs.LnkdManaged;
import io.github.portlek.configs.Provided;
import io.github.portlek.configs.processors.PropertyProceed;
import io.github.portlek.configs.util.Scalar;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class ScalarProvider implements Provided<Scalar<Object>> {

    @NotNull
    @Override
    public Optional<Scalar<Object>> getWithField(@NotNull final Scalar<Object> scalar,
                                                 @NotNull final CfgSection section, @NotNull final String path) {
        if (!(section.getParent() instanceof LnkdManaged)) {
            return Optional.empty();
        }
        return PropertyProceed.get(section, scalar.get(), path)
            .map(scalar::change);
    }

    @NotNull
    @Override
    public Optional<Scalar<Object>> get(@NotNull final CfgSection section, @NotNull final String path) {
        return Optional.empty();
    }

    @Override
    public void set(@NotNull final Scalar<Object> scalar, @NotNull final CfgSection section,
                    @NotNull final String path) {
        PropertyProceed.set(section, scalar.get(), path);
    }

}