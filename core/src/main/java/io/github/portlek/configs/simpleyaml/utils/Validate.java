/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.portlek.configs.simpleyaml.utils;

/**
 * <p>This class assists in validating arguments.</p>
 *
 * <p>The class is based along the lines of JUnit. If an argument value is
 * deemed invalid, an IllegalArgumentException is thrown. For example:</p>
 *
 * <pre>
 * Validate.isTrue( i bigger than 0, "The value must be greater than zero: ", i);
 * Validate.notNull( surname, "The surname must not be null");
 * </pre>
 *
 * @author Apache Software Foundation
 * @author <a href="mailto:ola.berg@arkitema.se">Ola Berg</a>
 * @author Gary Gregory
 * @author Norm Deane
 * @since 2.0
 */
public class Validate {
    // Validate has no dependencies on other classes in Commons Lang at present

    /**
     * Constructor. This class should not normally be instantiated.
     */
    public Validate() {
        super();
    }

    /**
     * <p>Validate that the argument condition is <code>true</code>; otherwise
     * throwing an exception with the specified message. This method is useful when
     * validating according to an arbitrary boolean expression, such as validating a
     * primitive number or using your own custom validation expression.</p>
     *
     * <pre>
     * Validate.isTrue( (i bigger than 0), "The value must be greater than zero");
     * Validate.isTrue( myObject.isOk(), "The object is not OK");
     * </pre>
     *
     * @param expression the boolean expression to check
     * @param message the exception message if invalid
     * @throws IllegalArgumentException if expression is <code>false</code>
     */
    public static void isTrue(final boolean expression, final String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }
    // notNull
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument is not <code>null</code>;
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.notNull(myObject, "The object must not be null");</pre>
     *
     * @param object the object to check
     * @param message the exception message if invalid
     */
    public static void notNull(final Object object, final String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
    // notEmpty string
    //---------------------------------------------------------------------------------

    /**
     * <p>Validate that the specified argument string is
     * neither <code>null</code> nor a length of zero (no characters);
     * otherwise throwing an exception with the specified message.
     *
     * <pre>Validate.notEmpty(myString, "The string must not be empty");</pre>
     *
     * @param string the string to check
     * @param message the exception message if invalid
     * @throws IllegalArgumentException if the string is empty
     */
    public static void notEmpty(final String string, final String message) {
        if (string == null || string.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

}
