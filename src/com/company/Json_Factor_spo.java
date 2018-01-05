package com.company;

import java.util.LinkedHashMap;

public class Json_Factor_spo {

    //sendparentorder
    public String order_method_spo = "order_method";
    public String minute_to_expire_spo = "minute_to_expire";
    public String time_in_force_spo = "time_in_force";
    public String parameters_spo = "parameters";
    public String size_spo = "size";
    public String product_code_spo = "product_code";
    public String condition_type_spo = "condition_type";
    public String price_spo = "price";
    public String trigger_price_spo = "trigger_price";

    public static LinkedHashMap<Object, Object> map_spo;

    LinkedHashMap<Object, Object> method(String oms, String mte, String tif, String p, String s, String pc, String ct, int p2, int tp) {

        map_spo = new LinkedHashMap<Object, Object>();
        map_spo.put(order_method_spo, oms);
        map_spo.put(minute_to_expire_spo, mte);
        map_spo.put(time_in_force_spo, tif);
        map_spo.put(parameters_spo, p);
        map_spo.put(size_spo, s);
        map_spo.put(product_code_spo, pc);
        map_spo.put(condition_type_spo, ct);
        map_spo.put(price_spo, p2);
        map_spo.put(trigger_price_spo, tp);

        return map_spo;
    }
}
