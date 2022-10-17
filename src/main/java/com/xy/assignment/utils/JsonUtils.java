package com.xy.assignment.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

/**
 * @ Author: Xiong Yao
 * @ Date: Created at 7:43 PM 10/16/2022
 * @ Description: json formatter
 * @ Version: 1.0
 * @ Email: gongchen711@gmail.com
 */


public class JsonUtils {

    public static String toJsonString(String key, Object value) {
        Map<String, Object> res = new HashMap<>();
        res.put(key, value);
        return JSON.toJSONString(res);
    }

}
