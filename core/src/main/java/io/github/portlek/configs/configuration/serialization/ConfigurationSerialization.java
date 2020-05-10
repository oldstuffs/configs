package io.github.portlek.configs.configuration.serialization;

import io.github.portlek.configs.configuration.Configuration;
import io.github.portlek.configs.util.Validate;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for storing and retrieving classes for {@link Configuration}.
 */
public class ConfigurationSerialization {

    public static final String SERIALIZED_TYPE_KEY = "==";

    private static final Map<String, Class<? extends ConfigurationSerializable>> aliases = new HashMap<>();

    private final Class<? extends ConfigurationSerializable> clazz;

    protected ConfigurationSerialization(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        this.clazz = clazz;
    }

    /**
     * Attempts to deserialize the given arguments into a new instance of the
     * given class.
     * <p>
     * The class must implement {@link ConfigurationSerializable}, including
     * the extra methods as specified in the javadoc of
     * ConfigurationSerializable.
     * <p>
     * If a new instance could not be made, an example being the class not
     * fully implementing the interface, null will be returned.
     *
     * @param args Arguments for deserialization
     * @param clazz Class to deserialize into
     * @return New instance of the specified class
     */
    @Nullable
    public static ConfigurationSerializable deserializeObject(@NotNull final Map<String, ?> args, @NotNull final Class<? extends ConfigurationSerializable> clazz) {
        return new ConfigurationSerialization(clazz).deserialize(args);
    }

    /**
     * Attempts to deserialize the given arguments into a new instance of the
     * given class.
     * <p>
     * The class must implement {@link ConfigurationSerializable}, including
     * the extra methods as specified in the javadoc of
     * ConfigurationSerializable.
     * <p>
     * If a new instance could not be made, an example being the class not
     * fully implementing the interface, null will be returned.
     *
     * @param args Arguments for deserialization
     * @return New instance of the specified class
     */
    @Nullable
    public static ConfigurationSerializable deserializeObject(@NotNull final Map<String, ?> args) {
        final Class<? extends ConfigurationSerializable> clazz;

        if (args.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
            try {
                final String alias = (String) args.get(ConfigurationSerialization.SERIALIZED_TYPE_KEY);

                if (alias == null) {
                    throw new IllegalArgumentException("Cannot have null alias");
                }
                clazz = ConfigurationSerialization.getClassByAlias(alias);
                if (clazz == null) {
                    throw new IllegalArgumentException("Specified class does not exist ('" + alias + "')");
                }
            } catch (final ClassCastException ex) {
                ex.fillInStackTrace();
                throw ex;
            }
        } else {
            throw new IllegalArgumentException("Args doesn't contain type key ('" + ConfigurationSerialization.SERIALIZED_TYPE_KEY + "')");
        }

        return new ConfigurationSerialization(clazz).deserialize(args);
    }

    /**
     * Attempts to get a registered {@link ConfigurationSerializable} class by
     * its alias
     *
     * @param alias Alias of the serializable
     * @return Registered class, or null if not found
     */
    @Nullable
    public static Class<? extends ConfigurationSerializable> getClassByAlias(@NotNull final String alias) {
        return ConfigurationSerialization.aliases.get(alias);
    }

    /**
     * Registers the given {@link ConfigurationSerializable} class by its
     * alias
     *
     * @param clazz Class to register
     */
    public static void registerClass(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        final DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);

        if (delegate == null) {
            ConfigurationSerialization.registerClass(clazz, ConfigurationSerialization.getAlias(clazz));
            ConfigurationSerialization.registerClass(clazz, clazz.getName());
        }
    }

    /**
     * Registers the given alias to the specified {@link
     * ConfigurationSerializable} class
     *
     * @param clazz Class to register
     * @param alias Alias to register as
     * @see SerializableAs
     */
    public static void registerClass(@NotNull final Class<? extends ConfigurationSerializable> clazz, @NotNull final String alias) {
        ConfigurationSerialization.aliases.put(alias, clazz);
    }

    /**
     * Unregisters the specified alias to a {@link ConfigurationSerializable}
     *
     * @param alias Alias to unregister
     */
    public static void unregisterClass(@NotNull final String alias) {
        ConfigurationSerialization.aliases.remove(alias);
    }

    /**
     * Unregisters any aliases for the specified {@link
     * ConfigurationSerializable} class
     *
     * @param clazz Class to unregister
     */
    public static void unregisterClass(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        while (ConfigurationSerialization.aliases.values().remove(clazz)) {
        }
    }

    /**
     * Gets the correct alias for the given {@link ConfigurationSerializable}
     * class
     *
     * @param clazz Class to get alias for
     * @return Alias to use for the class
     */
    @NotNull
    public static String getAlias(@NotNull final Class<? extends ConfigurationSerializable> clazz) {
        DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);

        if (delegate != null) {
            if (delegate.value() == null || delegate.value().equals(clazz)) {
                delegate = null;
            } else {
                return ConfigurationSerialization.getAlias(delegate.value());
            }
        }

        if (delegate == null) {
            final SerializableAs alias = clazz.getAnnotation(SerializableAs.class);

            if (alias != null && alias.value() != null) {
                return alias.value();
            }
        }

        return clazz.getName();
    }

    @Nullable
    public final ConfigurationSerializable deserialize(@NotNull final Map<String, ?> args) {
        Validate.notNull(args, "Args must not be null");

        ConfigurationSerializable result = null;
        Method method;

        if (result == null) {
            method = this.getMethod("deserialize");

            if (method != null) {
                result = this.deserializeViaMethod(method, args);
            }
        }

        if (result == null) {
            method = this.getMethod("valueOf");

            if (method != null) {
                result = this.deserializeViaMethod(method, args);
            }
        }

        if (result == null) {
            final Constructor<? extends ConfigurationSerializable> constructor = this.getConstructor();

            if (constructor != null) {
                result = this.deserializeViaCtor(constructor, args);
            }
        }

        return result;
    }

    @Nullable
    protected final Method getMethod(@NotNull final String name) {
        try {
            final Method method = this.clazz.getDeclaredMethod(name, Map.class);

            if (!ConfigurationSerializable.class.isAssignableFrom(method.getReturnType())) {
                return null;
            }
            if (!Modifier.isStatic(method.getModifiers())) {
                return null;
            }

            return method;
        } catch (final NoSuchMethodException | SecurityException ex) {
            return null;
        }
    }

    @Nullable
    protected final ConfigurationSerializable deserializeViaMethod(@NotNull final Method method, @NotNull final Map<String, ?> args) {
        try {
            final ConfigurationSerializable result = (ConfigurationSerializable) method.invoke(null, args);

            if (result == null) {
                Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method.toString() + "' of " + this.clazz + " for deserialization: method returned null");
            } else {
                return result;
            }
        } catch (final Throwable ex) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(
                Level.SEVERE,
                "Could not call method '" + method.toString() + "' of " + this.clazz + " for deserialization",
                ex instanceof InvocationTargetException ? ex.getCause() : ex);
        }

        return null;
    }

    @Nullable
    protected final Constructor<? extends ConfigurationSerializable> getConstructor() {
        try {
            return this.clazz.getConstructor(Map.class);
        } catch (final NoSuchMethodException | SecurityException ex) {
            return null;
        }
    }

    @Nullable
    protected final ConfigurationSerializable deserializeViaCtor(@NotNull final Constructor<? extends ConfigurationSerializable> ctor, @NotNull final Map<String, ?> args) {
        try {
            return ctor.newInstance(args);
        } catch (final Throwable ex) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(
                Level.SEVERE,
                "Could not call constructor '" + ctor.toString() + "' of " + this.clazz + " for deserialization",
                ex instanceof InvocationTargetException ? ex.getCause() : ex);
        }

        return null;
    }

}
