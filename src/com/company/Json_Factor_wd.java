package com.company;

import java.util.LinkedHashMap;

public class Json_Factor_wd {
    //withdraw
    public static String currency_code_wd = "currency_code";
    public static String bank_account_id_wd = "bank_account_id";
    public static String amount_wd = "amount";
    public static String code_wd = "code";

    public static LinkedHashMap<Object, Object> map_wd;

    LinkedHashMap<Object, Object> method(String cc, String bai, int aw) {

        map_wd = new LinkedHashMap<Object, Object>();
        map_wd.put(currency_code_wd, cc);
        map_wd.put(bank_account_id_wd, bai);
        map_wd.put(amount_wd, aw);
//        map_wd.put(code_wd, c);

        return map_wd;
    }
}
