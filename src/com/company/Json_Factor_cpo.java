package com.company;

import java.util.LinkedHashMap;

public class Json_Factor_cpo {

    //cancelparentorder
    public String product_code_cpo = "product_code";
    public String parent_order_id_cpo = "parent_order_id";
    public String parent_order_acceptance_id_cpo = "parent_order_acceptance_id";

    public static LinkedHashMap<Object, Object> map_cpo;

    LinkedHashMap<Object, Object> method(String pcc, String poi, String poai) {

        map_cpo = new LinkedHashMap<Object, Object>();
        map_cpo.put(product_code_cpo, pcc);
        map_cpo.put(parent_order_id_cpo, poi);
        map_cpo.put(parent_order_acceptance_id_cpo, poai);

        return map_cpo;
    }


}
