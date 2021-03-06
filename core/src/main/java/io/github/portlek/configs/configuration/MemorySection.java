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

package io.github.portlek.configs.configuration;

import io.github.portlek.configs.util.NumberConversions;
import io.github.portlek.configs.util.Validate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type of {@link ConfigurationSection} that is stored in memory.
 *
 * @author Bukkit
 * @author Carlos Lazaro Costa (removed Bukkit-dependent accessors)
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/MemorySection.java">Bukkit
 *   Source</a>
 */
public class MemorySection implements ConfigurationSection {

  protected final Map<String, Object> map = new LinkedHashMap<>();

  private final String fullPath;

  private final ConfigurationSection parent;

  private final String path;

  private final Configuration root;

  /**
   * Creates an empty MemorySection for use as a root {@link Configuration}
   * section.
   *
   * @throws IllegalStateException Thrown if this is not a {@link
   *   Configuration} root.
   */
  protected MemorySection() {
    if (!(this instanceof Configuration)) {
      throw new IllegalStateException("Cannot construct a root MemorySection when not a Configuration");
    }
    this.path = "";
    this.fullPath = "";
    this.parent = null;
    this.root = (Configuration) this;
  }

  /**
   * Creates an empty MemorySection with the specified parent and path.
   *
   * @param parent Parent section that contains this own section.
   * @param path Path that you may access this section from via the root
   *   {@link Configuration}.
   *
   * @throws IllegalArgumentException Thrown is parent or path is null, or
   *   if parent contains no root Configuration.
   */
  protected MemorySection(@NotNull final ConfigurationSection parent, @NotNull final String path) {
    this.path = path;
    this.parent = parent;
    this.root = parent.getRoot();
    Validate.checkNull(this.root, "Path cannot be orphaned");
    this.fullPath = MemorySection.createPath(parent, path);
  }

  /**
   * Creates a full path to the given {@link ConfigurationSection} from its
   * root {@link Configuration}.
   *
   * @param section Section to create a path for.
   * @param key Name of the specified section.
   *
   * @return Full path of the section from its root.
   */
  public static String createPath(@NotNull final ConfigurationSection section, @NotNull final String key) {
    return MemorySection.createPath(section, key, section.getRoot());
  }

  /**
   * Creates a relative path to the given {@link ConfigurationSection} from
   * the given relative section.
   *
   * @param section Section to create a path for.
   * @param key Name of the specified section.
   * @param relativeTo Section to create the path relative to.
   *
   * @return Full path of the section from its root.
   */
  public static String createPath(@NotNull final ConfigurationSection section, final String key,
                                  final ConfigurationSection relativeTo) {
    final Configuration root = section.getRoot();
    if (root == null) {
      throw new IllegalStateException("Cannot create path without a root");
    }
    final char separator = root.options().pathSeparator();
    final StringBuilder builder = new StringBuilder();
    for (ConfigurationSection parent = section; parent != null && parent != relativeTo; parent = parent.getParent()) {
      if (builder.length() > 0) {
        builder.insert(0, separator);
      }
      builder.insert(0, parent.getName());
    }
    if (key != null && key.length() > 0) {
      if (builder.length() > 0) {
        builder.append(separator);
      }
      builder.append(key);
    }
    return builder.toString();
  }

  @Override
  public void addDefault(@NotNull final String path, final Object value) {
    final Configuration root = this.getRoot();
    if (root == null) {
      throw new IllegalStateException("Cannot add default without root");
    }
    if (root == this) {
      throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation");
    }
    root.addDefault(MemorySection.createPath(this, path), value);
  }

  @Override
  public boolean contains(@NotNull final String path) {
    return this.get(path) != null;
  }

  @Override
  public boolean contains(@NotNull final String path, final boolean ignoreDefault) {
    return (ignoreDefault ? this.get(path, null) : this.get(path)) != null;
  }

  @Override
  public ConfigurationSection createSection(@NotNull final String path) {
    Validate.checkEmpty(path, "Cannot create section at empty path");
    final Configuration root = this.getRoot();
    if (root == null) {
      throw new IllegalStateException("Cannot create section without a root");
    }
    final char separator = root.options().pathSeparator();
    // i1 is the leading (higher) index
    // i2 is the trailing (lower) index
    int i1 = -1, i2;
    ConfigurationSection section = this;
    while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
      final String node = path.substring(i2, i1);
      final ConfigurationSection subSection = section.getConfigurationSection(node);
      if (subSection == null) {
        section = section.createSection(node);
      } else {
        section = subSection;
      }
    }
    final String key = path.substring(i2);
    if (section == this) {
      final ConfigurationSection result = new MemorySection(this, key);
      this.map.put(key, result);
      return result;
    }
    return section.createSection(key);
  }

  @Override
  public ConfigurationSection createSection(@NotNull final String path, final Map<?, ?> map) {
    final ConfigurationSection section = this.createSection(path);
    for (final Map.Entry<?, ?> entry : map.entrySet()) {
      if (entry.getValue() instanceof Map) {
        section.createSection(entry.getKey().toString(), (Map<?, ?>) entry.getValue());
      } else {
        section.set(entry.getKey().toString(), entry.getValue());
      }
    }
    return section;
  }

  @Override
  public Object get(@NotNull final String path) {
    return this.get(path, this.getDefault(path));
  }

  @Nullable
  @Override
  public Object get(@NotNull final String path, @Nullable final Object def) {
    if (path.length() == 0) {
      return this;
    }
    final Configuration root = this.getRoot();
    if (root == null) {
      throw new IllegalStateException("Cannot access section without a root");
    }
    final char separator = root.options().pathSeparator();
    // i1 is the leading (higher) index
    // i2 is the trailing (lower) index
    int i1 = -1, i2;
    ConfigurationSection section = this;
    while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
      section = section.getConfigurationSection(path.substring(i2, i1));
      if (section == null) {
        return def;
      }
    }
    final String key = path.substring(i2);
    if (section == this) {
      final Object result = this.map.get(key);
      return result == null ? def : result;
    }
    return section.get(key, def);
  }

  @Override
  public boolean getBoolean(@NotNull final String path) {
    final Object def = this.getDefault(path);
    return this.getBoolean(path, def instanceof Boolean ? (Boolean) def : false);
  }

  @Override
  public boolean getBoolean(@NotNull final String path, final boolean def) {
    final Object val = this.get(path, def);
    return val instanceof Boolean ? (Boolean) val : def;
  }

  @Override
  public List<Boolean> getBooleanList(@NotNull final String path) {
    final List<?> list = this.getList(path);
    if (list == null) {
      return new ArrayList<>(0);
    }
    final List<Boolean> result = new ArrayList<>();
    for (final Object object : list) {
      if (object instanceof Boolean) {
        result.add((Boolean) object);
      } else if (object instanceof String) {
        if (Boolean.TRUE.toString().equals(object)) {
          result.add(true);
        } else if (Boolean.FALSE.toString().equals(object)) {
          result.add(false);
        }
      }
    }
    return result;
  }

  @Override
  public List<Byte> getByteList(@NotNull final String path) {
    final List<?> list = this.getList(path);
    if (list == null) {
      return new ArrayList<>(0);
    }
    final List<Byte> result = new ArrayList<>();
    for (final Object object : list) {
      if (object instanceof Byte) {
        result.add((Byte) object);
      } else if (object instanceof String) {
        try {
          result.add(Byte.valueOf((String) object));
        } catch (final Exception ex) {
        }
      } else if (object instanceof Character) {
        result.add((byte) ((Character) object).charValue());
      } else if (object instanceof Number) {
        result.add(((Number) object).byteValue());
      }
    }
    return result;
  }

  @Override
  public List<Character> getCharacterList(@NotNull final String path) {
    final List<?> list = this.getList(path);
    if (list == null) {
      return new ArrayList<>(0);
    }
    final List<Character> result = new ArrayList<>();
    for (final Object object : list) {
      if (object instanceof Character) {
        result.add((Character) object);
      } else if (object instanceof String) {
        final String str = (String) object;
        if (str.length() == 1) {
          result.add(str.charAt(0));
        }
      } else if (object instanceof Number) {
        result.add((char) ((Number) object).intValue());
      }
    }
    return result;
  }

  @Override
  public ConfigurationSection getConfigurationSection(@NotNull final String path) {
    Object val = this.get(path, null);
    if (val != null) {
      return val instanceof ConfigurationSection ? (ConfigurationSection) val : null;
    }
    val = this.get(path, this.getDefault(path));
    return val instanceof ConfigurationSection ? this.createSection(path) : null;
  }

  @Override
  public String getCurrentPath() {
    return this.fullPath;
  }

  @Override
  public ConfigurationSection getDefaultSection() {
    final Configuration root = this.getRoot();
    final Configuration defaults = root == null ? null : root.getDefaults();
    if (defaults != null) {
      if (defaults.isConfigurationSection(this.getCurrentPath())) {
        return defaults.getConfigurationSection(this.getCurrentPath());
      }
    }
    return null;
  }

  @Override
  public double getDouble(@NotNull final String path) {
    final Object def = this.getDefault(path);
    return this.getDouble(path, def instanceof Number ? NumberConversions.toDouble(def) : 0);
  }

  @Override
  public double getDouble(@NotNull final String path, final double def) {
    final Object val = this.get(path, def);
    return val instanceof Number ? NumberConversions.toDouble(val) : def;
  }

  @Override
  public List<Double> getDoubleList(@NotNull final String path) {
    final List<?> list = this.getList(path);
    if (list == null) {
      return new ArrayList<>(0);
    }
    final List<Double> result = new ArrayList<>();
    for (final Object object : list) {
      if (object instanceof Double) {
        result.add((Double) object);
      } else if (object instanceof String) {
        try {
          result.add(Double.valueOf((String) object));
        } catch (final Exception ex) {
          // ignored.
        }
      } else if (object instanceof Character) {
        result.add((double) (Character) object);
      } else if (object instanceof Number) {
        result.add(((Number) object).doubleValue());
      }
    }
    return result;
  }

  @Override
  public List<Float> getFloatList(@NotNull final String path) {
    final List<?> list = this.getList(path);
    if (list == null) {
      return new ArrayList<>(0);
    }
    final List<Float> result = new ArrayList<>();
    for (final Object object : list) {
      if (object instanceof Float) {
        result.add((Float) object);
      } else if (object instanceof String) {
        try {
          result.add(Float.valueOf((String) object));
        } catch (final Exception ex) {
          // ignored.
        }
      } else if (object instanceof Character) {
        result.add((float) (Character) object);
      } else if (object instanceof Number) {
        result.add(((Number) object).floatValue());
      }
    }
    return result;
  }

  @Override
  public int getInt(@NotNull final String path) {
    final Object def = this.getDefault(path);
    return this.getInt(path, def instanceof Number ? NumberConversions.toInt(def) : 0);
  }

  @Override
  public int getInt(@NotNull final String path, final int def) {
    final Object val = this.get(path, def);
    return val instanceof Number ? NumberConversions.toInt(val) : def;
  }

  @Override
  public List<Integer> getIntegerList(@NotNull final String path) {
    final List<?> list = this.getList(path);
    if (list == null) {
      return new ArrayList<>(0);
    }
    final List<Integer> result = new ArrayList<>();
    for (final Object object : list) {
      if (object instanceof Integer) {
        result.add((Integer) object);
      } else if (object instanceof String) {
        try {
          result.add(Integer.valueOf((String) object));
        } catch (final Exception ex) {
          // ignored.
        }
      } else if (object instanceof Character) {
        result.add((int) (Character) object);
      } else if (object instanceof Number) {
        result.add(((Number) object).intValue());
      }
    }
    return result;
  }

  @Override
  public Set<String> getKeys(final boolean deep) {
    final Set<String> result = new LinkedHashSet<>();
    final Configuration root = this.getRoot();
    if (root != null && root.options().copyDefaults()) {
      final ConfigurationSection defaults = this.getDefaultSection();
      if (defaults != null) {
        result.addAll(defaults.getKeys(deep));
      }
    }
    this.mapChildrenKeys(result, this, deep);
    return result;
  }

  @Override
  public List<?> getList(@NotNull final String path) {
    final Object def = this.getDefault(path);
    return this.getList(path, def instanceof List ? (List<?>) def : null);
  }

  @Override
  public List<?> getList(@NotNull final String path, final List<?> def) {
    final Object val = this.get(path, def);
    return (List<?>) (val instanceof List ? val : def);
  }

  @Override
  public long getLong(@NotNull final String path) {
    final Object def = this.getDefault(path);
    return this.getLong(path, def instanceof Number ? NumberConversions.toLong(def) : 0);
  }

  @Override
  public long getLong(final String path, final long def) {
    final Object val = this.get(path, def);
    return val instanceof Number ? NumberConversions.toLong(val) : def;
  }

  @NotNull
  @Override
  public List<Long> getLongList(@NotNull final String path) {
    final List<?> list = this.getList(path);
    if (list == null) {
      return new ArrayList<>(0);
    }
    final List<Long> result = new ArrayList<>();
    for (final Object object : list) {
      if (object instanceof Long) {
        result.add((Long) object);
      } else if (object instanceof String) {
        try {
          result.add(Long.valueOf((String) object));
        } catch (final Exception ex) {
          // ignored.
        }
      } else if (object instanceof Character) {
        result.add((long) (Character) object);
      } else if (object instanceof Number) {
        result.add(((Number) object).longValue());
      }
    }
    return result;
  }

  @Override
  public List<Map<?, ?>> getMapList(@NotNull final String path) {
    final List<?> list = this.getList(path);
    final List<Map<?, ?>> result = new ArrayList<>();
    if (list == null) {
      return result;
    }
    for (final Object object : list) {
      if (object instanceof Map) {
        result.add((Map<?, ?>) object);
      }
    }
    return result;
  }

  @Override
  public String getName() {
    return this.path;
  }

  @Nullable
  @Override
  public <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz) {
    final var def = this.getDefault(path);
    return this.getObject(path, clazz, clazz.isInstance(def) ? clazz.cast(def) : null);
  }

  @Nullable
  @Override
  public <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def) {
    final var val = this.get(path, def);
    return clazz.isInstance(val) ? clazz.cast(val) : def;
  }

  @Override
  public ConfigurationSection getParent() {
    return this.parent;
  }

  @Override
  public Configuration getRoot() {
    return this.root;
  }

  @Override
  public List<Short> getShortList(@NotNull final String path) {
    final List<?> list = this.getList(path);
    if (list == null) {
      return new ArrayList<>(0);
    }
    final List<Short> result = new ArrayList<>();
    for (final Object object : list) {
      if (object instanceof Short) {
        result.add((Short) object);
      } else if (object instanceof String) {
        try {
          result.add(Short.valueOf((String) object));
        } catch (final Exception ex) {
          // ignored.
        }
      } else if (object instanceof Character) {
        result.add((short) ((Character) object).charValue());
      } else if (object instanceof Number) {
        result.add(((Number) object).shortValue());
      }
    }
    return result;
  }

  @Override
  public String getString(@NotNull final String path) {
    final Object def = this.getDefault(path);
    return this.getString(path, def != null ? def.toString() : null);
  }

  @Override
  public String getString(@NotNull final String path, final String def) {
    final Object val = this.get(path, def);
    return val != null ? val.toString() : def;
  }

  @Override
  public List<String> getStringList(@NotNull final String path) {
    final List<?> list = this.getList(path);
    if (list == null) {
      return new ArrayList<>(0);
    }
    return list.stream()
      .filter(object -> object instanceof String || this.isPrimitiveWrapper(object))
      .map(String::valueOf)
      .collect(Collectors.toList());
  }

  @Override
  public Map<String, Object> getValues(final boolean deep) {
    final Map<String, Object> result = new LinkedHashMap<>();
    final Configuration root = this.getRoot();
    if (root != null && root.options().copyDefaults()) {
      final ConfigurationSection defaults = this.getDefaultSection();
      if (defaults != null) {
        result.putAll(defaults.getValues(deep));
      }
    }
    this.mapChildrenValues(result, this, deep);
    return result;
  }

  @Override
  public boolean isBoolean(@NotNull final String path) {
    final Object val = this.get(path);
    return val instanceof Boolean;
  }

  @Override
  public boolean isConfigurationSection(@NotNull final String path) {
    final Object val = this.get(path);
    return val instanceof ConfigurationSection;
  }

  @Override
  public boolean isDouble(@NotNull final String path) {
    final Object val = this.get(path);
    return val instanceof Double;
  }

  @Override
  public boolean isInt(@NotNull final String path) {
    final Object val = this.get(path);
    return val instanceof Integer;
  }

  @Override
  public boolean isList(@NotNull final String path) {
    final Object val = this.get(path);
    return val instanceof List;
  }

  @Override
  public boolean isLong(@NotNull final String path) {
    final Object val = this.get(path);
    return val instanceof Long;
  }

  @Override
  public boolean isSet(@NotNull final String path) {
    final Configuration root = this.getRoot();
    if (root == null) {
      return false;
    }
    if (root.options().copyDefaults()) {
      return this.contains(path);
    }
    return this.get(path, null) != null;
  }

  @Override
  public boolean isString(@NotNull final String path) {
    final Object val = this.get(path);
    return val instanceof String;
  }

  @Override
  public void set(@NotNull final String path, final Object value) {
    Validate.checkEmpty(path, "Cannot set to an empty path");
    final Configuration root = this.getRoot();
    if (root == null) {
      throw new IllegalStateException("Cannot use section without a root");
    }
    final char separator = root.options().pathSeparator();
    // i1 is the leading (higher) index
    // i2 is the trailing (lower) index
    int i1 = -1, i2;
    ConfigurationSection section = this;
    while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
      final String node = path.substring(i2, i1);
      final ConfigurationSection subSection = section.getConfigurationSection(node);
      if (subSection == null) {
        section = section.createSection(node);
      } else {
        section = subSection;
      }
    }
    final String key = path.substring(i2);
    if (section == this) {
      if (value == null) {
        this.map.remove(key);
      } else {
        this.map.put(key, value);
      }
    } else {
      section.set(key, value);
    }
  }

  @Override
  public String toString() {
    final Configuration root = this.getRoot();
    return new StringBuilder()
      .append(this.getClass().getSimpleName())
      .append("[path='")
      .append(this.getCurrentPath())
      .append("', root='")
      .append(root == null ? null : root.getClass().getSimpleName())
      .append("']")
      .toString();
  }

  @Nullable
  protected Object getDefault(@NotNull final String path) {
    final Configuration root = this.getRoot();
    final Configuration defaults = root == null ? null : root.getDefaults();
    return defaults == null ? null : defaults.get(MemorySection.createPath(this, path));
  }

  protected boolean isPrimitiveWrapper(final Object input) {
    return input instanceof Integer || input instanceof Boolean ||
      input instanceof Character || input instanceof Byte ||
      input instanceof Short || input instanceof Double ||
      input instanceof Long || input instanceof Float;
  }

  protected void mapChildrenKeys(final Set<String> output, final ConfigurationSection section, final boolean deep) {
    if (section instanceof MemorySection) {
      final MemorySection sec = (MemorySection) section;
      sec.map.forEach((key, value) -> {
        output.add(MemorySection.createPath(section, key, this));
        if (deep && value instanceof ConfigurationSection) {
          final ConfigurationSection subsection = (ConfigurationSection) value;
          this.mapChildrenKeys(output, subsection, true);
        }
      });
    } else {
      final Set<String> keys = section.getKeys(deep);
      keys.stream()
        .map(key -> MemorySection.createPath(section, key, this))
        .forEach(output::add);
    }
  }

  protected void mapChildrenValues(final Map<String, Object> output, final ConfigurationSection section,
                                   final boolean deep) {
    if (section instanceof MemorySection) {
      final MemorySection sec = (MemorySection) section;
      sec.map.forEach((key, value) -> {
        output.put(MemorySection.createPath(section, key, this), value);
        if (value instanceof ConfigurationSection) {
          if (deep) {
            this.mapChildrenValues(output, (ConfigurationSection) value, deep);
          }
        }
      });
    } else {
      final Map<String, Object> values = section.getValues(deep);
      values.forEach((key, value) ->
        output.put(MemorySection.createPath(section, key, this), value));
    }
  }
}
