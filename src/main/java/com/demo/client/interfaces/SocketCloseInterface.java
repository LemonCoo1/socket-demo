package com.demo.client.interfaces;

/**
 * @author xucong
 * @date 2019/9/19
 */
public interface SocketCloseInterface {
    /**
     * 客户端收到服务端消息回调
     */
    void onSocketShutdownInput();

    /**
     * 客户端关闭回调
     */
    void onSocketDisconnection();
}
