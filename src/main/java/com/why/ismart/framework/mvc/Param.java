package com.why.ismart.framework.mvc;

import java.util.Map;

public class Param {

    private final Map<String, Object> map;

    public Param(Map<String, Object> map) {
        this.map = map;
    }
    
    public long getLong(String key) {
        return Long.parseLong(map.get(key).toString());
    }
    
}
