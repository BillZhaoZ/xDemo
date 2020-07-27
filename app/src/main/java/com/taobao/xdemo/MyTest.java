package com.taobao.xdemo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * @author bill
 * @Date on 2020/4/29
 * @Desc:
 */
public class MyTest {

    public static void main(String[] args) {

    }

    String DEFAULT_JSON = "{\"items\":[{\"data\":{\"entries\":[{\"bizCode\":\"widget_coin\",\"stateful\":\"true\","
        + "\"title\":\"领金币\",\"url\":\"https://market.m.taobao.com/app/tmall-wireless/tjb-2018/index/index"
        + ".html?disableNav=YES\"},{\"bizCode\":\"widget_logistic\",\"stateful\":\"true\",\"title\":\"查物流\","
        + "\"url\":\"https://m.duanqu.com?_wml_code=vvslIPG4dzypz9NyrjCAq5IPalrXJLavIpXHcBKguXM%3D&_ariver_appid"
        + "=11509317&query=from%3Dlittletool&from=littletool\"},{\"bizCode\":\"widget_order\",\"stateful\":\"false\","
        + "\"title\":\"查订单\",\"url\":\"http://tm.m.taobao.com/list.htm?OrderListType=total_orders\"}],"
        + "\"keyword\":\"半身裙\",\"promotionBizCode\":\"widget_promotion\",\"promotionIcon\":\"https://gw.alicdn"
        + ".com/tfs/TB1_IKgwW61gK0jSZFlXXXDKFXa-72-72.png\",\"promotionText\":\"翻倍赚金币\","
        + "\"promotionUrl\":\"https://market.m.taobao.com/app/tmall-wireless/tjb-2018/index/index"
        + ".html?disableNav=YES\",\"searchUrl\":\"http://s.m.taobao.com/h5?q=%E5%8D%8A%E8%BA%AB%E8%A3%99\"},"
        + "\"type\":\"2\"}]}";

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, Object> getUrlParams(String param) {
        Map<String, Object> map = new HashMap<>(0);
        String[] params = param.split("&");

        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }

        return map;
    }

    private static void reWriteAfcId(String landingUrl, String channel, boolean isGrowthWord, String clipBoard) {
        try {
            //String afcId = UTAnalytics.getInstance().getDefaultTracker().getGlobalProperty("_afc_id");
            String afcId
                = "afc_launch^com.taobao.taobao^1012_Initiactive^83880b2c-7c52-4236-a126-b502dee083bd_1594692743912";

            String[] split = afcId.split("\\^");
            split[1] = channel;

            if (landingUrl.contains("bc_fl_src")) {
                Map<String, Object> urlParams = getUrlParams(landingUrl);

                String bc_fl_src = (String)urlParams.get("bc_fl_src");
                split[2] = bc_fl_src;
            } else {
                split[2] = isGrowthWord ? clipBoard : "dahanghai";
            }

            StringBuffer stringBuffer = new StringBuffer();

            for (String s : split) {
                stringBuffer.append(s).append("^");
            }

            System.out.println("afc_id: " + stringBuffer);

            //UTAnalytics.getInstance().getDefaultTracker().setGlobalProperty("_afc_id", afcId);

        } catch (Exception e) {
            System.out.println("afc_id:异常 " + e);
        }
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
