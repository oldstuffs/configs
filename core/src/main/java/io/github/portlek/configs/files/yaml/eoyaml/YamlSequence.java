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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public interface YamlSequence extends YamlNode, Iterable<YamlNode> {

    Collection<YamlNode> values();

    @Override
    default Iterator<YamlNode> iterator() {
        return this.values().iterator();
    }

    default int size() {
        return this.values().size();
    }

    default YamlMapping yamlMapping(final int index) {
        YamlMapping mapping = null;
        int count = 0;
        for (final YamlNode node : this.values()) {
            if (count == index && node instanceof YamlMapping) {
                mapping = (YamlMapping) node;
            }
            count = count + 1;
        }
        return mapping;
    }

    default YamlSequence yamlSequence(final int index) {
        YamlSequence sequence = null;
        int count = 0;
        for (final YamlNode node : this.values()) {
            if (count == index && node instanceof YamlSequence) {
                sequence = (YamlSequence) node;
            }
            count = count + 1;
        }
        return sequence;
    }

    default String string(final int index) {
        String value = null;
        int count = 0;
        for (final YamlNode node : this.values()) {
            if (count == index && node instanceof Scalar) {
                value = ((Scalar) node).value();
                break;
            }
            count++;
        }
        return value;
    }

    default String foldedBlockScalar(final int index) {
        String value = null;
        int count = 0;
        for (final YamlNode node : this.values()) {
            if (count == index && node instanceof Scalar) {
                value = ((Scalar) node).value();
                break;
            }
            count++;
        }
        return value;
    }

    default Collection<String> literalBlockScalar(final int index) {
        Collection<String> value = null;
        int count = 0;
        for (final YamlNode node : this.values()) {
            if (count == index && node instanceof Scalar) {
                value = Arrays.asList(
                    ((Scalar) node)
                        .value().split(System.lineSeparator())
                );
                break;
            }
            count++;
        }
        return value;
    }

    default int integer(final int index) {
        final String value = this.string(index);
        if (value != null && !value.isEmpty()) {
            return Integer.parseInt(value);
        }
        return -1;
    }

    default float floatNumber(final int index) {
        final String value = this.string(index);
        if (value != null && !value.isEmpty()) {
            return Float.parseFloat(value);
        }
        return -1;
    }

    default double doubleNumber(final int index) {
        final String value = this.string(index);
        if (value != null && !value.isEmpty()) {
            return Double.parseDouble(value);
        }
        return -1.0;
    }

    default long longNumber(final int index) {
        final String value = this.string(index);
        if (value != null && !value.isEmpty()) {
            return Long.parseLong(value);
        }
        return -1L;
    }

    default LocalDate date(final int index) {
        final String value = this.string(index);
        if (value != null && !value.isEmpty()) {
            return LocalDate.parse(value);
        }
        return null;
    }

    default LocalDateTime dateTime(final int index) {
        final String value = this.string(index);
        if (value != null && !value.isEmpty()) {
            return LocalDateTime.parse(value);
        }
        return null;
    }

}
