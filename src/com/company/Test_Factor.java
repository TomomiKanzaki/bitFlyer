package com.company;

import java.util.LinkedHashMap;

public class Test_Factor {
    public static String ID = "userId";
    public static String PW = "userPass";
    public static LinkedHashMap<Object, Object> map;
    LinkedHashMap<Object, Object> method(String id, String pw) {

        map = new LinkedHashMap<Object, Object>();
        map.put(ID, id);
        map.put(PW, pw);

        return map;
    }

}
