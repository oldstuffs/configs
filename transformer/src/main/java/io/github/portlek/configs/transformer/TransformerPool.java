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

import io.github.portlek.configs.transformer.declarations.TransformedObjectDeclaration;
import io.github.portlek.configs.transformer.exceptions.TransformException;
import io.github.portlek.reflection.RefConstructed;
import io.github.portlek.reflection.RefField;
import io.github.portlek.reflection.clazz.ClassOf;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

/**
 * a class that represents transformer pools.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class TransformerPool {

  /**
   * creates a new transformed object.
   *
   * @param transformedObject the transformed object to create.
   * @param <T> type of the transformed object class.
   *
   * @return a newly created transformed object.
   */
  @NotNull
  public static <T extends TransformedObject> T create(@NotNull final T transformedObject) {
    transformedObject.withDeclaration(TransformedObjectDeclaration.of(transformedObject));
    return transformedObject;
  }

  /**
   * creates a new transformed object.
   *
   * @param cls the cls to create.
   * @param <T> type of the transformed object class.
   *
   * @return a newly created transformed object.
   */
  @NotNull
  public static <T extends TransformedObject> T create(@NotNull final Class<T> cls) {
    T transformedObject;
    try {
      transformedObject = new ClassOf<>(cls).getConstructor()
        .flatMap(RefConstructed::create)
        .orElseThrow(() ->
          new TransformException(String.format("Something went wrong when creating instance of %s", cls)));
    } catch (final Exception exception) {
      try {
        //noinspection unchecked
        transformedObject = (T) TransformerPool.allocateInstance(cls);
      } catch (final Exception exception1) {
        throw new TransformException(String.format("Failed to create %s instance, neither default constructor available, nor unsafe succeeded", cls));
      }
    }
    return TransformerPool.create(transformedObject);
  }

  /**
   * creates a new instance of the given class.
   *
   * @param cls the cls to create.
   *
   * @return a new instance of the class.
   *
   * @throws TransformException if something goes wrong when creating the instance.
   */
  @NotNull
  public static Object createInstance(@NotNull final Class<?> cls) throws TransformException {
    try {
      if (Collection.class.isAssignableFrom(cls)) {
        if (cls == Set.class) {
          return new HashSet<>();
        }
        if (cls == List.class) {
          return new ArrayList<>();
        }
        return new ClassOf<>(cls).getConstructor()
          .flatMap(RefConstructed::create)
          .orElseThrow();
      }
      if (Map.class.isAssignableFrom(cls)) {
        if (cls == Map.class) {
          return new LinkedHashMap<>();
        }
        return new ClassOf<>(cls).getConstructor()
          .flatMap(RefConstructed::create)
          .orElseThrow();
      }
      throw new TransformException(String.format("Cannot create instance of %s", cls));
    } catch (final Exception exception) {
      throw new TransformException(String.format("Failed to create instance of %s", cls), exception);
    }
  }

  /**
   * allocates an instance but does not run any constructor.
   * <p>
   * initializes the class if it has not yet been.
   *
   * @param cls the cls to allocate.
   *
   * @return allocated instance.
   */
  @NotNull
  private static Object allocateInstance(@NotNull final Class<?> cls) {
    final var unsafeClassOf = new ClassOf<>(Unsafe.class);
    return unsafeClassOf.getField("theUnsafe")
      .flatMap(RefField::getValue)
      .flatMap(object -> unsafeClassOf.getMethod("allocateInstance", Class.class)
        .map(method -> method.of(object)))
      .flatMap(refMethodExecuted -> refMethodExecuted.call(cls))
      .orElseThrow(() ->
        new TransformException(String.format("Something went wrong when allocating instance of %s", cls)));
  }
}
