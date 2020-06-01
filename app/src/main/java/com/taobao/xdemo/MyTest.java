package com.taobao.xdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author bill
 * @Date on 2020/4/29
 * @Desc:
 */
public class MyTest {

    public static void main(String[] args) {
        //hashmap();

        List<String> idList = new ArrayList<>();
        idList.add("ssss");
        idList.add("sssswwwwwwwwwwsss");

        getActivityId(idList);
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
