package io.github.portlek.configs.configuration;

import java.util.LinkedHashMap;
import java.util.Map;

public enum DataType {

    SORTED {
        @Override
        public Map<String, Object> getMap() {
            return new LinkedHashMap<>();
        }
    },
    UNSORTED {
        @Override
        public Map<String, Object> getMap() {
            return new LinkedHashMap<>();
        }
    };

    public abstract Map<String, Object> getMap();

}
