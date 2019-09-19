package com.demo.client;

import cn.hutool.core.util.StrUtil;
import com.demo.Log;
import com.demo.client.interfaces.SocketClientResponseInterface;
import com.demo.client.thread.SocketClientThread;

/**
 * @author xucong
 * @date 2019/9/19
 */
public class SocketClient {

    private static final String TAG = SocketClient.class.getSimpleName();

    private SocketClientThread socketClientThread;

    public SocketClient(SocketClientResponseInterface socketClientResponseInterface) {
        socketClientThread = new SocketClientThread("socketClientThread", socketClientResponseInterface);
        socketClientThread.start();
        //ThreadPoolUtil.getInstance().addExecuteTask(socketClientThread);
    }

    public <T> void sendData(T data) {
        //convert to string or serialize object
        String s = (String) data;
        if (StrUtil.isEmpty(s)) {
            Log.i(TAG, "sendData: 消息不能为空");
            return;
        }
        if (socketClientThread != null) {
            socketClientThread.sendMsg(s);
        }
    }

    public void stopSocket() {
        //一定要在子线程内执行关闭socket等IO操作
        new Thread(() -> {
            socketClientThread.setReConnect(false);
            socketClientThread.stopThread();
        }).start();
        //ThreadPoolUtil.getInstance().addExecuteTask(() -> {
        //    socketClientThread.setReConnect(false);
        //    socketClientThread.stopThread();
        //});
        //ThreadPoolUtil.getInstance().shutdown();
    }
}
