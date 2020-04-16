package io.github.portlek.configs.jsonconfiguration.util;

import io.github.portlek.configs.jsonconfiguration.SerializableSet;
import io.github.portlek.configs.jsonconfiguration.YAMLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import io.github.portlek.configs.simpleyaml.configuration.ConfigurationSection;
import io.github.portlek.configs.simpleyaml.configuration.serialization.ConfigurationSerializable;
import io.github.portlek.configs.simpleyaml.configuration.serialization.ConfigurationSerialization;

/**
 * @author Jeremy Wood
 * @version 6/14/2017
 */
public class SerializationHelper {

    private static final Logger LOG = Logger.getLogger(SerializationHelper.class.getName());

    public static Object serialize(@NotNull Object value) {
        if (value instanceof Object[]) {
            value = new ArrayList<>(Arrays.asList((Object[]) value));
        }
        if (value instanceof Set && !(value instanceof SerializableSet)) {
            value = new SerializableSet((Set<?>) value);
        }
        if (value instanceof ConfigurationSection) {
            return SerializationHelper.buildMap(((ConfigurationSection) value).getValues(false));
        } else if (value instanceof Map) {
            return SerializationHelper.buildMap((Map<?, ?>) value);
        } else if (value instanceof List) {
            return SerializationHelper.buildList((List<?>) value);
        } else if (value instanceof ConfigurationSerializable) {
            final ConfigurationSerializable serializable = (ConfigurationSerializable) value;
            final Map<String, Object> values = new LinkedHashMap<>();
            values.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY, ConfigurationSerialization.getAlias(serializable.getClass()));
            values.putAll(serializable.serialize());
            return SerializationHelper.buildMap(values);
        } else {
            return value;
        }
    }

    public static Object deserialize(@NotNull final Map<?, ?> input) {
        final Map<String, Object> output = new LinkedHashMap<>(input.size());
        for (final Map.Entry<?, ?> e : input.entrySet()) {
            if (e.getValue() instanceof Map) {
                output.put(e.getKey().toString(), SerializationHelper.deserialize((Map<?, ?>) e.getValue()));
            } else if (e.getValue() instanceof List) {
                output.put(e.getKey().toString(), SerializationHelper.deserialize((List<?>) e.getValue()));
            } else {
                output.put(e.getKey().toString(), e.getValue());
            }
        }
        if (output.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
            try {
                return ConfigurationSerialization.deserializeObject(output);
            } catch (final IllegalArgumentException ex) {
                throw new YAMLException("Could not deserialize object", ex);
            }
        }
        return output;
    }

    /**
     * Takes a Map and parses through the values, to ensure that, before saving, all objects are as appropriate as
     * possible for storage in most data formats.
     * <p>
     * Specifically it does the following:
     * for Map: calls this method recursively on the Map before putting it in the returned Map.
     * for List: calls {@link #buildList(Collection)} which functions similar to this method.
     * for ConfigurationSection: gets the values as a map and calls this method recursively on the Map before putting
     * it in the returned Map.
     * for ConfigurationSerializable: add the {@link ConfigurationSerialization#SERIALIZED_TYPE_KEY} to a new Map
     * along with the Map given by {@link ConfigurationSerializable#serialize()}
     * and calls this method recursively on the new Map before putting it in the returned Map.
     * for Everything else: stores it as is in the returned Map.
     */
    @NotNull
    private static Map<String, Object> buildMap(@NotNull final Map<?, ?> map) {
        final Map<String, Object> result = new LinkedHashMap<>(map.size());
        try {
            for (final Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(entry.getKey().toString(), SerializationHelper.serialize(entry.getValue()));
            }
        } catch (final Exception e) {
            SerializationHelper.LOG.log(Level.WARNING, "Error while building configuration map.", e);
        }
        return result;
    }

    /**
     * Takes a Collection and parses through the values, to ensure that, before saving, all objects are as appropriate
     * as possible for storage in most data formats.
     * <p>
     * Specifically it does the following:
     * for Map: calls {@link #buildMap(Map)} on the Map before adding to the returned list.
     * for List: calls this method recursively on the List.
     * for ConfigurationSection: gets the values as a map and calls {@link #buildMap(Map)} on the Map
     * before adding to the returned list.
     * for ConfigurationSerializable: add the {@link ConfigurationSerialization#SERIALIZED_TYPE_KEY} to a new Map
     * along with the Map given by {@link ConfigurationSerializable#serialize()}
     * and calls {@link #buildMap(Map)} on the new Map before adding to the returned list.
     * for Everything else: stores it as is in the returned List.
     */
    private static List<Object> buildList(@NotNull final Collection<?> collection) {
        final List<Object> result = new ArrayList<>(collection.size());
        try {
            for (final Object o : collection) {
                result.add(SerializationHelper.serialize(o));
            }
        } catch (final Exception e) {
            SerializationHelper.LOG.log(Level.WARNING, "Error while building configuration list.", e);
        }
        return result;
    }

    /**
     * Parses through the input list to deal with serialized objects a la {@link ConfigurationSerializable}.
     * <p>
     * Functions similarly to {@link #deserialize(Map)} but only for detecting lists within
     * lists and maps within lists.
     */
    private static Object deserialize(@NotNull final List<?> input) {
        final List<Object> output = new ArrayList<>(input.size());
        for (final Object o : input) {
            if (o instanceof Map) {
                output.add(SerializationHelper.deserialize((Map<?, ?>) o));
            } else if (o instanceof List) {
                output.add(SerializationHelper.deserialize((List<?>) o));
            } else {
                output.add(o);
            }
        }
        return output;
    }

}
