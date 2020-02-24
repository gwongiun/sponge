package com.ly.add.sponge.common.utils;

import java.util.*;

/**
 * @author : qqy48861
 * date : 2019/6/12.
 */
public class MapUtil {

    public static <T> Map<String, T> sortMapByValueDesc(Map<String, T> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, T> sortedMap = new LinkedHashMap<>();
        List<Map.Entry<String, T>> entryList = new ArrayList<>(oriMap.entrySet());
        entryList.sort((Map.Entry<String, T> me1, Map.Entry<String, T> me2) -> Double.compare(Double.parseDouble(me2.getValue().toString()), Double.parseDouble(me1.getValue().toString())));

        Iterator<Map.Entry<String, T>> iter = entryList.iterator();
        Map.Entry<String, T> tmpEntry;
        while (iter.hasNext()) {
            tmpEntry = iter.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }

}