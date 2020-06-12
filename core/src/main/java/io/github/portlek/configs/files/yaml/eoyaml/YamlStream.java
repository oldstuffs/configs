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

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * A YAML Stream of documents. Documents are separated by 3 dashes (---).<br>
 * This interface also offers integrations with Java 8's Stream API.<br>
 * All the methods have a default implementations based on the YamlNode
 * values Collection.
 *
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id: aad913b2279664e8464f5d0a6f56bb4658687351 $
 * @checkstyle FinalParameters (400 lines)
 * @checkstyle JavadocMethod (400 lines)
 * @checkstyle LineLength (400 lines)
 * @checkstyle ParameterName (400 lines)
 * @checkstyle FinalParameters (400 lines)
 * @since 3.1.1
 */
public interface YamlStream extends YamlNode, Stream<YamlNode> {

    /**
     * Fetch the values from this stream.
     *
     * @return Collection of {@link YamlNode}
     */
    Collection<YamlNode> values();

    default Comment comment() {
        return new BuiltComment(this, "");
    }

    @Override
    default Iterator<YamlNode> iterator() {
        return this.values().stream().iterator();
    }

    @Override
    default Spliterator<YamlNode> spliterator() {
        return this.values().stream().spliterator();
    }

    @Override
    default boolean isParallel() {
        return this.values().stream().isParallel();
    }

    @Override
    default Stream<YamlNode> sequential() {
        return this.values().stream().sequential();
    }

    @Override
    default Stream<YamlNode> parallel() {
        return this.values().stream().parallel();
    }

    @Override
    default Stream<YamlNode> unordered() {
        return this.values().stream().unordered();
    }

    @Override
    default Stream<YamlNode> onClose(final Runnable closeHandler) {
        return this.values().stream().onClose(closeHandler);
    }

    @Override
    default void close() {
        this.values().stream().close();
    }

    @Override
    default Stream<YamlNode> filter(final Predicate<? super YamlNode> predicate) {
        return this.values().stream().filter(predicate);
    }

    @Override
    default <R> Stream<R> map(final Function<? super YamlNode, ? extends R> mapper) {
        return this.values().stream().map(mapper);
    }

    @Override
    default IntStream mapToInt(final ToIntFunction<? super YamlNode> mapper) {
        return this.values().stream().mapToInt(mapper);
    }

    @Override
    default LongStream mapToLong(final ToLongFunction<? super YamlNode> mapper) {
        return this.values().stream().mapToLong(mapper);
    }

    @Override
    default DoubleStream mapToDouble(final ToDoubleFunction<? super YamlNode> mapper) {
        return this.values().stream().mapToDouble(mapper);
    }

    @Override
    default <R> Stream<R> flatMap(final Function<? super YamlNode, ? extends Stream<? extends R>> mapper) {
        return this.values().stream().flatMap(mapper);
    }

    @Override
    default IntStream flatMapToInt(final Function<? super YamlNode, ? extends IntStream> mapper) {
        return this.values().stream().flatMapToInt(mapper);
    }

    @Override
    default LongStream flatMapToLong(final Function<? super YamlNode, ? extends LongStream> mapper) {
        return this.values().stream().flatMapToLong(mapper);
    }

    @Override
    default DoubleStream flatMapToDouble(final Function<? super YamlNode, ? extends DoubleStream> mapper) {
        return this.values().stream().flatMapToDouble(mapper);
    }

    @Override
    default Stream<YamlNode> distinct() {
        return this.values().stream().distinct();
    }

    @Override
    default Stream<YamlNode> sorted() {
        return this.values().stream().sorted();
    }

    @Override
    default Stream<YamlNode> sorted(final Comparator<? super YamlNode> comparator) {
        return this.values().stream().sorted(comparator);
    }

    @Override
    default Stream<YamlNode> peek(final Consumer<? super YamlNode> action) {
        return this.values().stream().peek(action);
    }

    @Override
    default Stream<YamlNode> limit(final long maxSize) {
        return this.values().stream().limit(maxSize);
    }

    @Override
    default Stream<YamlNode> skip(final long n) {
        return this.values().stream().skip(n);
    }

    @Override
    default void forEach(final Consumer<? super YamlNode> action) {
        this.values().stream().forEach(action);
    }

    @Override
    default void forEachOrdered(final Consumer<? super YamlNode> action) {
        this.values().stream().forEachOrdered(action);
    }

    @Override
    default Object[] toArray() {
        return this.values().stream().toArray();
    }

    @Override
    default <A> A[] toArray(final IntFunction<A[]> generator) {
        return this.values().stream().toArray(generator);
    }

    @Override
    default YamlNode reduce(final YamlNode identity, final BinaryOperator<YamlNode> accumulator) {
        return this.values().stream().reduce(identity, accumulator);
    }

    @Override
    default Optional<YamlNode> reduce(final BinaryOperator<YamlNode> accumulator) {
        return this.values().stream().reduce(accumulator);
    }

    @Override
    default <U> U reduce(final U identity, final BiFunction<U, ? super YamlNode, U> accumulator, final BinaryOperator<U> combiner) {
        return this.values().stream().reduce(identity, accumulator, combiner);
    }

    @Override
    default <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super YamlNode> accumulator,
                          final BiConsumer<R, R> combiner) {
        return this.values().stream().collect(supplier, accumulator, combiner);
    }

    @Override
    default <R, A> R collect(final Collector<? super YamlNode, A, R> collector) {
        return this.values().stream().collect(collector);
    }

    @Override
    default Optional<YamlNode> min(final Comparator<? super YamlNode> comparator) {
        return this.values().stream().min(comparator);
    }

    @Override
    default Optional<YamlNode> max(final Comparator<? super YamlNode> comparator) {
        return this.values().stream().max(comparator);
    }

    @Override
    default long count() {
        return this.values().stream().count();
    }

    @Override
    default boolean anyMatch(final Predicate<? super YamlNode> predicate) {
        return this.values().stream().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(final Predicate<? super YamlNode> predicate) {
        return this.values().stream().allMatch(predicate);
    }

    @Override
    default boolean noneMatch(final Predicate<? super YamlNode> predicate) {
        return this.values().stream().noneMatch(predicate);
    }

    @Override
    default Optional<YamlNode> findFirst() {
        return this.values().stream().findFirst();
    }

    @Override
    default Optional<YamlNode> findAny() {
        return this.values().stream().findAny();
    }

}
