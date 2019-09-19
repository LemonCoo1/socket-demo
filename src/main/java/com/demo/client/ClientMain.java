package com.demo.client;

import cn.hutool.core.thread.ThreadUtil;
import com.demo.client.interfaces.SocketClientResponseInterface;

/**
 * @author xucong
 * @date 2019/9/19
 */
public class ClientMain {
    public static void main(String[] args) {
        SocketClient socketClient = new SocketClient(new SocketClientResponseInterface() {
            @Override
            public void onSocketConnect() {
                System.out.println("连接成功");


            }

            @Override
            public void onSocketReceive(Object socketResult, int code) {
                System.out.println(socketResult.toString() + code);
            }

            @Override
            public void onSocketDisable(String msg, int code) {
                System.out.println(msg +"  "+code);
            }
        });


        while (true){

            ThreadUtil.sleep(2000);
            socketClient.sendData("hello");
        }





    }
}
