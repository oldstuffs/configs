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

final class ReadPlainScalar extends BaseScalar {

    private final AllYamlLines all;

    private final YamlLine scalar;

    ReadPlainScalar(final AllYamlLines all, final YamlLine scalar) {
        this.all = all;
        this.scalar = scalar;
    }

    @Override
    public String value() {
        final String value;
        final String trimmed = this.scalar.trimmed();
        if (trimmed.contains(":") && trimmed.charAt(trimmed.length() - 1) != ':') {
            value = trimmed.substring(trimmed.indexOf(':') + 1).trim();
        } else if (!trimmed.isEmpty() && trimmed.charAt(0) == '-' && trimmed.length() > 1) {
            value = trimmed.substring(trimmed.indexOf('-') + 1).trim();
        } else {
            value = trimmed;
        }
        if (value.isEmpty()) {
            return this.emptyValue();
        } else {
            return this.unescape(value);
        }
    }

    @Override
    public Comment comment() {
        final Comment comment;
        if (this.scalar instanceof YamlLine.NullYamlLine) {
            comment = new BuiltComment(this, "");
        } else {
            comment = new ReadComment(
                new FirstCommentFound(
                    new Skip(
                        this.all,
                        line -> line.number() != this.scalar.number()
                    ),
                    Boolean.TRUE
                ),
                this
            );
        }
        return comment;
    }

    private String unescape(final String value) {
        final String unescaped;
        if (value == null || value.length() <= 2) {
            unescaped = value;
        } else {
            if (value.startsWith("\"") && value.endsWith("\"")) {
                unescaped = value.substring(1, value.length() - 1);
            } else if (value.startsWith("'") && value.endsWith("'")) {
                unescaped = value.substring(1, value.length() - 1);
            } else {
                unescaped = value;
            }
        }
        return unescaped;
    }

}
