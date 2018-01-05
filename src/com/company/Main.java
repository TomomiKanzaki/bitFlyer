package com.company;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;


public class Main{
    static URL url;
    static HttpURLConnection connection = null;
    //bitFlyerへの接続URL
    static String path_api = "https://api.bitflyer.jp";

    //パブリックAPI
    static String path_1 = "/v1/getmarkets";
    static String path_2 = "/v1/markets";
    static String path_3 = "/v1/getmarkets/usa";
    static String path_4 = "/v1/getmarkets";
    static String path_5 = "/v1/getboard";
    static String path_6 = "/v1/board";
    static String path_7 = "/v1/getticker";
    static String path_8 = "/v1/ticker";
    static String path_9 = "/v1/getexecutions";
    static String path_10 = "/v1/executions";
    static String path_11 = "/v1/getchats";//data量多すぎのため、だいたい取得不可
    static String path_12 = "/v1/getchats/usa";//data量多すぎのため、だいたい取得不可
    static String path_13 = "/v1/gethealth";
    static String path_14 = "/v1/getboardstate";

    //プライベートAPI
    static String request_method;

    //GET用
    static String GET = "GET";
    //アクセス不可===許可してない
    static String path_private_1 = "/v1/me/getaddresses";
    static String path_private_2 = "/v1/me/getcoinins";
    static String path_private_3 = "/v1/me/getcoinouts";
    static String path_private_4 = "/v1/me/getdeposits";
    static String path_private_5 = "/v1/me/getwithdrawals";
    static String path_private_6 = "/v1/me/getbankaccounts";

    //アクセス可===許可している
    static String path_private_7 = "/v1/me/getpermissions";
    static String path_private_8 = "/v1/me/getbalance";
    static String path_private_9 = "/v1/me/getcollateral";
    static String path_private_10 = "/v1/me/getcollateralaccounts";
    static String path_private_11 = "/v1/me/getchildorders";
    static String path_private_12 = "/v1/me/getparentorders";
    static String path_private_13 = "/v1/me/getparentorder";
    static String path_private_14 = "/v1/me/getexecutions";
    static String path_private_15 = "/v1/me/getpositions";
    static String path_private_16 = "/v1/me/getcollateralhistory";
    static String path_private_17 = "/v1/me/gettradingcommission";

    //POST用===金のやり取り
    static String POST = "POST";
    static String path_private_18 = "/v1/me/withdraw";
    static String path_private_19 = "/v1/me/sendchildorder";
    static String path_private_20 = "/v1/me/sendparentorder";
    static String path_private_21 = "/v1/me/cancelchildorder";
    static String path_private_22 = "/v1/me/cancelparentorder";
    static String path_private_23 = "/v1/me/cancelallchildorders";

    static String text_hmac;
    static int ContentsLength = 0;

    //childorder用(試し)6
    static String child_order_type = "LIMIT";
    static String side = "BUY";
    static int prize = 1000000;
    static double size = 0.001;

    //JOSNデータ作成用
    static String json = null;
    static LinkedHashMap<?, ?> map_for_REQUEST_public;
    static LinkedHashMap<?, ?> map_for_REQUEST_private;
    static ObjectMapper mapper;

    //JSON要素(共通部分)
    static String currency_code = "JPY";
    static String bank_account = "BF10-2047-5749-47";
    static String product_code = "BTC_JPY";
    static int minute_to_expire = 10000;
    static String time_in_force = "GTC";

    static String line;
    static String str_line_public;
    static String str_line_private;

    //清掃APIへ接続(テスト用)
    static String Path_seiso = "https://seiso-app-dev01.invdev.jp";
    static String Path_seiso2 = "https://rem-dev01.invdev.jp/iss/api/app/1/property.php";
    static String seiso_POST = "/v1/Login.php";
    static String ID_test = "yonehara";
    static String PW_test = "yonehara";


    public static void main(String[] args)
    {
        //PUBLIC_APIへのリクエスト
//        List vars_public = Arrays.asList(path_1,path_2,path_3,path_4,path_5,path_6,path_7,path_8,path_9,path_10,path_11,path_12,path_13,path_14);
//        for (int i = 0; i < vars_public.size(); i++){
//            if (i == 10|| i == 11)continue;
//              System.out.println("[execute_public]>>>" + execute_public((String) vars_public.get(i)));
//        }
////        PRIVATE_APIへリクエスト
//        System.out.println("[execute_private]>>>" + execute_private(path_private_8));
//
//
//
////        private8(例)のレスポンスを取得する場合、処理の過程でmapとlistを行ったり来たりするので以下のように対応が必要
//        List list_private_8 = (List) execute_private(path_private_8).get("hoge");
//        Map map_private_8 = (Map) list_private_8.get(0);
//        System.out.println("[available]>>>" + map_private_8.get("available"));
//            System.out.println("[execute_private]>>>" + execute_private(path_private_8).get("hoge"));

//        清掃APIへの接続(テスト)
        try {
            System.out.println(test_map(seiso_POST));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    //PUBLIC_APIへアクセス
    private static LinkedHashMap<?, ?> execute_public(String path) {
        System.out.println("\n===== HTTP Public " + GET + " Start of " + path +"=====\n");
        try {
            url = new URL(path_api + path);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod(GET);
                connection.setRequestProperty("Accept-Language", "jp");
                connection.setRequestProperty("Content-Type", "application/JSON; charset=utf-8");
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //成功レスポンスの出力
                    try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                                StandardCharsets.UTF_8);
                         BufferedReader reader = new BufferedReader(isr)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            str_line_public = line;
                            System.out.println("[SUCCESS-RESPONSE(JSON)]>>>" + str_line_public);
                        }
                        reader.close();
                        isr.close();
                    }
                }else{
                    //エラー内容の出力
                    System.out.println("[ERROR-CODE]>>>" + connection.getResponseCode());
                    try {
                        InputStreamReader isr = new InputStreamReader(connection.getErrorStream(),
                                StandardCharsets.UTF_8);
                        BufferedReader reader = new BufferedReader(isr);
                        StringBuffer sb = new StringBuffer();
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                            str_line_public = line;
                            System.out.println("[ERROR-RESPONSE]>>>" + str_line_public);
                        }
                        reader.close();
                        isr.close();
                    }catch (Exception ex){
                        System.out.println(ex.toString());
                    }
                }
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n===== HTTP Public " + GET + " End of " + path + " =====\n");
        System.out.println("[APIからのレスポンス(JSON)_PUBLIC]>>>" + str_line_public);
        try {
            map_for_REQUEST_public = StringToMap(str_line_public);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[JSONを変換したMAP_PUBLIC]>>>" + map_for_REQUEST_public);
        return map_for_REQUEST_public;
    }


    //PRIVATE_APIへアクセス
    private static LinkedHashMap<?, ?> execute_private(String path){
        System.out.println("\n===== HTTP Private Start of " + path +"=====\n");

        String API_key = "RtBHZDQJmpyKXw4y4DiKxP";
        String API_secret = "rUv9hjDx0OKkkL0cJrw22qqXQy4UbQpiJp0tNuvHwzg=";
        LocalDateTime datetime = LocalDateTime.now();
        String time_stamp = datetime.toString();
        String algo = "HMacSHA256";
        //POSTへのリクエストの場合、bodyをJSON文字列に変換
        if (path == path_private_18){
            //withdraw>>>使わない予定
            Json_Factor_wd json_factor_wd = new Json_Factor_wd();
            map_for_REQUEST_private = json_factor_wd.method(currency_code, bank_account, 0);
            json = Map_to_JSON(map_for_REQUEST_private);
        }else if (path == path_private_19){
            //新規注文
            Json_Factor_sco json_factor_sco = new Json_Factor_sco();
            map_for_REQUEST_private = json_factor_sco.method(product_code, child_order_type, side, prize, size, minute_to_expire, time_in_force);
            System.out.println(map_for_REQUEST_private);
            json = Map_to_JSON(map_for_REQUEST_private);
        }else if (path == path_private_20){
            //親注文
            Json_Factor_spo json_factor_spo = new Json_Factor_spo();
            //map_for_REQUEST = json_factor_spo.method()
            json = Map_to_JSON(map_for_REQUEST_private);
        }else if (path == path_private_21){
            //注文のキャンセル
            Json_Factor_cco json_factor_cco = new Json_Factor_cco();
            //map_for_REQUEST = json_factor_spo.method()
            json = Map_to_JSON(map_for_REQUEST_private);
        }else if (path == path_private_22){
            //親注文のキャンセル
            Json_Factor_cpo json_factor_cpo = new Json_Factor_cpo();
            //map_for_REQUEST = json_factor_spo.method()
            json = Map_to_JSON(map_for_REQUEST_private);
        }else if (path == path_private_23){
            //すべての注文のキャンセル
            Json_Factor_caco json_factor_caco = new Json_Factor_caco();
            map_for_REQUEST_private = json_factor_caco.method(product_code);
            json = Map_to_JSON(map_for_REQUEST_private);
        }
        //GET/POST判別
        if (json == null){
            request_method = GET;
            text_hmac = time_stamp + request_method + path;
        }else{
            request_method = POST;
            text_hmac = time_stamp + request_method + path + json;
            try {
                ContentsLength = json.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        final SecretKeySpec keySpec = new SecretKeySpec(API_secret.getBytes(), algo);
        final StringBuilder secret = new StringBuilder();
        final Mac mac;
        try {
            mac = Mac.getInstance(algo);
            mac.init(keySpec);
            final byte[] signBytes = mac.doFinal(text_hmac.getBytes());
            for (byte signByte: signBytes){
                secret.append(String.format("%02x", signByte&0xff) );
            }
            try{
                url = new URL(path_api + path);
                try{
                    System.out.println("request_method>>>" + request_method);
                    System.out.println("ContentsLength>>>" + ContentsLength);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(request_method);
                    connection.setRequestProperty("ACCESS-KEY", API_key);
                    connection.setRequestProperty("ACCESS-TIMESTAMP", time_stamp);
                    connection.setRequestProperty("ACCESS-SIGN", secret.toString());
                    connection.setRequestProperty("Accept-Language", "jp");
                    connection.setRequestProperty("Content-Type", "application/JSON; charset=utf-8");
                    //POST用
                    if (json != null){
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Length", String.valueOf(ContentsLength));
                        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                        out.write(json);
                        out.flush();
                    }
                    connection.connect();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        //成功レスポンスの出力
                        System.out.println("[SUCCESS-CODE]>>>" + connection.getResponseCode());
                        try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                                StandardCharsets.UTF_8);
                            BufferedReader reader = new BufferedReader(isr)) {
                            while ((line = reader.readLine()) != null) {
                                str_line_private = line;
                            }
                            reader.close();
                            isr.close();
                        }
                    }else{
                        //エラー内容の出力
                        System.out.println("[ERROR-CODE]>>>" + connection.getResponseCode());
                        try {
                            InputStreamReader isr = new InputStreamReader(connection.getErrorStream(),
                                StandardCharsets.UTF_8);
                            BufferedReader reader = new BufferedReader(isr);
                            while ((line = reader.readLine()) != null) {
                                str_line_private = line;
                            }
                            reader.close();
                            isr.close();
                        }catch (Exception ex){
                            System.out.println(ex.toString());
                        }
                    }
                }catch (Exception ex){
                    System.out.println(ex);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        System.out.println("\n===== HTTP Private END of " + path + " =====\n");
        System.out.println("[APIからのレスポンス(JSON)_PRIVATE]>>>" + str_line_private);
        try {
            map_for_REQUEST_private = StringToMap(str_line_private);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[JSONを変換したMAP_PRIVATE]>>>" + map_for_REQUEST_private);
        return map_for_REQUEST_private;
    }
    //map_for_REQUESTをJSON文字列へ変換
    private static String Map_to_JSON(LinkedHashMap map){
        String Map_to_JSON = new String();
        map_for_REQUEST_private = new LinkedHashMap<Object, Object>();
        mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        //JSONデータ作成
        try {
            Map_to_JSON = mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("[JSON文字列]>>>" + Map_to_JSON);
        return Map_to_JSON;
    }
    //String状態のJSONデータをMAPへ変換
    public static LinkedHashMap<?, ?> StringToMap(String string) throws Exception {
        String str_index1 = string.substring(0, 1);
        switch (str_index1){
            case "[":
                string = "{\"hoge\":" + string + "}";
                break;
        }
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<?, ?> map = mapper.readValue(string, LinkedHashMap.class);

        return map;
    }


    //test用
    public static String test_map(String path_test) throws Exception {
        try{
            url = new URL(Path_seiso + path_test);
            Test_Factor factor = new Test_Factor();
            map_for_REQUEST_private = factor.method(ID_test, PW_test);
            json = Map_to_JSON(map_for_REQUEST_private);
            try {
                ContentsLength = json.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try{
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(POST);

//                connection.setRequestProperty("userId", ID_test);
//                connection.setRequestProperty("userPass", PW_test);
                connection.setRequestProperty("Accept-Language", "jp");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//                //POST用
                connection.setDoInput(true);
                connection.setDoOutput(true);
////                connection.setRequestProperty("Content-Length", String.valueOf(ContentsLength));
//                connection.setRequestProperty("Content-Length", String.valueOf(ContentsLength));
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(json);
                out.flush();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //成功レスポンスの出力
                    System.out.println("[SUCCESS-CODE]>>>" + connection.getResponseCode());

//                    url = new URL(Path_seiso2);
//                    connection = (HttpURLConnection)url.openConnection();
//                    connection.connect()　;
//                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                        System.out.println("[物件情報]>>>" + connection.getContent());
//                    }
                    try (InputStreamReader isr = new InputStreamReader(connection.getInputStream(),
                            StandardCharsets.UTF_8);
                         BufferedReader reader = new BufferedReader(isr)) {
                        System.out.println("[reader.readLine]>>>" + reader.readLine());
                        while ((line = reader.readLine()) != null) {
                            //レスポンス格納
                            System.out.println("[Line]>>>" + line);
                        }
                        reader.close();
                        isr.close();
                    }
                }else{
                    //エラー内容の出力
                    System.out.println("[ERROR-CODE]>>>" + connection.getResponseCode());
                }
            }catch (Exception ex){
                System.out.println(ex);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return str_line_private;
    }
}