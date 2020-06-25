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

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

final class RtYamlPrinter implements YamlPrinter {

    private final Writer writer;

    RtYamlPrinter(final Writer writer) {
        this.writer = writer;
    }

    @Override
    public void print(final YamlNode node) throws IOException {
        try {
            if (node instanceof Scalar) {
                this.writer.append("---").append(System.lineSeparator());
                this.printScalar((Scalar) node, 0);
                this.writer.append(System.lineSeparator()).append("...");
            } else if (node instanceof YamlSequence) {
                this.printPossibleComment(node, "");
                this.printSequence((YamlSequence) node, 0);
            } else if (node instanceof YamlMapping) {
                this.printPossibleComment(node, "");
                this.printMapping((YamlMapping) node, 0);
            } else if (node instanceof YamlStream) {
                this.printStream((YamlStream) node, 0);
            }
        } finally {
            this.writer.close();
        }
    }

    private void printStream(
        final YamlStream stream,
        final int indentation
    ) throws IOException {
        final String newLine = System.lineSeparator();
        int spaces = indentation;
        final StringBuilder indent = new StringBuilder();
        while (spaces > 0) {
            indent.append(" ");
            spaces--;
        }
        final Iterator<YamlNode> valuesIt = stream.values().iterator();
        while (valuesIt.hasNext()) {
            final YamlNode document = valuesIt.next();
            this.writer
                .append(indent)
                .append("---");
            this.printNode(document, true, indentation + 2);
            if (valuesIt.hasNext()) {
                this.writer.append(newLine);
            }
        }
    }

    private void printMapping(
        final YamlMapping mapping,
        final int indentation
    ) throws IOException {
        final String newLine = System.lineSeparator();
        int spaces = indentation;
        final StringBuilder alignment = new StringBuilder();
        while (spaces > 0) {
            alignment.append(" ");
            spaces--;
        }
        final Iterator<YamlNode> keysIt = mapping.keys().iterator();
        while (keysIt.hasNext()) {
            final YamlNode key = keysIt.next();
            this.printPossibleComment(key, alignment.toString());
            this.writer.append(alignment);
            final YamlNode value = mapping.value(key);
            if (!(value instanceof Scalar)) {
                this.printPossibleComment(value, alignment.toString());
            }
            if (key instanceof Scalar) {
                this.printScalar((Scalar) key, 0);
                this.writer
                    .append(":");
            } else {
                this.writer
                    .append("?");
                this.printNode(key, true, indentation + 2);
                this.writer.append(newLine)
                    .append(alignment)
                    .append(":");
            }
            if (value instanceof Scalar) {
                this.printNode(value, false, 0);
            } else {
                this.printNode(value, true, indentation + 2);
            }
            if (keysIt.hasNext()) {
                this.writer.append(newLine);
            }
        }
    }

    private void printSequence(
        final YamlSequence sequence,
        final int indentation
    ) throws IOException {
        final String newLine = System.lineSeparator();
        int spaces = indentation;
        final StringBuilder alignment = new StringBuilder();
        while (spaces > 0) {
            alignment.append(" ");
            spaces--;
        }
        final Iterator<YamlNode> valuesIt = sequence.values().iterator();
        while (valuesIt.hasNext()) {
            final YamlNode node = valuesIt.next();
            if (node instanceof Scalar) {
                this.writer
                    .append(alignment)
                    .append("-");
                this.printNode(node, false, 0);
            } else {
                this.printPossibleComment(node, alignment.toString());
                this.writer
                    .append(alignment)
                    .append("-");
                this.printNode(node, true, indentation + 2);
            }
            if (valuesIt.hasNext()) {
                this.writer.append(newLine);
            }
        }
    }

    private void printScalar(
        final Scalar scalar,
        final int indentation
    ) throws IOException {
        if (scalar instanceof BaseFoldedScalar) {
            final BaseFoldedScalar foldedScalar = (BaseFoldedScalar) scalar;
            this.writer
                .append(">");
            if (!scalar.comment().value().isEmpty()) {
                this.writer.append(" # ").append(scalar.comment().value());
            }
            this.writer.append(System.lineSeparator());
            final List<String> unfolded = foldedScalar.unfolded();
            for (int idx = 0; idx < unfolded.size(); idx++) {
                this.writer.append(
                    this.indent(
                        unfolded.get(idx).trim(),
                        indentation + 2
                    )
                );
                if (idx < unfolded.size() - 1) {
                    this.writer.append(System.lineSeparator());
                }
            }
        } else if (scalar instanceof RtYamlScalarBuilder.BuiltLiteralBlockScalar
            || scalar instanceof ReadLiteralBlockScalar
        ) {
            this.writer
                .append("|");
            if (!scalar.comment().value().isEmpty()) {
                this.writer.append(" # ").append(scalar.comment().value());
            }
            this.writer
                .append(System.lineSeparator())
                .append(
                    this.indent(scalar.value(), indentation + 2)
                );
        } else {
            this.writer.append(
                this.indent(
                    new RtYamlPrinter.Escaped(scalar).value(),
                    indentation
                )
            );
            if (!scalar.comment().value().isEmpty()) {
                this.writer.append(" # ").append(scalar.comment().value());
            }
        }
    }

    private void printNode(
        final YamlNode node,
        final boolean onNewLine,
        final int indentation
    ) throws IOException {
        if (node == null) {
            this.writer.append(" ").append("null");
        } else if (((BaseYamlNode) node).isEmpty()) {
            this.writer.append(" ").append(node.emptyValue());
        } else {
            if (onNewLine) {
                this.writer.append(System.lineSeparator());
            } else {
                this.writer.append(" ");
            }
            if (node instanceof Scalar) {
                this.printScalar((Scalar) node, indentation);
            } else if (node instanceof YamlSequence) {
                this.printSequence((YamlSequence) node, indentation);
            } else if (node instanceof YamlMapping) {
                this.printMapping((YamlMapping) node, indentation);
            } else if (node instanceof YamlStream) {
                this.printStream((YamlStream) node, indentation);
            }
        }
    }

    private void printPossibleComment(
        final YamlNode node,
        final String alignment
    ) throws IOException {
        if (node != null) {
            final String com = node.comment().value();
            if (com.trim().length() != 0) {
                final String[] lines = com.split(System.lineSeparator());
                for (final String line : lines) {
                    this.writer
                        .append(alignment)
                        .append("# ")
                        .append(line)
                        .append(System.lineSeparator());
                }
            }
        }
    }

    private String indent(final String value, final int indentation) {
        final StringBuilder alignment = new StringBuilder();
        int spaces = indentation;
        while (spaces > 0) {
            alignment.append(" ");
            spaces--;
        }
        final String[] lines = value.split(System.lineSeparator());
        final StringBuilder printed = new StringBuilder();
        for (int idx = 0; idx < lines.length; idx++) {
            printed.append(alignment);
            printed.append(lines[idx]);
            if (idx < lines.length - 1) {
                printed.append(System.lineSeparator());
            }
        }
        return printed.toString();
    }

    static class Escaped extends BaseScalar {

        private final String RESERVED = "#:->|$%&";

        private final Scalar original;

        Escaped(final Scalar original) {
            this.original = original;
        }

        @Override
        public String value() {
            final String value = this.original.value();
            String escaped = null;
            if (value.startsWith("'") && value.endsWith("'")
                || value.startsWith("\"") && value.endsWith("\"")
            ) {
                escaped = value;
            } else {
                for (int idx = 0; idx < value.length(); idx++) {
                    if (this.RESERVED.contains(
                        String.valueOf(value.charAt(idx)))) {
                        if (value.contains("\"")) {
                            escaped = "'" + value + "'";
                        } else {
                            escaped = "\"" + value + "\"";
                        }
                        break;
                    }
                }
                if (escaped == null) {
                    escaped = value;
                }
            }
            return escaped;
        }

        @Override
        public Comment comment() {
            return this.original.comment();
        }

    }

}
