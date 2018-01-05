package com.company;

import java.util.LinkedHashMap;

public class Json_Factor_caco {

    //cancelallchildorder
    public String product_code_caco = "product_code";

    public static LinkedHashMap<Object, Object> map_caco;

    LinkedHashMap<Object, Object> method(String pc) {

        map_caco = new LinkedHashMap<Object, Object>();
        map_caco.put(product_code_caco, pc);

        return map_caco;
    }

}
