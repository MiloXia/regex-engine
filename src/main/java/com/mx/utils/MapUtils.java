package com.mx.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MapUtils {
    public static <K, V, V2> Map<K, V2> mapValue(Map<K, V> map, Function<V, V2> mapping) {
        Map<K, V2> res = new HashMap<>(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            res.put(entry.getKey(), mapping.apply(entry.getValue()));
        }
        return res;
    }
}
