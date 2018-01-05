package com.company;

import java.util.LinkedHashMap;

public class Json_Factor_sco {

    //sendchildorder
    public String product_code_sco = "product_code";
    public String child_order_type_sco = "child_order_type";
    public String side_sco = "side";
    public String price_sco = "price";
    public String size_sco = "size";
    public String minute_to_expire_sco = "minute_to_expire";
    public String time_in_force_sco = "time_in_force";

    public static LinkedHashMap<Object, Object> map_sco;

    LinkedHashMap<Object, Object> method(String pc, String cot, String s, int p, double s2, int mte, String tif) {

        map_sco = new LinkedHashMap<Object, Object>();
        map_sco.put(product_code_sco, pc);
        map_sco.put(child_order_type_sco, cot);
        map_sco.put(side_sco, s);
        map_sco.put(price_sco, p);
        map_sco.put(size_sco, s2);
        map_sco.put(minute_to_expire_sco, mte);
        map_sco.put(time_in_force_sco, tif);

        return map_sco;
    }


}
