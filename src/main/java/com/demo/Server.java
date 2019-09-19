package com.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xucong
 * @date 2019/9/19
 */
public class Server extends Thread {
    public static void main(String args[]) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("127.0.0.1",8089));
            System.out.println("服务器已启动");
            while (true) {
                Socket socket = serverSocket.accept();
                new ReceiveThread(socket).start();

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    static class ReceiveThread extends Thread {
        public static final int PACKET_HEAD_LENGTH=4;//包头长度
        public static final int PACKET_BODY_LENGTH=4;//包体长度
        private Socket socket;
        private volatile byte[] bytes = new byte[0];

        public ReceiveThread(Socket socket) {
            this.socket = socket;
        }

        public byte[] mergebyte(byte[] a, byte[] b, int begin, int end) {
            byte[] add = new byte[a.length + end - begin];
            int i = 0;
            for (i = 0; i < a.length; i++) {
                add[i] = a[i];
            }
            for (int k = begin; k < end; k++, i++) {
                add[i] = b[k];
            }
            return add;
        }

        @Override
        public void run() {
            int count =0;
            while (true) {
                try {
                    InputStream reader = socket.getInputStream();

                    int headlength = 0;
                    if (bytes.length < PACKET_HEAD_LENGTH) {
                        byte[] head = new byte[PACKET_HEAD_LENGTH];
                        int couter = reader.read(head);
                        if (couter < 0) {
                            continue;
                        }
                        bytes = mergebyte(bytes, head, 0, couter);
                        if (couter < PACKET_HEAD_LENGTH) {
                            continue;
                        }
                        headlength = BytesUtil.bytes2Int(head);

                        System.out.println("包头长度："+headlength);
                    }
                    int bodylength = 0;
                    if (bytes.length < PACKET_HEAD_LENGTH + PACKET_BODY_LENGTH) {
                        byte[] body = new byte[PACKET_BODY_LENGTH];
                        int couter = reader.read(body);
                        if (couter < 0) {
                            continue;
                        }
                        bytes = mergebyte(bytes, body, 0, couter);
                        if (couter < PACKET_BODY_LENGTH) {
                            continue;
                        }
                        bodylength = BytesUtil.bytes2Int(body);
                        System.out.println("包体长度："+bodylength);
                    }

                    if (bytes.length - 8  < headlength) {//不够一个包
                        byte[] head = new byte[headlength];//剩下应该读的字节(凑一个包)
                        int couter = reader.read(head);
                        if (couter < 0) {
                            continue;
                        }
                        bytes = mergebyte(bytes, head, 0, couter);
                        if (couter < head.length) {
                            continue;
                        }
                        System.out.println("包头内容："+ new String(head));
                    }

                    if (bytes.length - 8 - headlength  < bodylength) {//不够一个包
                        byte[] body = new byte[bodylength];//剩下应该读的字节(凑一个包)
                        int couter = reader.read(body);
                        if (couter < 0) {
                            continue;
                        }
                        bytes = mergebyte(bytes, body, 0, couter);
                        if (couter < body.length) {
                            continue;
                        }
                        System.out.println("包体内容："+ new String(body));
                    }

                    bytes = new byte[0];
                    System.out.println();


//                    if (bytes.length < PACKET_HEAD_LENGTH) {
//                        byte[] head = new byte[PACKET_HEAD_LENGTH - bytes.length];
//                        int couter = reader.read(head);
//                        if (couter < 0) {
//                            continue;
//                        }
//                        bytes = mergebyte(bytes, head, 0, couter);
//                        if (couter < PACKET_HEAD_LENGTH) {
//                            continue;
//                        }
//                    }
//                    // 下面这个值请注意，一定要取2长度的字节子数组作为报文长度，你懂得
//                    byte[] temp = new byte[0];
//                    temp = mergebyte(temp, bytes, 0, PACKET_HEAD_LENGTH);
//                    int bodylength = BytesUtil.bytes2Int(temp);//包体长度
//                    if (bytes.length - PACKET_HEAD_LENGTH < bodylength) {//不够一个包
//                        byte[] body = new byte[bodylength + PACKET_HEAD_LENGTH - bytes.length];//剩下应该读的字节(凑一个包)
//                        int couter = reader.read(body);
//                        if (couter < 0) {
//                            continue;
//                        }
//                        bytes = mergebyte(bytes, body, 0, couter);
//                        if (couter < body.length) {
//                            continue;
//                        }
//                    }
//                    byte[] body = new byte[0];
//                    body = mergebyte(body, bytes, PACKET_HEAD_LENGTH, bytes.length);
//                    count++;
//                    System.out.println("server receive body:  " + count+new String(body));
//                    bytes = new byte[0];
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }
}
