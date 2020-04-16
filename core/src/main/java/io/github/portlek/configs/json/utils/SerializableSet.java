/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package io.github.portlek.configs.json.utils;

import io.github.portlek.configs.yaml.configuration.serialization.ConfigurationSerializable;
import io.github.portlek.configs.yaml.configuration.serialization.SerializableAs;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

@SerializableAs("set")
public class SerializableSet implements Set, ConfigurationSerializable {

    @NotNull
    private final Set backingSet;

    public SerializableSet(@NotNull final Set backingSet) {
        this.backingSet = backingSet;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> serializedForm = new HashMap<>(this.backingSet.size());
        final List<Object> contents = new ArrayList(this.backingSet);
        serializedForm.put("contents", contents);
        return serializedForm;
    }

    @Override
    public int size() {
        return this.backingSet.size();
    }

    @Override
    public boolean isEmpty() {
        return this.backingSet.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return this.backingSet.contains(o);
    }

    @NotNull
    @Override
    public Iterator iterator() {
        return this.backingSet.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return this.backingSet.toArray();
    }

    @NotNull
    @Override
    public Object[] toArray(@NotNull final Object[] a) {
        return this.backingSet.toArray(a);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(final Object o) {
        return this.backingSet.add(o);
    }

    @Override
    public boolean remove(final Object o) {
        return this.backingSet.remove(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsAll(@NotNull final Collection c) {
        return this.backingSet.containsAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(@NotNull final Collection c) {
        return this.backingSet.addAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(@NotNull final Collection c) {
        return this.backingSet.retainAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(@NotNull final Collection c) {
        return this.backingSet.removeAll(c);
    }

    @Override
    public void clear() {
        this.backingSet.clear();
    }

    @Override
    public Spliterator spliterator() {
        return this.backingSet.spliterator();
    }

    @Override
    public int hashCode() {
        return this.backingSet.hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(final Object o) {
        return this.backingSet.equals(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeIf(final Predicate filter) {
        return this.backingSet.removeIf(filter);
    }

    @Override
    public Stream stream() {
        return this.backingSet.stream();
    }

    @Override
    public Stream parallelStream() {
        return this.backingSet.parallelStream();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void forEach(final Consumer action) {
        this.backingSet.forEach(action);
    }

}
