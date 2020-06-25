/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package io.github.portlek.configs.files.yaml.eoyaml.extensions;

import io.github.portlek.configs.files.yaml.eoyaml.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

public final class MergedYamlSequence extends BaseYamlSequence {

    private final YamlSequence merged;

    public MergedYamlSequence(
        final YamlSequence original,
        final YamlSequence changed
    ) {
        this(original, changed, false);
    }

    public MergedYamlSequence(
        final YamlSequence original,
        final Supplier<YamlSequence> changed
    ) {
        this(original, changed, false);
    }

    public MergedYamlSequence(
        final YamlSequence original,
        final Supplier<YamlSequence> changed,
        final boolean overrideIndices
    ) {
        this(original, changed.get(), overrideIndices);
    }

    public MergedYamlSequence(
        final YamlSequence original,
        final YamlSequence changed,
        final boolean overrideIndices
    ) {
        if (original == null && changed == null) {
            throw new IllegalArgumentException(
                "Both sequences cannot be null!"
            );
        } else {
            this.merged = MergedYamlSequence.merge(original, changed, overrideIndices);
        }
    }

    private static YamlSequence merge(
        final YamlSequence original,
        final YamlSequence changed,
        final boolean overrideIndices
    ) {
        final YamlSequence merged;
        if (original == null || original.size() == 0) {
            merged = changed;
        } else if (changed == null || changed.size() == 0) {
            merged = original;
        } else {
            if (overrideIndices) {
                if (changed.size() >= original.size()) {
                    merged = changed;
                } else {
                    YamlSequenceBuilder builder = Yaml
                        .createYamlSequenceBuilder();
                    int continueFrom = -1;
                    for (final YamlNode node : changed) {
                        builder = builder.add(node);
                        continueFrom++;
                    }
                    final Iterator<YamlNode> originalIt = original.iterator();
                    int originalIdx = -1;
                    while (originalIt.hasNext()) {
                        if (originalIdx >= continueFrom) {
                            builder = builder.add(originalIt.next());
                        } else {
                            originalIt.next();
                        }
                        originalIdx++;
                    }
                    merged = builder.build();
                }
            } else {
                YamlSequenceBuilder builder = Yaml
                    .createYamlSequenceBuilder();
                for (final YamlNode node : original) {
                    builder = builder.add(node);
                }
                for (final YamlNode node : changed) {
                    builder = builder.add(node);
                }
                merged = builder.build();
            }
        }
        return merged;
    }

    @Override
    public Collection<YamlNode> values() {
        return this.merged.values();
    }

    @Override
    public Comment comment() {
        return this.merged.comment();
    }

}
