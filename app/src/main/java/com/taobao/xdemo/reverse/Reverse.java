package com.taobao.xdemo.reverse;

import java.util.LinkedList;

/**
 * @author bill
 * @Date on 2020/7/6
 * @Desc: 反转linkedList
 */
class Reverse {

    public static void main(String[] args) {
        // linkedlist翻转
        //LinkedListReverse();

        // 链表反转
        链表反转();
    }

    private static void 链表反转() {
        Node head = new Node(0);
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        head.setNext(node1);
        node1.setNext(node2);
        node2.setNext(node3);

        // 打印反转前的链表
        Node h = head;
        while (null != h) {
            System.out.print(h.getData() + " ");
            h = h.getNext();
        }

        // 调用反转方法
        head = Reverse1(head);

        System.out.println("\n**************************");

        // 打印反转后的结果
        while (null != head) {
            System.out.print(head.getData() + " ");
            head = head.getNext();
        }
    }

    /**
     * 递归，在反转当前节点之前先反转后续节点
     */
    public static Node Reverse1(Node head) {
        // head看作是前一结点，head.getNext()是当前结点，reHead是反转后新链表的头结点
        if (head == null || head.getNext() == null) {
            return head;// 若为空链或者当前结点在尾结点，则直接还回
        }

        Node reHead = Reverse1(head.getNext());// 先反转后续节点head.getNext()
        head.getNext().setNext(head);// 将当前结点的指针域指向前一结点
        head.setNext(null);// 前一结点的指针域令为null;

        return reHead;// 反转后新链表的头结点
    }

    private static void LinkedListReverse() {
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
