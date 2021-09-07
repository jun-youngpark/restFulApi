package com.mnwise.wiseu.web.rest.dto;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONObject;

public class DataMap<K, V> {
    private static final long serialVersionUID = 1L;
	private final Map<String, Object> map;
	private ObjectMapper mapper;

    public DataMap() {
        map = new HashMap<String, Object>();
        mapper = new ObjectMapper();
    }

	public DataMap<K, V> put(String key, Object value) {
		map.put(key, value);
		return this;
	}

	public JSONObject build() throws JsonProcessingException {
        return mapper.convertValue(map, JSONObject.class);
    }

}
