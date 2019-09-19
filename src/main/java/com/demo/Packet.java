package com.demo;

import cn.hutool.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author xucong
 * @date 2019/9/19
 */
public class Packet {
    private int Type;
    private int HeadLength;
    private int BodyLength;
    private byte[] Head;
    private byte[] Body;
    private JSONObject HeadObject;


    public Packet(JSONObject headObject, byte[] Body) {

        Body=Body==null?"Null Body".getBytes():Body;

        this.HeadObject = headObject;
        this.Head=headObject.toString().getBytes();
        this.HeadLength=headObject.toString().getBytes().length;

        this.Body=Body;
        this.BodyLength=Body.length;
    }

    //读取数据流
    public Packet(InputStream inputStream) throws Exception {
        byte[] bytesReadHeadLength=new byte[4];
        byte[] bytesReadBodyLength=new byte[4];

        //读取Head长度
        int hl=0,hlSize=4;
        while (hl<hlSize){
            int aa=inputStream.read(bytesReadHeadLength,hl,hlSize-hl);

            if (aa==-1){
                break;
            }

            if (aa==0){
                Thread.sleep(10);
            }

            hl+=aa;
        }


        //读取数据报正文长度
        int bl=0,blSize=4;

        while (bl<blSize){
            int aa=inputStream.read(bytesReadBodyLength,bl,blSize-bl);
            if (aa==-1){
                break;
            }

            if (aa==0){
                Thread.sleep(10);
            }
            bl+=aa;
        }


        this.Head=new byte[BytesUtil.bytes2Int(bytesReadHeadLength)];
        this.Body=new byte[BytesUtil.bytes2Int(bytesReadBodyLength)];


        //读取Head
        try {
            int h=0,hSize= BytesUtil.bytes2Int(bytesReadHeadLength);
            while (h<hSize){
                int aa=inputStream.read(Head,h,hSize-h);

                if (aa==-1){
                    break;
                }
                if (aa==0){
                    Thread.sleep(10);
                }
                h+=aa;
            }

            int b=0,bSize=BytesUtil.bytes2Int(bytesReadBodyLength);

            while (b<bSize){
                int aa=inputStream.read(Body,b,bSize-b);

                if (aa==-1){
                    break;
                }
                if (aa==0){
                    Thread.sleep(10);
                }
                b+=aa;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        try{
            this.HeadObject= new JSONObject(new String(this.Head));
            //System.out.println(HeadObject==null?"HeadObject为空":this.HeadObject.toJSONString());
            if (HeadObject==null){
                throw new Exception();
            }
        }catch (Exception e){
            System.out.println("转换JSONObject异常");
            throw e;
        }

    }


    //发送数据包
    public boolean Send(Socket socket) throws IOException {

        OutputStream outputStream=socket.getOutputStream();

        //发送Head长度
        outputStream.write(BytesUtil.int2Bytes(HeadLength));
        //发送Body长度
        outputStream.write(BytesUtil.int2Bytes(BodyLength));
        //发送Head
        outputStream.write(Head);
        //发送Body
        outputStream.write(Body);
        //刷新缓存推送数据
        outputStream.flush();
        //System.out.println("转发完毕");
        return true;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getHeadLength() {
        return HeadLength;
    }

    public void setHeadLength(int headLength) {
        HeadLength = headLength;
    }

    public int getBodyLength() {
        return BodyLength;
    }

    public void setBodyLength(int bodyLength) {
        BodyLength = bodyLength;
    }

    public byte[] getHead() {
        return Head;
    }

    public void setHead(byte[] head) {
        Head = head;
    }

    public byte[] getBody() {
        return Body;
    }

    public void setBody(byte[] body) {
        Body = body;
    }

    public JSONObject getHeadObject() {
        return HeadObject;
    }

    public void setHeadObject(JSONObject headObject) {
        HeadObject = headObject;
    }
}
