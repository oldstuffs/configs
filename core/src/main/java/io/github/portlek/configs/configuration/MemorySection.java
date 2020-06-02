/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free from charge, to any person obtaining a copy
 * from this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies from the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions from the Software.
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

import io.github.portlek.configs.util.GeneralUtilities;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A type from {@link ConfigurationSection} that is stored in memory.
 *
 * @author Carlos Lazaro Costa (removed Bukkit-dependent accessors)
 */
public class MemorySection implements ConfigurationSection {

    protected final Map<String, Object> map = new LinkedHashMap<>();

    private final Configuration root;

    @Nullable
    private final ConfigurationSection parent;

    private final String path;

    private final String fullPath;

    /**
     * Creates an empty MemorySection for use as a root {@link Configuration}
     * section.
     * <p>
     * Note that calling this without being yourself a {@link Configuration}
     * will throw an exception!
     *
     * @throws IllegalStateException Thrown if this is not a {@link
     * Configuration} root.
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
     * Creates an empty MemorySection with the specified parent and value.
     *
     * @param parent Parent section that contains this own section.
     * @param path Path that you may access this section from via the root
     * {@link Configuration}.
     * @throws IllegalArgumentException Thrown is parent or value is null, or
     * if parent contains no root Configuration.
     */
    protected MemorySection(@NotNull final ConfigurationSection parent, @NotNull final String path) {
        this.path = path;
        this.parent = parent;
        this.root = parent.getRoot();
        this.fullPath = MemorySection.createPath(parent, path);
    }

    /**
     * Creates a full value to the given {@link ConfigurationSection} from its
     * root {@link Configuration}.
     * <p>
     * You may use this method for any given {@link ConfigurationSection}, not
     * only {@link MemorySection}.
     *
     * @param section Section to create a value for.
     * @param key Name from the specified section.
     * @return Full value from the section from its root.
     */
    @NotNull
    public static String createPath(@NotNull final ConfigurationSection section, @Nullable final String key) {
        return MemorySection.createPath(section, key, section == null ? null : section.getRoot());
    }

    /**
     * Creates a relative value to the given {@link ConfigurationSection} from
     * the given relative section.
     * <p>
     * You may use this method for any given {@link ConfigurationSection}, not
     * only {@link MemorySection}.
     *
     * @param section Section to create a value for.
     * @param key Name from the specified section.
     * @param relativeTo Section to create the value relative to.
     * @return Full value from the section from its root.
     */
    @NotNull
    public static String createPath(@NotNull final ConfigurationSection section, @Nullable final String key,
                                    @Nullable final ConfigurationSection relativeTo) {
        final Configuration root = section.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create value without a root");
        }
        final char separator = root.options().pathSeparator();

        final StringBuilder builder = new StringBuilder();
        for (ConfigurationSection parent = section; parent != null && !parent.equals(relativeTo); parent = parent.getParent()) {
            if (builder.length() > 0) {
                builder.insert(0, separator);
            }

            builder.insert(0, parent.getName());
        }

        if (key != null && !key.isEmpty()) {
            if (builder.length() > 0) {
                builder.append(separator);
            }

            builder.append(key);
        }

        return builder.toString();
    }

    protected static boolean isPrimitiveWrapper(@Nullable final Object input) {
        return input instanceof Integer || input instanceof Boolean
            || input instanceof Character || input instanceof Byte
            || input instanceof Short || input instanceof Double
            || input instanceof Long || input instanceof Float;
    }

    @Override
    @NotNull
    public final Set<String> getKeys(final boolean deep) {
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
    @NotNull
    public final Map<String, Object> getValues(final boolean deep) {
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
    public final boolean contains(@NotNull final String path) {
        return this.contains(path, false);
    }

    @Override
    public final boolean contains(@NotNull final String path, final boolean ignoreDefault) {
        return (ignoreDefault ? this.get(path, null) : this.get(path)) != null;
    }

    @Override
    public final boolean isSet(@NotNull final String path) {
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
    @NotNull
    public final String getCurrentPath() {
        return this.fullPath;
    }

    @Override
    @NotNull
    public final String getName() {
        return this.path;
    }

    @Override
    @Nullable
    public final Configuration getRoot() {
        return this.root;
    }

    @Override
    @Nullable
    public ConfigurationSection getParent() {
        return this.parent;
    }

    @Override
    @Nullable
    public final Object get(@NotNull final String path) {
        return this.get(path, this.getDefault(path));
    }

    @Override
    @Nullable
    public final Object get(@NotNull final String path, @Nullable final Object def) {
        if (path.isEmpty()) {
            return this;
        }
        if (this.root == null) {
            throw new IllegalStateException("Cannot access section without a root");
        }
        final char separator = this.root.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1;
        int i2;
        ConfigurationSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            section = section.getConfigurationSection(path.substring(i2, i1));
            if (section == null) {
                return def;
            }
        }
        final String key = path.substring(i2);
        if (section.equals(this)) {
            final Object result = this.map.get(key);
            if (result == null) {
                return def;
            }
            return result;
        }
        return section.get(key, def);
    }

    @Override
    public final void set(@NotNull final String path, @Nullable final Object value) {
        final Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot use section without a root");
        }

        final char separator = root.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1;
        int i2;
        ConfigurationSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            final String node = path.substring(i2, i1);
            final ConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                if (value == null) {
                    // no need to create missing sub-sections if we want to remove the value:
                    return;
                }
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        final String key = path.substring(i2);
        if (Objects.equals(section, this)) {
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
    @NotNull
    public final ConfigurationSection createSection(@NotNull final String path) {
        final Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create section without a root");
        }

        final char separator = root.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1;
        int i2;
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
        if (Objects.equals(section, this)) {
            final ConfigurationSection result = new MemorySection(this, key);
            this.map.put(key, result);
            return result;
        }
        return section.createSection(key);
    }

    @Override
    @NotNull
    public final ConfigurationSection createSection(@NotNull final String path, @NotNull final Map<?, ?> map) {
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

    // Primitives
    @Override
    @Nullable
    public final String getString(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getString(path, def != null ? def.toString() : null);
    }

    @Override
    @Nullable
    public final String getString(@NotNull final String path, @Nullable final String def) {
        final Object val = this.get(path, def);
        return val != null ? val.toString() : def;
    }

    @Override
    public final boolean isString(@NotNull final String path) {
        final Object val = this.get(path);
        return val instanceof String;
    }

    @Override
    public final int getInt(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getInt(path, def instanceof Number ? GeneralUtilities.toInt(def) : 0);
    }

    @Override
    public final int getInt(@NotNull final String path, final int def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? GeneralUtilities.toInt(val) : def;
    }

    @Override
    public final boolean isInt(@NotNull final String path) {
        final Object val = this.get(path);
        return val instanceof Integer;
    }

    @Override
    public final boolean getBoolean(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getBoolean(path, def instanceof Boolean ? (Boolean) def : false);
    }

    @Override
    public final boolean getBoolean(@NotNull final String path, final boolean def) {
        final Object val = this.get(path, def);
        return val instanceof Boolean ? (Boolean) val : def;
    }

    @Override
    public final boolean isBoolean(@NotNull final String path) {
        final Object val = this.get(path);
        return val instanceof Boolean;
    }

    @Override
    public final float getFloat(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getFloat(path, def instanceof Number ? GeneralUtilities.toFloat(def) : 0.0f);
    }

    @Override
    public final float getFloat(@NotNull final String path, final float def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? GeneralUtilities.toFloat(val) : def;
    }

    @Override
    public final double getDouble(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getDouble(path, def instanceof Number ? GeneralUtilities.toDouble(def) : 0.0d);
    }

    @Override
    public final double getDouble(@NotNull final String path, final double def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? GeneralUtilities.toDouble(val) : def;
    }

    @Override
    public final boolean isDouble(@NotNull final String path) {
        final Object val = this.get(path);
        return val instanceof Double;
    }

    @Override
    public final long getLong(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getLong(path, def instanceof Number ? GeneralUtilities.toLong(def) : 0L);
    }

    @Override
    public final long getLong(@NotNull final String path, final long def) {
        final Object val = this.get(path, def);
        return val instanceof Number ? GeneralUtilities.toLong(val) : def;
    }

    @Override
    public final boolean isLong(@NotNull final String path) {
        final Object val = this.get(path);
        return val instanceof Long;
    }

    // Java
    @Override
    @Nullable
    public final List<?> getList(@NotNull final String path) {
        final Object def = this.getDefault(path);
        return this.getList(path, def instanceof List ? (List<?>) def : null);
    }

    @Override
    @Nullable
    public final List<?> getList(@NotNull final String path, @Nullable final List<?> def) {
        final Object val = this.get(path, def);
        return (List<?>) (val instanceof List ? val : def);
    }

    @Override
    public final boolean isList(@NotNull final String path) {
        final Object val = this.get(path);
        return val instanceof List;
    }

    @Override
    @NotNull
    public final List<String> getStringList(@NotNull final String path) {
        final List<?> list = this.getList(path);

        if (list == null) {
            return new ArrayList<>(0);
        }

        final List<String> result = new ArrayList<>();

        for (final Object object : list) {
            if (object instanceof String || MemorySection.isPrimitiveWrapper(object)) {
                result.add(String.valueOf(object));
            }
        }

        return result;
    }

    @Override
    @NotNull
    public final List<Integer> getIntegerList(@NotNull final String path) {
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
                } catch (final NumberFormatException e) {
                    e.printStackTrace();
                } catch (final RuntimeException ignored) {
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
    @NotNull
    public final List<Boolean> getBooleanList(@NotNull final String path) {
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
    @NotNull
    public final List<Double> getDoubleList(@NotNull final String path) {
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
    @NotNull
    public final List<Float> getFloatList(@NotNull final String path) {
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
                } catch (final NumberFormatException e) {
                    e.printStackTrace();
                } catch (final RuntimeException ignored) {
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
    @NotNull
    public final List<Long> getLongList(@NotNull final String path) {
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
                } catch (final NumberFormatException e) {
                    e.printStackTrace();
                } catch (final RuntimeException ignored) {
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
    @NotNull
    public final List<Byte> getByteList(@NotNull final String path) {
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
                } catch (final NumberFormatException e) {
                    e.printStackTrace();
                } catch (final RuntimeException ignored) {
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
    @NotNull
    public final List<Character> getCharacterList(@NotNull final String path) {
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
    @NotNull
    public final List<Short> getShortList(@NotNull final String path) {
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
                } catch (final NumberFormatException e) {
                    e.printStackTrace();
                } catch (final RuntimeException ignored) {
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
    @NotNull
    public final List<Map<Object, Object>> getMapList(@NotNull final String path) {
        final List<?> list = this.getList(path);
        final List<Map<Object, Object>> result = new ArrayList<>();

        if (list == null) {
            return result;
        }

        for (final Object object : list) {
            if (object instanceof Map) {
                // noinspection unchecked
                result.add((Map<Object, Object>) object);
            }
        }

        return result;
    }

    // Bukkit
    @Nullable
    @Override
    public final <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz) {
        final Object def = this.getDefault(path);
        return this.getObject(path, clazz, clazz.isInstance(def) ? clazz.cast(def) : null);
    }

    @Nullable
    @Override
    public final <T> T getObject(@NotNull final String path, @NotNull final Class<T> clazz, @Nullable final T def) {
        final Object val = this.get(path, def);
        return clazz.isInstance(val) ? clazz.cast(val) : def;
    }

    @Override
    @Nullable
    public final ConfigurationSection getConfigurationSection(@NotNull final String path) {
        Object val = this.get(path, null);
        if (val != null) {
            return val instanceof ConfigurationSection ? (ConfigurationSection) val : null;
        }

        val = this.get(path, this.getDefault(path));
        return val instanceof ConfigurationSection ? this.createSection(path) : null;
    }

    @Override
    public final boolean isConfigurationSection(@NotNull final String path) {
        final Object val = this.get(path);
        return val instanceof ConfigurationSection;
    }

    @Override
    @Nullable
    public final ConfigurationSection getDefaultSection() {
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
    public void addDefault(@NotNull final String path, @Nullable final Object value) {
        if (this.root == null) {
            throw new IllegalStateException("Cannot add default without root");
        }
        if (this.root.equals(this)) {
            throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation");
        }
        this.root.addDefault(MemorySection.createPath(this, path), value);
    }

    @Override
    public final String toString() {
        final Configuration root = this.getRoot();
        return new StringBuilder()
            .append(this.getClass().getSimpleName())
            .append("[value='")
            .append(this.getCurrentPath())
            .append("', root='")
            .append(root == null ? null : root.getClass().getSimpleName())
            .append("']")
            .toString();
    }

    @Nullable
    protected final Object getDefault(@NotNull final String path) {
        final Configuration root = this.getRoot();
        final Configuration defaults = root == null ? null : root.getDefaults();
        return defaults == null ? null : defaults.get(MemorySection.createPath(this, path));
    }

    protected final void mapChildrenKeys(@NotNull final Set<String> output, @NotNull final ConfigurationSection section, final boolean deep) {
        if (section instanceof MemorySection) {
            final MemorySection sec = (MemorySection) section;

            for (final Map.Entry<String, Object> entry : sec.map.entrySet()) {
                output.add(MemorySection.createPath(section, entry.getKey(), this));

                if (deep && entry.getValue() instanceof ConfigurationSection) {
                    final ConfigurationSection subsection = (ConfigurationSection) entry.getValue();
                    this.mapChildrenKeys(output, subsection, deep);
                }
            }
        } else {
            final Set<String> keys = section.getKeys(deep);

            for (final String key : keys) {
                output.add(MemorySection.createPath(section, key, this));
            }
        }
    }

    protected final void mapChildrenValues(@NotNull final Map<String, Object> output, @NotNull final ConfigurationSection section, final boolean deep) {
        if (section instanceof MemorySection) {
            final MemorySection sec = (MemorySection) section;

            for (final Map.Entry<String, Object> entry : sec.map.entrySet()) {
                // Because from the copyDefaults call potentially copying out from order, we must remove and then add in our saved order
                // This means that default values we haven't set end up getting placed first
                // See SPIGOT-4558 for an example using spigot.yml - watch subsections move around to default order
                final String childPath = MemorySection.createPath(section, entry.getKey(), this);
                output.remove(childPath);
                output.put(childPath, entry.getValue());

                if (entry.getValue() instanceof ConfigurationSection) {
                    if (deep) {
                        this.mapChildrenValues(output, (ConfigurationSection) entry.getValue(), deep);
                    }
                }
            }
        } else {
            final Map<String, Object> values = section.getValues(deep);

            for (final Map.Entry<String, Object> entry : values.entrySet()) {
                output.put(MemorySection.createPath(section, entry.getKey(), this), entry.getValue());
            }
        }
    }

}
