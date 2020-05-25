package server.action;

import com.alibaba.fastjson.JSON;
import dataObjs.MsgData;
import server.dataBase.DB;
import server.talkingServer.OnlineUserPool;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    发送消息接口
    1.接收一个MsgData对象  println JSON传输
    2.发送一行字符串通知接收者  println传输
 */
public class SendAMsg {
    Socket socket;
    BufferedReader in;
    MsgData msgData;
    DB database = DB.instance;
    ResultSet resultSet;
    String user1, user2, text;

    public SendAMsg(Socket s) throws IOException, SQLException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        msgData = JSON.parseObject(in.readLine(), MsgData.class);
        user1 = msgData.getSenderID();
        user2 = msgData.getReceiverID();
        text = msgData.getText();
        resultSet = database.query("SELECT * FROM trade.dialogue WHERE (`user1` = " + user1 + " AND `user2`=" + user2 + ") OR (`user1`= " + user2 + " AND `user2`=" + user1 + ")");
        if (!resultSet.next()) {
            if (user1.compareTo(user2) < 0) {
                database.update("INSERT INTO `trade`.`dialogue` (`user1`, `user2`) VALUES ('" + user1 + "', '" + user2 + "');");
            } else {
                database.update("INSERT INTO `trade`.`dialogue` (`user1`, `user2`) VALUES ('" + user2 + "', '" + user1 + "');");
            }
        }
        database.update("INSERT INTO `trade`.`message` (`text`, `senderID`, `receiverID`, `time`) VALUES ('" + text + "', '" + user1 + "', '" + user2 + "', NOW())");
        Socket receiverSocket = OnlineUserPool.getSocket(user2);
        if (receiverSocket != null) {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(receiverSocket.getOutputStream()), true);
            out.println("NEW MSG");
            System.out.println("new msg!!");
        }
        receiverSocket = OnlineUserPool.getSocket(user1);
        if (receiverSocket != null) {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(receiverSocket.getOutputStream()), true);
            out.println("NEW MSG");
            System.out.println("new msg!!");
        }
    }
}
