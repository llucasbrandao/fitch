package com.lucasbrandao.restaurantapi.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapListUtils {
	
	/*
	 * Método auxiliar para converter valores de um Map para string
	 */
	public static Map<String, String> mapObjectToString(Map<String, Object> map) {
		Map<String, String> mapString = new HashMap<>();
				
		for (String key : map.keySet()) 
			mapString.put(key, map.get(key).toString());
		
		return mapString;
	}
	
	public static Map<String, String> listMapObjectToString(List<Map<String, Object>> map) {
		Map<String, String> mapString = new HashMap<>();
				
		for (Map<String, Object> mapObject : map)
			for (Map.Entry<String, Object> entry : mapObject.entrySet())
			mapString.put(entry.getKey(), entry.getValue().toString());
		
		return mapString;
	}
	
	/*
	 * Extrai a key informada do Map e devolve um Set com seus respectivos valores.
	 * O intuito deste método é produzir uma lista sem elementos repetidos, a partir de um Map que pode ter elementos duplicados.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> mapKeyToGenericSet(List<Map<String, Object>> map, String key) {
		Set<T> setList = new HashSet<T>();
		
		for (Map<String, Object> o : map)
			for (String akey : o.keySet()) {
				if (akey.equals(key))
					setList.add((T) o.get(key));
			}
		
		return setList;
	}
}
