package com.taobao.xdemo.notification;

import java.io.Serializable;

/**
 * @author bill
 * @Date on 2019-08-26
 * @Desc: 推送的消息数据
 */
public class MessageData implements Serializable {

    public String ladningUrl;  // 落地页URL
    public String iconUrl; // icon
    public int messageType; // 消息类型
}
