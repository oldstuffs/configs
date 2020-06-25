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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


final class RtYamlScalarBuilder implements YamlScalarBuilder {


    private final List<String> lines;


    RtYamlScalarBuilder() {
        this(new LinkedList<>());
    }


    RtYamlScalarBuilder(final List<String> lines) {
        this.lines = lines;
    }

    @Override
    public YamlScalarBuilder addLine(final String value) {
        final List<String> all = new LinkedList<>();
        all.addAll(this.lines);
        all.add(value);
        return new RtYamlScalarBuilder(all);
    }

    @Override
    public Scalar buildPlainScalar(final String comment) {
        final String plain = this.lines.stream().filter(line -> line != null).map(
            line -> line.replaceAll(System.lineSeparator(), " ")
        ).collect(Collectors.joining(" "));
        return new PlainStringScalar(plain, comment);
    }

    @Override
    public Scalar buildFoldedBlockScalar(final String comment) {
        return new RtYamlScalarBuilder.BuiltFoldedBlockScalar(this.lines, comment);
    }

    @Override
    public Scalar buildLiteralBlockScalar(final String comment) {
        return new RtYamlScalarBuilder.BuiltLiteralBlockScalar(this.lines, comment);
    }


    static class BuiltFoldedBlockScalar extends BaseFoldedScalar {


        private final Comment comment;


        private final List<String> lines;


        BuiltFoldedBlockScalar(final List<String> lines) {
            this(lines, "");
        }


        BuiltFoldedBlockScalar(
            final List<String> lines, final String comment
        ) {
            this.lines = lines;
            this.comment = new BuiltComment(this, comment);
        }


        @Override
        public String value() {
            return this.lines.stream().map(
                line -> line.replaceAll(System.lineSeparator(), " ")
            ).collect(Collectors.joining(" "));
        }

        @Override
        public Comment comment() {
            return this.comment;
        }

        @Override
        final List<String> unfolded() {
            final List<String> unfolded = new ArrayList<>();
            unfolded.addAll(this.lines);
            return unfolded;
        }

    }


    static class BuiltLiteralBlockScalar extends BaseScalar {


        private final Comment comment;

        /**
         * Lines of this scalar.
         */
        private final List<String> lines;

        /**
         * Ctor.
         *
         * @param lines Given string lines.
         */
        BuiltLiteralBlockScalar(final List<String> lines) {
            this(lines, "");
        }

        /**
         * Ctor.
         *
         * @param lines Given string lines.
         * @param comment Comment referring to this scalar.
         */
        BuiltLiteralBlockScalar(
            final List<String> lines, final String comment
        ) {
            this.lines = lines;
            this.comment = new BuiltComment(this, comment);
        }

        /**
         * Return the value of this literal scalar.
         *
         * @return String value.
         */
        @Override
        public String value() {
            return this.lines.stream().collect(
                Collectors.joining(System.lineSeparator())
            );
        }

        @Override
        public Comment comment() {
            return this.comment;
        }

    }

}
