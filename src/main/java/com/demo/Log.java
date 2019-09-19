package com.demo;


/**
 * @author xucong
 * @date 2019/9/19
 */
public class Log {
    public static void i(String tag,String msg){
        String name = Thread.currentThread().getName();
        System.out.println(String.format(" [%s] - %s %s", name,tag,msg));
    }

    public static void i(String msg){
        i("",msg);
    }
}
