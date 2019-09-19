package com.demo;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author xucong
 * @date 2019/9/19
 */
public class Client extends Thread{

    public static void main(String args[]) throws IOException {
        Socket clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress("127.0.0.1",8089));
        clientSocket.setKeepAlive(true);
        new SendThread(clientSocket).start();

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    static class SendThread extends Thread {
        Socket socket;
        PrintWriter printWriter = null;

        public SendThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String reqMessage = "Hello";
//            for (int i = 0; i < 100; i++) {
//
//            }
//            sendPacket(reqMessage);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key","value");
            Packet packet = new Packet(jsonObject,"hello".getBytes());
            while (true){
                if (!socket.isConnected()){
                    continue;
                }
                try {
                    packet.Send(socket);
                    System.out.println("数据发送成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                ThreadUtil.sleep(1000);
            }



        }

        public void sendPacket(String message) {
            // 包体内容
			byte[] contentBytes = message.getBytes();
            // 包体长度
			int contentlength = contentBytes.length;

            // 头部内容字节数组
			byte[] headbytes = BytesUtil.int2Bytes(contentlength);
            // 包=包头+包体
			byte[] bytes = new byte[headbytes.length + contentlength];
			int i = 0;
            // 包头
			for (i = 0; i < headbytes.length; i++) {
				bytes[i] = headbytes[i];
			}
            // 包体
			for (int j = i, k = 0; k < contentlength; k++, j++) {
				bytes[j] = contentBytes[k];
			}
            try {
                OutputStream writer = socket.getOutputStream();
				writer.write(bytes);
                writer.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
}
