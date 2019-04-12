package com.asiainfo.km.test;

import org.junit.Test;

import java.sql.Timestamp;

/**
 * Created by jiyuze on 2017/8/4.
 */

public class TestSome {

    @Test
    public void testSome2() throws Exception {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        System.out.println(System.currentTimeMillis());
        System.out.println(String.valueOf(ts.getTime()));
        System.out.println(ts.toString());
    }

}
