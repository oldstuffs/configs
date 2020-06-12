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

import io.github.portlek.configs.util.GeneralUtilities;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Yaml Scalar.
 *
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id: a3fc0cf8017a486e72dca505f285e3d20dca5a58 $
 * @since 3.1.3
 */
public interface Scalar extends YamlNode {

    /**
     * Value of this scalar. The value returned by this
     * method should be un-escaped. e.g. if the scalar is
     * escaped, say '#404040', the returned value should be
     * #404040.
     *
     * @return String value.
     * @throws IllegalStateException In the case of reading YAML,
     * this exception is thrown if the Scalar isn't found where it's
     * supposed to be.
     */
    String value();
    
    @Nullable
    default Object getAsAll() {
        final Optional<Integer> optional1 = this.getAsInteger();
        if (optional1.isPresent()) {
            return optional1.get();
        }
        final Optional<Long> optional2 = this.getAsLong();
        if (optional2.isPresent()) {
            return optional2.get();
        }
        final Optional<Float> optional3 = this.getAsFloat();
        if (optional3.isPresent()) {
            return optional3.get();
        }
        final Optional<Double> optional4 = this.getAsDouble();
        if (optional4.isPresent()) {
            return optional4.get();
        }
        final Optional<Short> optional5 = this.getAsShort();
        if (optional5.isPresent()) {
            return optional5.get();
        }
        final Optional<Boolean> optional6 = this.getAsBoolean();
        if (optional6.isPresent()) {
            return optional6.get();
        }
        final Optional<Character> optional7 = this.getAsCharacter();
        if (optional7.isPresent()) {
            return optional7.get();
        }
        final Optional<Byte> optional8 = this.getAsByte();
        if (optional8.isPresent()) {
            return optional8.get();
        }
        return this.getAsString().orElse(null);
    }

    @NotNull
    default Optional<Integer> getAsInteger() {
        return Optional.ofNullable(this.value())
            .flatMap(GeneralUtilities::toInt);
    }

    @NotNull
    default Optional<Long> getAsLong() {
        return Optional.ofNullable(this.value())
            .flatMap(GeneralUtilities::toLong);
    }

    @NotNull
    default Optional<Float> getAsFloat() {
        return Optional.ofNullable(this.value())
            .flatMap(GeneralUtilities::toFloat);
    }

    @NotNull
    default Optional<Double> getAsDouble() {
        return Optional.ofNullable(this.value())
            .flatMap(GeneralUtilities::toDouble);
    }

    @NotNull
    default Optional<Short> getAsShort() {
        return Optional.ofNullable(this.value())
            .flatMap(GeneralUtilities::toShort);
    }

    @NotNull
    default Optional<String> getAsString() {
        return Optional.ofNullable(this.value());
    }

    @NotNull
    default Optional<Boolean> getAsBoolean() {
        return Optional.ofNullable(this.value())
            .flatMap(GeneralUtilities::toBoolean);
    }

    @NotNull
    default Optional<Character> getAsCharacter() {
        return Optional.ofNullable(this.value())
            .flatMap(GeneralUtilities::toCharacter);
    }

    @NotNull
    default Optional<Byte> getAsByte() {
        return Optional.ofNullable(this.value())
            .flatMap(GeneralUtilities::toByte);
    }

}
