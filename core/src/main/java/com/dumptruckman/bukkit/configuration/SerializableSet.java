package com.dumptruckman.bukkit.configuration;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.serialization.ConfigurationSerializable;
import org.simpleyaml.configuration.serialization.SerializableAs;

@SerializableAs("set")
public class SerializableSet implements Set, ConfigurationSerializable {

    @NotNull
    private final Set backingSet;

    public SerializableSet(@NotNull final Set backingSet) {
        this.backingSet = backingSet;
    }

    @SuppressWarnings("unchecked")
    public SerializableSet(@NotNull final Map<String, Object> serializedForm) {
        final Object o = serializedForm.get("contents");
        if (o instanceof List) {
            this.backingSet = new HashSet((List) o);
        } else {
            this.backingSet = Collections.emptySet();
        }
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
