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

package io.github.portlek.configs.snakeyaml.bukkit;

import io.github.portlek.transformer.TransformResolver;
import io.github.portlek.transformer.TransformedObject;
import io.github.portlek.transformer.declarations.FieldDeclaration;
import io.github.portlek.transformer.declarations.GenericDeclaration;
import io.github.portlek.transformer.declarations.TransformedObjectDeclaration;
import io.github.portlek.transformer.exceptions.TransformException;
import io.github.portlek.transformer.postprocessor.LineInfo;
import io.github.portlek.transformer.postprocessor.PostProcessor;
import io.github.portlek.transformer.postprocessor.SectionSeparator;
import io.github.portlek.transformer.postprocessor.walkers.YamlSectionWalker;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * a class that represents yaml file configuration.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class YamlBukkitConfigurer extends TransformResolver {

  /**
   * the comment prefix.
   */
  @NotNull
  private final String commentPrefix;

  /**
   * the config.
   */
  @NotNull
  private final YamlConfiguration config;

  /**
   * the section separator.
   */
  @NotNull
  private final String sectionSeparator;

  /**
   * ctor.
   *
   * @param config the config.
   * @param commentPrefix the comment prefix.
   * @param sectionSeparator the section separator.
   */
  public YamlBukkitConfigurer(@NotNull final YamlConfiguration config, @NotNull final String commentPrefix,
                              @NotNull final String sectionSeparator) {
    this(commentPrefix, config, sectionSeparator);
  }

  /**
   * ctor.
   *
   * @param commentPrefix the comment prefix.
   * @param sectionSeparator the section separator.
   */
  public YamlBukkitConfigurer(@NotNull final String commentPrefix, @NotNull final String sectionSeparator) {
    this(commentPrefix, new YamlConfiguration(), sectionSeparator);
  }

  /**
   * ctor.
   *
   * @param config the config.
   */
  public YamlBukkitConfigurer(@NotNull final YamlConfiguration config) {
    this(" #", config, SectionSeparator.NONE);
  }

  /**
   * ctor.
   *
   * @param sectionSeparator the section separator.
   */
  public YamlBukkitConfigurer(@NotNull final String sectionSeparator) {
    this(" #", sectionSeparator);
  }

  /**
   * ctor.
   */
  public YamlBukkitConfigurer() {
    this(new YamlConfiguration());
  }

  @Nullable
  @Override
  public <T> T deserialize(@Nullable final Object object, @Nullable final GenericDeclaration genericSource,
                           @NotNull final Class<T> targetClass, @Nullable final GenericDeclaration genericTarget,
                           @Nullable final Object defaultValue)
    throws TransformException {
    if (object instanceof MemorySection) {
      final var values = this.getMapValues((ConfigurationSection) object, false);
      return super.deserialize(values, GenericDeclaration.of(values), targetClass, genericTarget, defaultValue);
    }
    return super.deserialize(object, genericSource, targetClass, genericTarget, defaultValue);
  }

  @NotNull
  @Override
  public List<String> getAllKeys() {
    return List.copyOf(this.config.getKeys(false));
  }

  @NotNull
  @Override
  public Optional<Object> getValue(@NotNull final String path) {
    return Optional.ofNullable(this.config.get(path));
  }

  @Override
  public void load(@NotNull final InputStream inputStream, @NotNull final TransformedObjectDeclaration declaration)
    throws Exception {
    this.config.loadFromString(PostProcessor.of(inputStream).getContext());
  }

  @Override
  public boolean pathExists(@NotNull final String path) {
    return this.config.getKeys(false).contains(path);
  }

  @Override
  public void removeValue(@NotNull final String path, @Nullable final GenericDeclaration genericType,
                          @Nullable final FieldDeclaration field) {
    this.config.set(path, null);
  }

  @Nullable
  @Override
  public Object serialize(@Nullable final Object value, @Nullable final GenericDeclaration genericType,
                          final boolean conservative) throws TransformException {
    if (value instanceof ConfigurationSection) {
      return this.getMapValues((ConfigurationSection) value, false);
    }
    return super.serialize(value, genericType, conservative);
  }

  @Override
  public void setValue(@NotNull final String path, @Nullable final Object value,
                       @Nullable final GenericDeclaration genericType, @Nullable final FieldDeclaration field) {
    this.config.set(path, this.serialize(value, genericType, true));
  }

  @Override
  public void write(@NotNull final OutputStream outputStream, @NotNull final TransformedObjectDeclaration declaration) {
    final var contents = this.config.saveToString();
    final var processor = PostProcessor.of(contents)
      .removeLines(line -> line.startsWith(this.commentPrefix.trim()))
      .updateLinesPaths(new YamlSectionWalker() {
        @NotNull
        @Override
        public String update(@NotNull final String line, @NotNull final LineInfo lineInfo,
                             @NotNull final List<LineInfo> path) {
          var currentDeclaration = declaration;
          for (int i = 0; i < path.size() - 1; i++) {
            final var pathElement = path.get(i);
            final var field = currentDeclaration.getNonMigratedFields().get(pathElement.getName());
            if (field == null) {
              return line;
            }
            final var fieldType = field.getGenericDeclaration();
            final var type = fieldType.getType();
            if (type == null) {
              continue;
            }
            if (!TransformedObject.class.isAssignableFrom(type)) {
              return line;
            }
            currentDeclaration = TransformedObjectDeclaration.of(type);
          }
          final var lineDeclaration = currentDeclaration.getNonMigratedFields().get(lineInfo.getName());
          if (lineDeclaration == null) {
            return line;
          }
          final var fieldComment = lineDeclaration.getComment();
          if (fieldComment == null) {
            return line;
          }
          return PostProcessor.addIndent(
            PostProcessor.createComment(YamlBukkitConfigurer.this.commentPrefix, fieldComment.value()),
            lineInfo.getIndent()) + line;
        }
      });
    final var header = declaration.getHeader();
    if (header != null) {
      processor.prependContextComment(this.commentPrefix, this.sectionSeparator, header.value());
    }
    processor.write(outputStream);
  }

  /**
   * gets the section value with primitive objects.
   *
   * @param section the section to get.
   * @param deep the deep to get.
   *
   * @return map values.
   */
  @NotNull
  private Map<String, Object> getMapValues(@NotNull final ConfigurationSection section, final boolean deep) {
    return section.getValues(deep).entrySet().stream()
      .map(entry -> {
        final var key = entry.getKey();
        final var value = entry.getValue();
        if (value instanceof ConfigurationSection) {
          return Map.entry(key, this.getMapValues(section, deep));
        }
        return Map.entry(key, value);
      })
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }
}
