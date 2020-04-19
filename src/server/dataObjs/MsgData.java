package server.dataObjs;

import java.io.Serializable;

public class MsgData implements Serializable {
    String senderID, receiverID, time, text;

    public MsgData(String senderID, String receiverID, String text) {//发送消息时使用三参数的构造方法
        this.receiverID = receiverID;
        this.senderID = senderID;
        this.text = text;
        this.time = "NOW()";
    }

    public MsgData(String senderID, String receiverID, String text, String time) {//接收消息时服务器使用这个构造方法，可以获得消息的具体时间
        this.receiverID = receiverID;
        this.senderID = senderID;
        this.text = text;
        this.time = time;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }
}
