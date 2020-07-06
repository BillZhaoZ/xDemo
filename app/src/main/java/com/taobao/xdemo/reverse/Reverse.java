package com.taobao.xdemo.reverse;

import java.util.LinkedList;

/**
 * @author bill
 * @Date on 2020/7/6
 * @Desc: 反转linkedList
 */
class Reverse {

    public static void main(String[] args) {
        LinkedList<Object> newLinkedList = new LinkedList<>();

        newLinkedList.add("1");
        newLinkedList.add("2");
        newLinkedList.add("3");
        newLinkedList.add("4");
        newLinkedList.add("5");
        newLinkedList.add("6");

        System.out.println("反转前：" + newLinkedList);

        newLinkedList = reverseLinkedList(newLinkedList);

        System.out.println("反转后：" + newLinkedList);
    }

    static LinkedList reverseLinkedList(LinkedList linkedList) {
        LinkedList<Object> newLinkedList = new LinkedList<>();
        for (Object object : linkedList) {
            newLinkedList.add(0, object);

            System.out.println("反转中：" + newLinkedList);
        }

        return newLinkedList;
    }

}
