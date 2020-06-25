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

import org.jetbrains.annotations.NotNull;

abstract class BaseScalar extends BaseYamlNode implements Scalar {

    @Override
    public final Node type() {
        return Node.SCALAR;
    }

    @NotNull
    @Override
    public final String emptyValue() {
        return "\"\"";
    }

    @Override
    public int hashCode() {
        return this.value().hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        final boolean result;
        if (other == null || !(other instanceof Scalar)) {
            result = false;
        } else if (this == other) {
            result = true;
        } else {
            result = this.compareTo((Scalar) other) == 0;
        }
        return result;
    }

    @Override
    public int compareTo(final YamlNode other) {
        int result = -1;
        if (this == other) {
            result = 0;
        } else if (other == null) {
            result = 1;
        } else if (other instanceof Scalar) {
            final String value = this.value();
            final String otherVal = ((Scalar) other).value();
            if (value == null && otherVal == null) {
                result = 0;
            } else if (value != null && otherVal == null) {
                result = 1;
            } else if (value == null && otherVal != null) {
                result = -1;
            } else {
                result = this.value().compareTo(otherVal);
            }
        }
        return result;
    }

    @Override
    final boolean isEmpty() {
        return this.value() == null || this.value().trim().isEmpty();
    }

}
