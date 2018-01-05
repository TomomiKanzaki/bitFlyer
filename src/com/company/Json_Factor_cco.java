package com.company;

import java.util.LinkedHashMap;

public class Json_Factor_cco {

    //cancelchildorder
    public String product_code_cco = "product_code";
    public String child_order_id_cco = "child_order_id";
    public String child_order_acceptance_id_cco = "child_order_acceptance_id";

    public static LinkedHashMap<Object, Object> map_cco;

    LinkedHashMap<Object, Object> method(String pc, String coi, String coai) {

        map_cco = new LinkedHashMap<Object, Object>();
        map_cco.put(product_code_cco, pc);
        map_cco.put(child_order_id_cco, coi);
        map_cco.put(child_order_acceptance_id_cco, coai);

        return map_cco;
    }
}
