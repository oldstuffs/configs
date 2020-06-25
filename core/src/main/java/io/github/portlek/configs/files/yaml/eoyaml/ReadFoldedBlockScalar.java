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

import java.util.ArrayList;
import java.util.List;

final class ReadFoldedBlockScalar extends BaseFoldedScalar {

    private final YamlLine previous;

    private final AllYamlLines all;

    private final YamlLines significant;

    ReadFoldedBlockScalar(final AllYamlLines lines) {
        this(new YamlLine.NullYamlLine(), lines);
    }

    ReadFoldedBlockScalar(final YamlLine previous, final AllYamlLines lines) {
        this.previous = previous;
        this.all = lines;
        this.significant = new GreaterIndentation(
            previous,
            new Skip(
                lines,
                line -> line.number() <= previous.number(),
                line -> line.trimmed().endsWith(">"),
                line -> line.trimmed().startsWith("---"),
                line -> line.trimmed().startsWith("..."),
                line -> line.trimmed().startsWith("%"),
                line -> line.trimmed().startsWith("!!")
            )
        );
    }

    @Override
    public String value() {
        final StringBuilder builder = new StringBuilder();
        final String newLine = System.lineSeparator();
        for (final YamlLine line : this.significant) {
            if (line.trimmed().length() == 0 || line.indentation() > 0) {
                if (this.doNotEndWithNewLine(builder)) {
                    builder.append(newLine);
                }
                final int indentation = line.indentation();
                for (int i = 0; i < indentation; i++) {
                    builder.append(' ');
                }
                builder.append(line.trimmed());
                builder.append(newLine);
            } else {
                if (this.doNotEndWithNewLine(builder)) {
                    builder.append(' ');
                }
                builder.append(line.trimmed());
            }
        }
        return builder.toString();
    }

    @Override
    public Comment comment() {
        //LineLength (50 lines)
        return new ReadComment(
            new Backwards(
                new FirstCommentFound(
                    new Backwards(
                        new Skip(
                            this.all,
                            line -> {
                                final boolean skip;
                                if (this.previous.number() < 0) {
                                    if (this.significant.iterator().hasNext()) {
                                        skip = line.number() >= this.significant
                                            .iterator().next().number();
                                    } else {
                                        skip = false;
                                    }
                                } else {
                                    skip = line.number() >= this.previous.number();
                                }
                                return skip;
                            },
                            line -> line.trimmed().startsWith("---"),
                            line -> line.trimmed().startsWith("..."),
                            line -> line.trimmed().startsWith("%"),
                            line -> line.trimmed().startsWith("!!")
                        )
                    )
                )
            ),
            this
        );
    }

    @Override
    List<String> unfolded() {
        final List<String> unfolded = new ArrayList<>();
        for (final YamlLine line : this.significant) {
            unfolded.add(line.toString());
        }
        return unfolded;
    }

    private boolean doNotEndWithNewLine(final StringBuilder builder) {
        return builder.length() > 0
            && !builder.toString().endsWith(System.lineSeparator());
    }

}
