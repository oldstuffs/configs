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

import io.github.portlek.configs.files.yaml.eoyaml.exceptions.YamlReadingException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;


final class ReadYamlMapping extends BaseYamlMapping {


    private final AllYamlLines all;


    private final YamlLines significant;


    private final boolean guessIndentation;


    private final YamlLine previous;


    ReadYamlMapping(final AllYamlLines lines) {
        this(lines, Boolean.FALSE);
    }


    ReadYamlMapping(
        final AllYamlLines lines,
        final boolean guessIndentation
    ) {
        this(new YamlLine.NullYamlLine(), lines, guessIndentation);
    }


    ReadYamlMapping(final YamlLine previous, final AllYamlLines lines) {
        this(previous, lines, Boolean.FALSE);
    }


    ReadYamlMapping(
        final YamlLine previous,
        final AllYamlLines lines,
        final boolean guessIndentation
    ) {
        this.previous = previous;
        this.all = lines;
        this.significant = new SameIndentationLevel(
            new WellIndented(
                new Skip(
                    lines,
                    line -> line.number() <= previous.number(),
                    line -> line.trimmed().startsWith("#"),
                    line -> line.trimmed().startsWith("---"),
                    line -> line.trimmed().startsWith("..."),
                    line -> line.trimmed().startsWith("%"),
                    line -> line.trimmed().startsWith("!!")
                ),
                guessIndentation
            )
        );
        this.guessIndentation = guessIndentation;
    }

    @Override
    public Set<YamlNode> keys() {
        final Set<YamlNode> keys = new LinkedHashSet<>();
        YamlLine prev = new YamlLine.NullYamlLine();
        for (final YamlLine line : this.significant) {
            final String trimmed = line.trimmed();
            if (trimmed.startsWith(":")
                || trimmed.startsWith("-")
                && !(prev instanceof YamlLine.NullYamlLine)
            ) {
                continue;
            } else if ("?".equals(trimmed)) {
                keys.add(
                    this.significant.toYamlNode(line, this.guessIndentation)
                );
            } else {
                if (!trimmed.contains(":")) {
                    continue;
                }
                final String key;
                if (trimmed.startsWith("-")) {
                    key = trimmed.substring(
                        1, trimmed.indexOf(":")
                    ).trim();
                } else {
                    key = trimmed.substring(
                        0, trimmed.indexOf(":")
                    ).trim();
                }
                if (!key.isEmpty()) {
                    keys.add(new PlainStringScalar(key));
                }
            }
            prev = line;
        }
        return keys;
    }

    @Override
    public YamlNode value(final YamlNode key) {
        final YamlNode value;
        if (key instanceof Scalar) {
            value = this.valueOfStringKey(((Scalar) key).value());
        } else {
            value = this.valueOfNodeKey(key);
        }
        return value;
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


    private YamlNode valueOfStringKey(final String key) {
        YamlNode value = null;
        final String[] keys = {
            key,
            "\"" + key + "\"",
            "'" + key + "'",
        };
        for (final String tryKey : keys) {
            for (final YamlLine line : this.significant) {
                final String trimmed = line.trimmed();
                if (trimmed.endsWith(tryKey + ":")
                    || trimmed.matches("^" + tryKey + "\\:[ ]*\\>$")
                    || trimmed.matches("^" + tryKey + "\\:[ ]*\\|$")
                ) {
                    value = this.significant.toYamlNode(
                        line, this.guessIndentation
                    );
                } else if ((trimmed.startsWith(tryKey + ":")
                    || trimmed.startsWith("- " + tryKey + ":"))
                    && trimmed.length() > 1
                ) {
                    value = new ReadPlainScalar(this.all, line);
                }
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }


    private YamlNode valueOfNodeKey(final YamlNode key) {
        YamlNode value = null;
        final Iterator<YamlLine> linesIt = this.significant.iterator();
        while (linesIt.hasNext()) {
            final YamlLine line = linesIt.next();
            final String trimmed = line.trimmed();
            if ("?".equals(trimmed)) {
                final YamlNode keyNode = this.significant.toYamlNode(
                    line, this.guessIndentation
                );
                if (keyNode.equals(key)) {
                    final YamlLine colonLine = linesIt.next();
                    if (":".equals(colonLine.trimmed())
                        || colonLine.trimmed().matches("^\\:[ ]*\\>$")
                        || colonLine.trimmed().matches("^\\:[ ]*\\|$")
                    ) {
                        value = this.significant.toYamlNode(
                            colonLine, this.guessIndentation
                        );
                    } else if (colonLine.trimmed().startsWith(":")
                        && colonLine.trimmed().length() > 1
                    ) {
                        value = new ReadPlainScalar(this.all, colonLine);
                    } else {
                        throw new YamlReadingException(
                            "No value found for existing complex key: "
                                + System.lineSeparator()
                                + key.toString()
                        );
                    }
                    break;
                }
            }
        }
        return value;
    }

}
