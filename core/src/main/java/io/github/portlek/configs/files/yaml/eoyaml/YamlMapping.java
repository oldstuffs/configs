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
import java.util.*;

public interface YamlMapping extends YamlNode {

    Set<YamlNode> keys();

    YamlNode value(YamlNode key);

    default Collection<YamlNode> values() {
        final List<YamlNode> values = new LinkedList<>();
        for (final YamlNode key : this.keys()) {
            values.add(this.value(key));
        }
        return values;
    }

    default YamlMapping yamlMapping(final String key) {
        return this.yamlMapping(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default YamlMapping yamlMapping(final YamlNode key) {
        final YamlNode value = this.value(key);
        final YamlMapping found;
        if (value != null && value instanceof YamlMapping) {
            found = (YamlMapping) value;
        } else {
            found = null;
        }
        return found;
    }

    default YamlSequence yamlSequence(final String key) {
        return this.yamlSequence(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default YamlSequence yamlSequence(final YamlNode key) {
        final YamlNode value = this.value(key);
        final YamlSequence found;
        if (value != null && value instanceof YamlSequence) {
            found = (YamlSequence) value;
        } else {
            found = null;
        }
        return found;
    }

    default String string(final String key) {
        return this.string(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default String string(final YamlNode key) {
        final YamlNode value = this.value(key);
        final String found;
        if (value != null && value instanceof Scalar) {
            found = ((Scalar) value).value();
        } else {
            found = null;
        }
        return found;
    }

    default String foldedBlockScalar(final String key) {
        return this.foldedBlockScalar(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default String foldedBlockScalar(final YamlNode key) {
        final YamlNode value = this.value(key);
        final String found;
        if (value != null && value instanceof Scalar) {
            found = ((Scalar) value).value();
        } else {
            found = null;
        }
        return found;
    }

    default Collection<String> literalBlockScalar(final String key) {
        return this.literalBlockScalar(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default Collection<String> literalBlockScalar(final YamlNode key) {
        final Collection<String> found;
        final YamlNode value = this.value(key);
        if (value instanceof Scalar) {
            found = Arrays.asList(
                ((Scalar) value)
                    .value()
                    .split(System.lineSeparator())
            );
        } else {
            found = null;
        }
        return found;
    }

    default YamlNode value(final String key) {
        return this.value(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default int integer(final String key) {
        return this.integer(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default int integer(final YamlNode key) {
        final YamlNode value = this.value(key);
        if (value != null && value instanceof Scalar) {
            return Integer.parseInt(((Scalar) value).value());
        }
        return -1;
    }

    default float floatNumber(final String key) {
        return this.floatNumber(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default float floatNumber(final YamlNode key) {
        final YamlNode value = this.value(key);
        if (value != null && value instanceof Scalar) {
            return Float.parseFloat(((Scalar) value).value());
        }
        return -1;
    }

    default double doubleNumber(final String key) {
        return this.doubleNumber(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default double doubleNumber(final YamlNode key) {
        final YamlNode value = this.value(key);
        if (value != null && value instanceof Scalar) {
            return Double.parseDouble(((Scalar) value).value());
        }
        return -1.0;
    }

    default long longNumber(final String key) {
        return this.longNumber(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default long longNumber(final YamlNode key) {
        final YamlNode value = this.value(key);
        if (value != null && value instanceof Scalar) {
            return Long.parseLong(((Scalar) value).value());
        }
        return -1L;
    }

    default LocalDate date(final String key) {
        return this.date(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default LocalDate date(final YamlNode key) {
        final YamlNode value = this.value(key);
        if (value != null && value instanceof Scalar) {
            return LocalDate.parse(((Scalar) value).value());
        }
        return null;
    }

    default LocalDateTime dateTime(final String key) {
        return this.dateTime(
            Yaml.createYamlScalarBuilder().addLine(key).buildPlainScalar()
        );
    }

    default LocalDateTime dateTime(final YamlNode key) {
        final YamlNode value = this.value(key);
        if (value != null && value instanceof Scalar) {
            return LocalDateTime.parse(((Scalar) value).value());
        }
        return null;
    }

}
