package com.demo.server;

/**
 * @author xucong
 * @date 2019/9/19
 */
public interface SocketServerResponseInterface {

    /**
     * 客户端断线回调
     */
    void clientOffline();

    /**
     * 客户端上线回调
     *
     * @param clientIp
     */
    void clientOnline(String clientIp);
}
