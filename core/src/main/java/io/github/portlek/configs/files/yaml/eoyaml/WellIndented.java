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

import io.github.portlek.configs.files.yaml.eoyaml.exceptions.YamlIndentationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


final class WellIndented implements YamlLines {


    private final YamlLines yamlLines;


    private final boolean guessIndentation;


    WellIndented(final YamlLines yamlLines) {
        this(yamlLines, false);
    }


    WellIndented(final YamlLines yamlLines, final boolean guessIndentation) {
        this.yamlLines = yamlLines;
        this.guessIndentation = guessIndentation;
    }

    @Override
    public Collection<YamlLine> original() {
        return this.yamlLines.original();
    }

    @Override
    public YamlNode toYamlNode(
        final YamlLine prev,
        final boolean guessIndent
    ) {
        return this.yamlLines.toYamlNode(prev, guessIndent);
    }


    @Override
    public Iterator<YamlLine> iterator() {
        final Iterator<YamlLine> iterator = this.yamlLines.iterator();
        final List<YamlLine> wellIndented = new ArrayList<>();
        YamlLine previous;
        if (iterator.hasNext()) {
            previous = iterator.next();
            wellIndented.add(previous);
            while (iterator.hasNext()) {
                YamlLine line = iterator.next();
                if (!(previous instanceof YamlLine.NullYamlLine)) {
                    int prevIndent = previous.indentation();
                    if (previous.trimmed().matches("^[ ]*\\-.*\\:.*$")) {
                        prevIndent += 2;
                    }
                    final int lineIndent = line.indentation();
                    if (previous.requireNestedIndentation()) {
                        if (lineIndent != prevIndent + 2) {
                            if (this.guessIndentation) {
                                line = new Indented(line, prevIndent + 2);
                            } else {
                                throw new YamlIndentationException(
                                    "Indentation of line " + (line.number() + 1)
                                        + " is not ok. It should be greater than the one"
                                        + " of line " + (previous.number() + 1)
                                        + " by 2 spaces."
                                );
                            }
                        }
                    } else {
                        if (!"---".equals(previous.trimmed()) && lineIndent > prevIndent) {
                            if (this.guessIndentation) {
                                line = new Indented(line, prevIndent);
                            } else {
                                throw new YamlIndentationException(
                                    "Indentation of line " + (line.number() + 1) + " is "
                                        + "greater than the one of line "
                                        + (previous.number() + 1) + ". "
                                        + "It should be less or equal."
                                );
                            }
                        }
                    }
                }
                previous = line;
                wellIndented.add(line);
            }
        }
        return wellIndented.iterator();
    }

}
