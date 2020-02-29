package io.github.portlek.configs.configuration;

import com.eclipsesource.json.JsonObject;
import io.github.portlek.configs.util.JsonUtils;
import java.util.*;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MemorySection implements ConfigurationSection {

    @NotNull
    private final Map<String, Object> map;

    public MemorySection(@NotNull final Map<String, Object> copy, @NotNull final DataType type) {
        this.map = type.getMap();
        this.map.putAll(copy);
    }

    @Nullable
    private static Object get(@NotNull final Map<String, Object> temp, @NotNull final String[] key,
                              final int index) {
        @Nullable final Object object;
        if (index < key.length - 1) {
            if (temp.get(key[index]) instanceof Map<?, ?>) {
                //noinspection unchecked
                object = MemorySection.get((Map<String, Object>) temp.get(key[index]), key, index + 1);
            } else {
                object = null;
            }
        } else {
            object = temp.get(key[index]);
        }
        return object;
    }

    @NotNull
    private static Object insert(@NotNull final Map<String, Object> temp, @NotNull final String[] key,
                                 @NotNull final Object value, final int index) {
        final Object object;
        if (index < key.length) {
            final Map<String, Object> tmp = new HashMap<>(temp);
            final Map<String, Object> child;
            if (temp.containsKey(key[index]) && temp.get(key[index]) instanceof Map) {
                //noinspection unchecked
                child = (Map<String, Object>) temp.get(key[index]);
            } else {
                child = new HashMap<>();
            }
            tmp.put(key[index], MemorySection.insert(child, key, value, index + 1));
            object = tmp;
        } else {
            object = value;
        }
        return object;
    }

    private static boolean containsKey(@NotNull final Map<String, Object> temp, @NotNull final String[] key,
                                       final int index) {
        final boolean contains;
        if (index < key.length - 1) {
            if (temp.containsKey(key[index]) && temp.get(key[index]) instanceof Map) {
                //noinspection unchecked
                contains = MemorySection.containsKey((Map<String, Object>) temp.get(key[index]), key, index + 1);
            } else {
                contains = false;
            }
        } else {
            contains = temp.containsKey(key[index]);
        }
        return contains;
    }

    @NotNull
    private static Map<String, Object> remove(@NotNull final Map<String, Object> temp,
                                              @NotNull final String[] key,
                                              final int index) {
        if (index < key.length - 1) {
            final Object value = temp.get(key[index]);
            if (value instanceof Map) {
                //noinspection unchecked
                temp.put(key[index], MemorySection.remove((Map<String, Object>) value, key, index + 1));
                if (((Map<?, ?>) temp.get(key[index])).isEmpty()) {
                    temp.remove(key[index]);
                }
            }
        } else {
            temp.remove(key[index]);
        }
        return temp;
    }

    @NotNull
    private static Set<String> keySet(final Map<String, Object> temp) {
        return temp.entrySet().stream()
            .map(entry -> {
                final String key = entry.getKey();
                if (entry.getValue() instanceof Map) {
                    //noinspection unchecked
                    for (final String tmp : MemorySection.keySet((Map<String, Object>) entry.getValue())) {
                        return key + '.' + tmp;
                    }
                }
                return key;
            }).collect(Collectors.toSet());
    }

    private static int size(@NotNull final Map<String, Object> temp) {
        int size = temp.size();
        for (final Object object : temp.values()) {
            if (object instanceof Map) {
                //noinspection unchecked
                size += MemorySection.size((Map<String, Object>) object);
            }
        }
        return size;
    }

    public final int singleLayerSize() {
        return this.map.size();
    }

    public final int singleLayerSize(@NotNull final String key) {
        final int size;
        final Object object = this.get(key);
        if (object instanceof Map) {
            size = ((Map<?, ?>) object).size();
        } else {
            size = 0;
        }
        return size;
    }

    public final int size() {
        return this.map.size();
    }

    public final int size(@NotNull final String key) {
        return this.map.size();
    }

    public final void load(@NotNull final Map<String, Object> load) {
        this.clear();
        this.map.putAll(load);
    }

    public final void clear() {
        this.map.clear();
    }

    @NotNull
    @Override
    public final Set<String> singleLayerKeySet() {
        return this.map.keySet();
    }

    @NotNull
    @Override
    public final Set<String> singleLayerKeySet(final String key) {
        final Object object = this.get(key);
        final Set<String> set;
        if (object instanceof Map) {
            //noinspection unchecked
            set = ((Map<String, Object>) object).keySet();
        } else {
            set = new HashSet<>();
        }
        return set;
    }

    @NotNull
    @Override
    public final Set<String> keySet() {
        return MemorySection.keySet(this.map);
    }

    @NotNull
    @Override
    public final Set<String> keySet(final String key) {
        final Set<String> set;
        final Object object = this.get(key);
        if (object instanceof Map) {
            //noinspection unchecked
            set = MemorySection.keySet((Map<String, Object>) object);
        } else {
            set = new HashSet<>();
        }
        return set;
    }

    @Override
    public final synchronized void remove(@NotNull final String key) {
        if (this.contains(key)) {
            final String[] parts = key.split("\\.");
            this.remove(parts);
        }
    }

    @Override
    public final boolean contains(final String key) {
        final String[] parts = key.split("\\.");
        return MemorySection.containsKey(this.map, parts, 0);
    }

    @Override
    @Nullable
    public final Object get(@NotNull final String path) {
        final String[] parts = path.split("\\.");
        return MemorySection.get(this.map, parts, 0);
    }

    @Override
    public final synchronized void set(@NotNull final String key, @NotNull final Object value) {
        final String[] parts = key.split("\\.");
        if (this.map.containsKey(parts[0]) && this.map.get(parts[0]) instanceof Map) {
            //noinspection unchecked
            this.map.put(parts[0],
                MemorySection.insert((Map<String, Object>) this.map.get(parts[0]), parts, value, 1));
        } else {
            this.map.put(parts[0],
                MemorySection.insert(new HashMap<>(), parts, value, 1));
        }
    }

    public final void putAll(final Map<String, Object> temp) {
        this.map.putAll(temp);
    }

    @NotNull
    public final JsonObject toJsonObject() {
        return JsonUtils.mapAsJsonObject(this.map);
    }

    @Override
    public final int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        final boolean equals;
        if (obj == this) {
            equals = true;
        } else if (obj == null || !this.getClass().equals(obj.getClass())) {
            equals = false;
        } else {
            final MemorySection section = (MemorySection) obj;
            equals = this.map.equals(section.toMap());
        }
        return equals;
    }

    @NotNull
    public final Map<String, Object> toMap() {
        return Collections.unmodifiableMap(this.map);
    }

    @Override
    public final String toString() {
        return this.map.toString();
    }

    private void remove(@NotNull final String[] key) {
        if (key.length == 1) {
            this.map.remove(key[0]);
        } else {
            final Object temp = this.map.get(key[0]);
            if (temp instanceof Map) {
                //noinspection unchecked
                this.map.put(key[0], MemorySection.remove((Map<String, Object>) temp, key, 1));
                if (((Map<?, ?>) this.map.get(key[0])).isEmpty()) {
                    this.map.remove(key[0]);
                }
            }
        }
    }

}
