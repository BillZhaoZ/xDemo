package com.taobao.xdemo;

import java.util.HashMap;

public class TestC {

    public static void main(String[] args) {
        hahahMater444();
    }

    private static void hahahMater444() {
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("cc", "cccc");
        objectObjectHashMap.put("cc", "cccc");
        objectObjectHashMap.put("cc", "cccc");
        objectObjectHashMap.put("cc", "cccc");
        objectObjectHashMap.put("cc", "cccc");

        Object dddd = objectObjectHashMap.get("dddd");

        System.out.println("ddd" + dddd);
    }

    synchronized void getccc() {

    }
}
