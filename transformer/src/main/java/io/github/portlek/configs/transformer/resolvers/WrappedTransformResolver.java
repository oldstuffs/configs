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

package io.github.portlek.configs.transformer.resolvers;

import io.github.portlek.configs.transformer.TransformResolver;
import io.github.portlek.configs.transformer.declarations.FieldDeclaration;
import io.github.portlek.configs.transformer.declarations.GenericDeclaration;
import io.github.portlek.configs.transformer.declarations.TransformedObjectDeclaration;
import io.github.portlek.configs.transformer.exceptions.TransformException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an abstract class that represents wrapped transform resolvers.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class WrappedTransformResolver extends TransformResolver {

  /**
   * the delegate.
   */
  @NotNull
  private final TransformResolver delegate;

  @Nullable
  @Override
  public <T> T deserialize(@Nullable final Object object, @Nullable final GenericDeclaration genericSource,
                           @NotNull final Class<T> targetClass, @Nullable final GenericDeclaration genericTarget)
    throws TransformException {
    return this.delegate.deserialize(object, genericSource, targetClass, genericTarget);
  }

  @Override
  public List<String> getAllKeys() {
    return this.delegate.getAllKeys();
  }

  @NotNull
  @Override
  public Optional<Object> getValue(@NotNull final String path) {
    return this.delegate.getValue(path);
  }

  @NotNull
  @Override
  public <T> Optional<T> getValue(@NotNull final String path, @NotNull final Class<T> cls,
                                  @Nullable final GenericDeclaration genericType) {
    return this.delegate.getValue(path, cls, genericType);
  }

  @Override
  public boolean isToStringObject(@NotNull final Object object, @Nullable final GenericDeclaration declaration) {
    return this.delegate.isToStringObject(object, declaration);
  }

  @Override
  public boolean isValid(@NotNull final FieldDeclaration declaration, @Nullable final Object value) {
    return this.delegate.isValid(declaration, value);
  }

  @Override
  public void load(@NotNull final InputStream inputStream, @NotNull final TransformedObjectDeclaration declaration)
    throws Exception {
    this.delegate.load(inputStream, declaration);
  }

  @Override
  public boolean pathExists(@NotNull final String path) {
    return this.delegate.pathExists(path);
  }

  @Nullable
  @Override
  public Object serialize(@Nullable final Object value, @Nullable final GenericDeclaration genericType,
                          final boolean conservative) throws TransformException {
    return this.delegate.serialize(value, genericType, conservative);
  }

  @Override
  public void setValue(@NotNull final String path, @Nullable final Object value,
                       @Nullable final GenericDeclaration genericType, @Nullable final FieldDeclaration field) {
    this.delegate.setValue(path, value, genericType, field);
  }

  @Override
  public List<?> serializeCollection(@NotNull final Collection<?> value, @Nullable final GenericDeclaration genericType,
                                     final boolean conservative) throws TransformException {
    return this.delegate.serializeCollection(value, genericType, conservative);
  }

  @NotNull
  @Override
  public Map<Object, Object> serializeMap(@NotNull final Map<Object, Object> value,
                                          @Nullable final GenericDeclaration genericType, final boolean conservative)
    throws TransformException {
    return this.delegate.serializeMap(value, genericType, conservative);
  }

  @Override
  public void write(@NotNull final OutputStream outputStream, @NotNull final TransformedObjectDeclaration declaration)
    throws Exception {
    this.delegate.write(outputStream, declaration);
  }
}
