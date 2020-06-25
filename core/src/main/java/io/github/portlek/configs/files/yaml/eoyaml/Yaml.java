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

import java.io.*;


public final class Yaml {


    private Yaml() {
    }


    public static YamlMappingBuilder createYamlMappingBuilder() {
        return new RtYamlMappingBuilder();
    }


    public static YamlSequenceBuilder createYamlSequenceBuilder() {
        return new RtYamlSequenceBuilder();
    }


    public static YamlScalarBuilder createYamlScalarBuilder() {
        return new RtYamlScalarBuilder();
    }


    public static YamlStreamBuilder createYamlStreamBuilder() {
        return new RtYamlStreamBuilder();
    }


    public static YamlInput createYamlInput(final File input)
        throws FileNotFoundException {
        return Yaml.createYamlInput(input, Boolean.FALSE);
    }


    public static YamlInput createYamlInput(
        final File input,
        final boolean guessIndentation
    ) throws FileNotFoundException {
        return Yaml.createYamlInput(
            new FileInputStream(input),
            guessIndentation
        );
    }


    public static YamlInput createYamlInput(final String input) {
        return Yaml.createYamlInput(input, Boolean.FALSE);
    }


    public static YamlInput createYamlInput(
        final String input,
        final boolean guessIndentation
    ) {
        return Yaml.createYamlInput(
            new ByteArrayInputStream(input.getBytes()),
            guessIndentation
        );
    }


    public static YamlInput createYamlInput(final InputStream input) {
        return Yaml.createYamlInput(input, Boolean.FALSE);
    }


    public static YamlInput createYamlInput(
        final InputStream input,
        final boolean guessIndentation
    ) {
        return new RtYamlInput(input, guessIndentation);
    }


    public static YamlPrinter createYamlPrinter(final Writer destination) {
        return new RtYamlPrinter(destination);
    }


    public static YamlDump createYamlDump(final Object object) {
        return new ReflectedYamlDump(object);
    }

}
