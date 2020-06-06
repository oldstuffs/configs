/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package io.github.portlek.configs.files.yaml.eoyaml;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Yaml mapping.
 *
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id: 64c8f3c72feccf3b9dfbe994bb8d814466f6cd42 $
 * @since 1.0.0
 */
public interface YamlMapping extends YamlNode {

    /**
     * Return the keys' set of this mapping.<br><br>
     *
     * @return Set of YamlNode keys.
     */
    Set<YamlNode> keys();

    /**
     * Get the YamlNode mapped to the specified key.
     *
     * @param key YamlNode key. Could be a simple scalar,
     * a YamlMapping or a YamlSequence.
     * @return The found YamlNode or null if nothing is found.
     */
    YamlNode value(YamlNode key);

    /**
     * Fetch the values of this mapping.
     *
     * @return Collection of {@link YamlNode}
     */
    default Collection<YamlNode> values() {
        return this.keys().stream()
            .map(this::value)
            .collect(Collectors.toCollection(LinkedList::new));
    }

}
