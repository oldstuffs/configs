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
import java.util.Set;
import java.util.function.Supplier;

public final class MergedYamlMapping extends BaseYamlMapping {

    private final YamlMapping merged;

    public MergedYamlMapping(
        final YamlMapping original,
        final YamlMapping changed
    ) {
        this(original, changed, false);
    }

    public MergedYamlMapping(
        final YamlMapping original,
        final Supplier<YamlMapping> changed
    ) {
        this(original, changed, false);
    }

    public MergedYamlMapping(
        final YamlMapping original,
        final Supplier<YamlMapping> changed,
        final boolean overrideConflicts
    ) {
        this(original, changed.get(), overrideConflicts);
    }

    public MergedYamlMapping(
        final YamlMapping original,
        final YamlMapping changed,
        final boolean overrideConflicts
    ) {
        if (original == null && changed == null) {
            throw new IllegalArgumentException(
                "Both mappings cannot be null!"
            );
        } else {
            this.merged = MergedYamlMapping.merge(original, changed, overrideConflicts);
        }
    }

    private static YamlMapping merge(
        final YamlMapping original,
        final YamlMapping changed,
        final boolean overrideConflicts
    ) {
        final YamlMapping merged;
        if (original == null || original.keys().isEmpty()) {
            merged = changed;
        } else if (changed == null || changed.keys().isEmpty()) {
            merged = original;
        } else {
            YamlMappingBuilder builder = Yaml.createYamlMappingBuilder();
            final Set<YamlNode> changedKeys = changed.keys();
            for (final YamlNode key : original.keys()) {
                if (changedKeys.contains(key)) {
                    if (overrideConflicts) {
                        builder = builder.add(key, changed.value(key));
                    } else {
                        builder = builder.add(key, original.value(key));
                    }
                    changedKeys.remove(key);
                } else {
                    builder = builder.add(key, original.value(key));
                }
            }
            for (final YamlNode key : changedKeys) {
                builder = builder.add(key, changed.value(key));
            }
            merged = builder.build(original.comment().value());
        }
        return merged;
    }

    @Override
    public Set<YamlNode> keys() {
        return this.merged.keys();
    }

    @Override
    public YamlNode value(final YamlNode key) {
        return this.merged.value(key);
    }

    @Override
    public Comment comment() {
        return this.merged.comment();
    }

}
