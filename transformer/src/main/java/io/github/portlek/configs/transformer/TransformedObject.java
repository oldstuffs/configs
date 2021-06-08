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

package io.github.portlek.configs.transformer;

import io.github.portlek.configs.transformer.declarations.GenericDeclaration;
import io.github.portlek.configs.transformer.declarations.TransformedObjectDeclaration;
import io.github.portlek.configs.transformer.exceptions.TransformException;
import io.github.portlek.configs.transformer.transformers.TransformerPack;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an abstract class that represents transformed objects.
 */
public abstract class TransformedObject {

  /**
   * the declaration.
   */
  @Nullable
  @Getter
  private TransformedObjectDeclaration declaration;

  /**
   * the file.
   */
  @Nullable
  private File file;

  /**
   * the resolver.
   */
  @Nullable
  private TransformResolver resolver;

  /**
   * checks if the file exists or not.
   *
   * @return {@code true} if the file exists.
   */
  public static boolean exists(@NotNull final File file) {
    return Files.exists(file.toPath());
  }

  /**
   * get values as map.
   *
   * @param resolver the resolver to get.
   * @param conservative the conservative to get.
   *
   * @throws TransformException if something goes wrong when getting the value as map.
   */
  @NotNull
  public final Map<String, Object> asMap(@NotNull final TransformResolver resolver, final boolean conservative)
    throws TransformException {
    Objects.requireNonNull(this.declaration, "declaration");
    final var map = new LinkedHashMap<String, Object>();
    this.declaration.getFields().forEach((key, fieldDeclaration) -> map.put(
      fieldDeclaration.getPath(),
      resolver.serialize(fieldDeclaration.getValue(), fieldDeclaration.getGenericDeclaration(), conservative)));
    if (this.resolver == null) {
      return map;
    }
    this.resolver.getAllKeys().stream()
      .filter(keyName -> !map.containsKey(keyName))
      .forEach(keyName -> {
        final var value = this.resolver.getValue(keyName);
        map.put(keyName, this.resolver.serialize(value, GenericDeclaration.of(value), conservative));
      });
    return map;
  }

  /**
   * checks if the file exists or not.
   *
   * @return {@code true} if the file exists.
   */
  public final boolean exists() {
    Objects.requireNonNull(this.file, "file");
    return TransformedObject.exists(this.file);
  }

  /**
   * gets the value at path.
   *
   * @param path the path to get.
   * @param cls the cls to get.
   * @param <T> type of the value.
   *
   * @return value at path.
   */
  @NotNull
  public final <T> Optional<T> get(@NotNull final String path, @NotNull final Class<T> cls) {
    Objects.requireNonNull(this.resolver, "resolver");
    Objects.requireNonNull(this.declaration, "declaration");
    final var field = this.declaration.getFields().get(path);
    if (field == null) {
      return this.resolver.getValue(path, cls, null);
    }
    return Optional.ofNullable(this.resolver.deserialize(
      field.getValue(),
      field.getGenericDeclaration(),
      cls,
      GenericDeclaration.of(cls)));
  }

  /**
   * gets value at path.
   *
   * @param path the path to get.
   *
   * @return value at path.
   */
  @NotNull
  public final Optional<Object> get(@NotNull final String path) {
    Objects.requireNonNull(this.resolver, "resolver");
    Objects.requireNonNull(this.declaration, "declaration");
    final var field = this.declaration.getFields().get(path);
    if (field != null) {
      return Optional.ofNullable(field.getValue());
    }
    return this.resolver.getValue(path);
  }

  /**
   * loads the transformed object.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when loading the objects.
   */
  @NotNull
  public final TransformedObject initiate() throws TransformException {
    if (this.exists()) {
      return this.load();
    } else {
      return this.save();
    }
  }

  /**
   * loads the transformed object.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when loading the objects.
   */
  @NotNull
  public final TransformedObject initiate(@NotNull final File file) throws TransformException {
    if (TransformedObject.exists(file)) {
      return this.load(file);
    } else {
      return this.save(file);
    }
  }

  /**
   * loads the transformed object.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when loading the objects.
   */
  @NotNull
  public final TransformedObject load() throws TransformException {
    Objects.requireNonNull(this.file, "file");
    return this.load(this.file);
  }

  /**
   * loads the transformed object.
   *
   * @param file the file to load.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when loading the objects.
   */
  @NotNull
  public final TransformedObject load(@NotNull final File file) throws TransformException {
    try {
      return this.load(new FileInputStream(file));
    } catch (final FileNotFoundException exception) {
      throw new TransformException(String.format("Failed use #load with %s", file), exception);
    }
  }

  /**
   * loads the transformed object.
   *
   * @param data the data to load.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when loading the objects.
   */
  @NotNull
  public final TransformedObject load(@NotNull final String data) {
    return this.load(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
  }

  /**
   * loads the transformed object.
   *
   * @param inputStream the input stream to load.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when loading the objects.
   */
  @NotNull
  public final TransformedObject load(@NotNull final InputStream inputStream) {
    Objects.requireNonNull(this.resolver, "resolver");
    Objects.requireNonNull(this.declaration, "declaration");
    try {
      this.resolver.load(inputStream, this.declaration);
    } catch (final Exception exception) {
      throw new TransformException("failed #load", exception);
    }
    return this.update();
  }

  /**
   * loads the transformed object.
   *
   * @param update the update to load.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when loading the objects.
   */
  @NotNull
  public final TransformedObject load(final boolean update) throws TransformException {
    this.load();
    if (update) {
      this.save();
    }
    return this;
  }

  /**
   * saves the objects into the {@link #file}.
   *
   * @param file the file to save.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when saving objects into the file.
   */
  public final TransformedObject save(@NotNull final File file) throws TransformException {
    try {
      final var parentFile = file.getParentFile();
      if (!TransformedObject.exists(parentFile)) {
        Files.createDirectories(parentFile.toPath());
      }
      return this.save(new PrintStream(new FileOutputStream(file, false), true, StandardCharsets.UTF_8.name()));
    } catch (final Exception exception) {
      throw new TransformException(String.format("Failed use #save with %s", file), exception);
    }
  }

  /**
   * saves the objects into the {@link #file}.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when saving objects into the file.
   */
  public final TransformedObject save() throws TransformException {
    Objects.requireNonNull(this.file, "file");
    return this.save(this.file);
  }

  /**
   * saves the values into the output stream.
   *
   * @param outputStream the output stream to save.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when saving the objects into the stream.
   */
  @NotNull
  public final TransformedObject save(@NotNull final OutputStream outputStream) throws TransformException {
    Objects.requireNonNull(this.declaration, "declaration");
    Objects.requireNonNull(this.resolver, "resolver");
    for (final var entry : this.declaration.getFields().entrySet()) {
      final var fieldDeclaration = entry.getValue();
      final var path = fieldDeclaration.getPath();
      final var fieldValue = fieldDeclaration.getValue();
      if (!this.resolver.isValid(fieldDeclaration, fieldValue)) {
        throw new TransformException(String.format("%s marked %s as invalid without throwing an exception",
          this.resolver.getClass(), path));
      }
      try {
        this.resolver.setValue(path, fieldValue, fieldDeclaration.getGenericDeclaration(), fieldDeclaration);
      } catch (final Exception exception) {
        throw new TransformException(String.format("Failed to use #setValue for %s", path), exception);
      }
    }
    try {
      this.resolver.write(outputStream, this.declaration);
    } catch (final Exception exception) {
      throw new TransformException("Failed use #write", exception);
    }
    return this;
  }

  /**
   * saves default values into the file.
   *
   * @return {@code this} for builder chain.
   *
   * @throws TransformException if something goes wrong when sawing the defaults.
   */
  @NotNull
  public final TransformedObject saveDefaults() throws TransformException {
    if (this.exists()) {
      this.save();
    }
    return this;
  }

  /**
   * saves values to string.
   *
   * @return saved string.
   *
   * @throws TransformException if something goes wrong when saving the objects.
   */
  @NotNull
  public final String saveToString() throws TransformException {
    final var outputStream = new ByteArrayOutputStream();
    this.save(outputStream);
    return outputStream.toString(StandardCharsets.UTF_8);
  }

  /**
   * sets the value to path.
   *
   * @param path the path to set.
   * @param value the value to set.
   *
   * @return {@code this} for builder chain.
   */
  public final TransformedObject set(@NotNull final String path, @NotNull final Object value) {
    Objects.requireNonNull(this.resolver, "resolver");
    Objects.requireNonNull(this.declaration, "declaration");
    final var field = this.declaration.getFields().get(path);
    var tempValue = value;
    if (field != null) {
      final var declaration = field.getGenericDeclaration();
      if (declaration.getType() != null) {
        tempValue = this.resolver.deserialize(tempValue, GenericDeclaration.of(tempValue), declaration.getType(), declaration);
      }
      field.setValue(tempValue);
    }
    final var fieldGenerics = field == null ? null : field.getGenericDeclaration();
    this.resolver.setValue(path, tempValue, fieldGenerics, field);
    return this;
  }

  /**
   * updates the transformed object.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public final TransformedObject update() {
    Objects.requireNonNull(this.declaration, "declaration");
    Objects.requireNonNull(this.resolver, "resolver");
    for (final var entry : this.declaration.getFields().entrySet()) {
      final var fieldDeclaration = entry.getValue();
      final var fieldPath = fieldDeclaration.getPath();
      final var genericType = fieldDeclaration.getGenericDeclaration();
      final var type = Objects.requireNonNull(genericType.getType(), "type");
      final var variable = fieldDeclaration.getVariable();
      var updateValue = true;
      if (variable != null) {
        final var variableValue = variable.value();
        final var property = System.getProperty(variableValue, System.getenv(variableValue));
        if (property != null) {
          final Object value;
          try {
            value = this.resolver.deserialize(property, GenericDeclaration.of(property), type, genericType);
          } catch (final Exception exception) {
            throw new TransformException(String.format("Failed to use #deserialize for @Variable { %s }",
              variableValue), exception);
          }
          if (!this.resolver.isValid(fieldDeclaration, value)) {
            throw new TransformException(String.format("%s marked %s as invalid without throwing an exception",
              this.resolver.getClass(), fieldPath));
          }
          fieldDeclaration.setValue(value);
          fieldDeclaration.setHideVariable(true);
          updateValue = false;
        }
      }
      if (!this.resolver.pathExists(fieldPath)) {
        continue;
      }
      final Object value;
      try {
        value = this.resolver.getValue(fieldPath, type, genericType).orElse(null);
      } catch (final Exception exception) {
        throw new TransformException(String.format("Failed to use #getValue for %s", fieldPath), exception);
      }
      if (updateValue) {
        if (!this.resolver.isValid(fieldDeclaration, value)) {
          throw new TransformException(String.format("%s marked %s as invalid without throwing an exception",
            this.resolver.getClass(), fieldPath));
        }
        if (value != null) {
          fieldDeclaration.setValue(value);
        }
      }
      fieldDeclaration.setStartingValue(value);
    }
    return this;
  }

  /**
   * sets the declaration.
   *
   * @param declaration the declaration to set.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public final TransformedObject withDeclaration(@NotNull final TransformedObjectDeclaration declaration) {
    this.declaration = declaration;
    return this;
  }

  /**
   * sets the file.
   *
   * @param file the file to set.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public final TransformedObject withFile(@NotNull final File file) {
    this.file = file;
    return this;
  }

  /**
   * sets the file.
   *
   * @param path the path to set.
   *
   * @return {@code this} for builder chain.
   */
  public final TransformedObject withPath(@NotNull final String path) {
    return this.withPath(Path.of(path));
  }

  /**
   * sets the file.
   *
   * @param path the path to set.
   *
   * @return {@code this} for builder chain.
   */
  public final TransformedObject withPath(@NotNull final Path path) {
    return this.withFile(path.toFile());
  }

  /**
   * sets the resolver.
   *
   * @param resolver the resolver to set.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public final TransformedObject withResolver(@NotNull final TransformResolver resolver) {
    this.resolver = resolver;
    return this;
  }

  /**
   * register the transformer pack.
   *
   * @param pack the pack to register.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public final TransformedObject withTransformPack(@NotNull final TransformerPack pack) {
    Objects.requireNonNull(this.resolver, "resolver").withTransformerPacks(pack);
    return this;
  }

  /**
   * register the transformer pack.
   *
   * @param consumer the consumer to register.
   *
   * @return {@code this} for builder chain.
   */
  @NotNull
  public final TransformedObject withTransformPack(@NotNull final Consumer<@NotNull TransformRegistry> consumer) {
    return this.withTransformPack(TransformerPack.create(consumer));
  }
}
