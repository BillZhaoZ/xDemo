package com.taobao.xdemo;

/**
 * @author bill
 * @Date on 2020/4/29
 * @Desc:
 */
public class MyTest {

    public static void main(String[] args) {

        // paraName=k1\001v1\002k2\001v2
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("appKey").append("001").append("22222222").append("001").append(
            "bc_fl_src").append("001").append("tmall_hw_sem");

        System.out.println(stringBuffer.toString());
    }

}
