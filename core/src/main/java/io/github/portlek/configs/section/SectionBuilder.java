/*
 * MIT License
 *
 * Copyright (c) 2021 Hasan Demirtaş
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

package io.github.portlek.configs.section;

import io.github.portlek.configs.CfgSection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public final class SectionBuilder {

  private final Collection<SectionBuilder> parents = new ArrayList<>();

  private final Collection<Runnable> runs = new ArrayList<>();

  @NotNull
  private final CfgSection section;

  private static void buildParents(@NotNull final SectionBuilder builder) {
    builder.getRuns().forEach(Runnable::run);
    builder.getParents().forEach(SectionBuilder::buildParents);
  }

  public void build() {
    final boolean save = this.section.getParent().isAutoSave();
    this.section.getParent().setAutoSave(false);
    SectionBuilder.buildParents(this);
    this.section.getParent().setAutoSave(save);
  }

  @NotNull
  public SectionBuilder createIfAbsentSection(@NotNull final String path,
                                              @NotNull final Function<SectionBuilder, SectionBuilder> func) {
    return this.add(() ->
      this.parents.add(func.apply(new SectionBuilder(this.section.getOrCreateSection(path)))));
  }

  @NotNull
  public SectionBuilder createSection(@NotNull final String path,
                                      @NotNull final Function<SectionBuilder, SectionBuilder> func) {
    return this.add(() ->
      this.parents.add(func.apply(new SectionBuilder(this.section.createSection(path)))));
  }

  @NotNull
  public SectionBuilder remove(@NotNull final String path) {
    return this.add(() ->
      this.section.set(path, null));
  }

  @NotNull
  public SectionBuilder set(@NotNull final String path, @Nullable final Object object) {
    return this.add(() ->
      this.section.set(path, object));
  }

  @NotNull
  public SectionBuilder setIfAbsentBoolean(@NotNull final String path, @NotNull final Boolean fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getBoolean);
  }

  @NotNull
  public SectionBuilder setIfAbsentBooleanList(@NotNull final String path, @NotNull final List<Boolean> fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getBooleanList);
  }

  @NotNull
  public SectionBuilder setIfAbsentByteList(@NotNull final String path, @NotNull final List<Byte> fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getByteList);
  }

  @NotNull
  public SectionBuilder setIfAbsentCharacterList(@NotNull final String path, @NotNull final List<Character> fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getCharacterList);
  }

  @NotNull
  public SectionBuilder setIfAbsentDouble(@NotNull final String path, @NotNull final Double fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getDouble);
  }

  @NotNull
  public SectionBuilder setIfAbsentDoubleList(@NotNull final String path, @NotNull final List<Double> fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getDoubleList);
  }

  @NotNull
  public SectionBuilder setIfAbsentFloatList(@NotNull final String path, @NotNull final List<Float> fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getFloatList);
  }

  @NotNull
  public <T> SectionBuilder setIfAbsentGeneric(@NotNull final String path, @NotNull final T fallback,
                                               @NotNull final Function<String, Optional<T>> function) {
    return this.add(() ->
      this.section.getOrSetGeneric(path, fallback, function));
  }

  @NotNull
  public SectionBuilder setIfAbsentInteger(@NotNull final String path, @NotNull final Integer fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getInteger);
  }

  @NotNull
  public SectionBuilder setIfAbsentIntegerList(@NotNull final String path, @NotNull final List<Integer> fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getIntegerList);
  }

  @NotNull
  public SectionBuilder setIfAbsentLong(@NotNull final String path, @NotNull final Long fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getLong);
  }

  @NotNull
  public SectionBuilder setIfAbsentLongList(@NotNull final String path, @NotNull final List<Long> fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getLongList);
  }

  @NotNull
  public SectionBuilder setIfAbsentShortList(@NotNull final String path, @NotNull final List<Short> fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getShortList);
  }

  public SectionBuilder setIfAbsentString(@NotNull final String path, @NotNull final String fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getString);
  }

  @NotNull
  public SectionBuilder setIfAbsentStringList(@NotNull final String path, @NotNull final List<String> fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getStringList);
  }

  @NotNull
  public SectionBuilder setIfAbsentUniqueId(@NotNull final String path, @NotNull final UUID fallback) {
    return this.setIfAbsentGeneric(path, fallback, this.section::getUniqueId);
  }

  @NotNull
  public SectionBuilder setIfAbsentUniqueId(@NotNull final String path, @NotNull final String fallback) {
    return this.setIfAbsentUniqueId(path, UUID.fromString(fallback));
  }

  @NotNull
  private SectionBuilder add(@NotNull final Runnable run) {
    this.runs.add(run);
    return this;
  }

  @NotNull
  private Collection<SectionBuilder> getParents() {
    return Collections.unmodifiableCollection(this.parents);
  }

  @NotNull
  private Collection<Runnable> getRuns() {
    return Collections.unmodifiableCollection(this.runs);
  }
}
