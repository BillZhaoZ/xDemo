package com.taobao.xdemo;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.net.Uri;

/**
 * @author bill
 * @Date on 2020/4/29
 * @Desc:
 */
public class MyTest {

    public static void main(String[] args) {
        //hashmap();

       /* List<String> idList = new ArrayList<>();
        idList.add("ssss");
        idList.add("sssswwwwwwwwwwsss");

        getActivityId(idList);*/

        String s =
            "https%3A%2F%2Fm.taobao.com%2Findex.htm%3Fscrollto%3Drecmd%26target%3Dguess%26recmdparams%3D%257b"
                + "%2522tabindex%2522%253a0%252c%2522bizparams%2522%253a%257b%2522outPushPlanId%2522%253a%25223YfuAj"
                + "%2522%252c%2522test%2522%253a%2522testvalue%2522%257d%257d%26tppabid%3D170722%26pvid%3D33db1135"
                + "-7edf-48c4-a847-f8ca68e8cc4b%26bucketid%3DGuDing%26_afc_params_json%3D%257B%2522tppabid%2522%253A"
                + "%2522170722%2522%252C%2522appKey%2522%253A%252223262200%2522%252C%2522bc_fl_src%2522%253A"
                + "%2522bc_ultimate_android%2522%252C%2522shopId%2522%253A%2522unknow%2522%257D%26_afc_params_kv"
                + "%3DappKey%250123262200%2502bc_fl_src%2501bc_ultimate_android%2502shopId%2501unknow%2502tppabid"
                + "%2501170722%26launchType%3DCOLD";

        Uri parse = Uri.parse(s);

        String afcBackUrl = parse.getQueryParameter("afcBackUrl");

        System.out.println("dayin: " + afcBackUrl);

    }

    private static String getActivityId(List<String> idList) {
        if (idList == null || idList.size() == 0) {
            return "";
        }

        StringBuilder afcId = new StringBuilder();

        for (String id : idList) {
            afcId.append(id).append(",");
        }

        System.out.println("dayin: " + afcId.substring(0, afcId.length() - 1));

        return afcId.substring(0, afcId.length() - 1);
    }

    private static void hashmap() {
        try {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("1", "Hello");
            hashMap.put("2", "World");
            //            bb.remove("1");  //直接删除的方式 不会报错
            Iterator<String> it = hashMap.keySet().iterator();
            while (it.hasNext()) {
                Object ele = it.next();
                //System.out.println(hashMap);
                if (ele.equals("1")) {
                    //hashMap.remove(ele);    //出错 修改了映射结构 影响了迭代器遍历
                    it.remove();              //用迭代器删除 则不会出错
                }
            }

            System.out.println("dayin" + hashMap);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

}
