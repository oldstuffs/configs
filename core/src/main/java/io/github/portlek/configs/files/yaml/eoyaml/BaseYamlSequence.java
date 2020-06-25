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
package io.github.portlek.configs.files.yaml.eoyaml;

import java.util.Collection;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;


public abstract class BaseYamlSequence
    extends BaseYamlNode implements YamlSequence {

    @Override
    public final Node type() {
        return Node.SEQUENCE;
    }

    @NotNull
    @Override
    public final String emptyValue() {
        return "[]";
    }

    @Override
    public final int hashCode() {
        int hash = 0;
        for (final YamlNode node : this.values()) {
            hash += node.hashCode();
        }
        return hash;
    }


    @Override
    public final boolean equals(final Object other) {
        final boolean result;
        if (other == null || !(other instanceof YamlSequence)) {
            result = false;
        } else if (this == other) {
            result = true;
        } else {
            result = this.compareTo((YamlSequence) other) == 0;
        }
        return result;
    }


    @Override
    public final int compareTo(final YamlNode other) {
        int result = 0;
        if (other == null || other instanceof Scalar) {
            result = 1;
        } else if (other instanceof YamlMapping) {
            result = -1;
        } else if (this != other) {
            final Collection<YamlNode> nodes = this.values();
            final Collection<YamlNode> others = ((YamlSequence) other).values();
            if (nodes.size() > others.size()) {
                result = 1;
            } else if (nodes.size() < others.size()) {
                result = -1;
            } else {
                final Iterator<YamlNode> iterator = others.iterator();
                final Iterator<YamlNode> here = nodes.iterator();
                while (iterator.hasNext()) {
                    result = here.next().compareTo(iterator.next());
                    if (result != 0) {
                        break;
                    }
                }
            }
        }
        return result;
    }

    @Override
    final boolean isEmpty() {
        return this.values().isEmpty();
    }

}
