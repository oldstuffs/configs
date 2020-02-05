package org.simpleyaml.exceptions;

import org.simpleyaml.configuration.Configuration;

/**
 * Exception thrown when attempting to load an invalid {@link Configuration}
 * @author Bukkit <https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/InvalidConfigurationException.java>
 */
@SuppressWarnings("serial")
public class InvalidConfigurationException extends Exception {

    /**
     * Constructs an instance of InvalidConfigurationException with the
     * specified message.
     *
     * @param msg The details of the exception.
     */
    public InvalidConfigurationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of InvalidConfigurationException with the
     * specified cause.
     *
     * @param cause The cause of the exception.
     */
    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

}
