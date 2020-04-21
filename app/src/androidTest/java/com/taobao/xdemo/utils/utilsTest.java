package com.taobao.xdemo.utils;

import static org.junit.Assert.*;

/**
 * @author bill
 * @Date on 2020/4/21
 * @Desc:
 */
public class utilsTest {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.Test
    public void isLoginLink() {

        assertEquals(utils.isLoginLink("tbopen://m.taobao.com/sso"), true);
    }
}