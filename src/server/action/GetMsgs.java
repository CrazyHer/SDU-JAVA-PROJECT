package server.action;

import com.alibaba.fastjson.JSON;
import server.dataBase.DB;
import server.dataObjs.MsgData;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
在聊天模块中，对用户保持长连接。我应该另外建立一个用于聊天的服务器和新端口
一旦用户登录，OnlineUserPool存放在线用户信息以及对应的socket，保持连接
发送消息后，服务器先进行数据库操作，然后再看这个用户是否在线
若在线，则直接向接收者发出信号（最好是发送者的ID）让对应用户获取新消息
若不在线，就什么也不做
因此对于发送者来说，只管发送
服务器只提醒接收者
因此对于一个用户，一个长连接socket用于接收信号，另外的发送消息、获取消息只需要瞬时请求即可

获取聊天消息接口
    1.接收字符串，writeUTF传输，格式为 学号;学号 或 学号;ALL。前者表示获取两者之间的对话，后者表示获取该用户的对话列表的所有对话
    2.返回一个MsgData二维数组。前者的第一个维度为1，第二维度表示具体的消息。后者第一个维度为聊天对象的数量，第二个维度为具体的消息。
 */
public class GetMsgs {
    Socket socket;
    DB database = new DB();
    ResultSet resultSet, resultSet1;
    MsgData[][] msgData;
    String filter, user1, user2;
    String[] users;
    PrintWriter out;
    DataInputStream dis;


    public GetMsgs(Socket s) throws IOException, SQLException {
        this.socket = s;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        filter = dis.readUTF();
        users = filter.split(";");
        if (users[1].equals("ALL")) {
            resultSet = database.query("SELECT * FROM trade.dialogue WHERE `user1`='"+users[0]+"' OR `user2`='"+users[0]+"'");
            int n;
            resultSet.last();
            n = resultSet.getRow();
            resultSet.beforeFirst();
            msgData = new MsgData[n][];
            for (int i = 0; resultSet.next(); i++) {
                user1 = resultSet.getString("user1");
                user2 = resultSet.getString("user2");
                resultSet1 = database.query("SELECT * FROM trade.message WHERE (`senderID`=" + user1 + " AND `receiverID`=" + user2 + ") OR(`senderID`=" + user2 + " AND `receiverID`=" + user1 + ")");
                resultSet1.last();
                int m = resultSet1.getRow();
                resultSet1.beforeFirst();
                msgData[i] = new MsgData[m];
                for (int j = 0; resultSet1.next(); j++) {
                    msgData[i][j] = new MsgData(resultSet1.getString("senderID"), resultSet1.getString("receiverID"), resultSet1.getString("text"), resultSet1.getString("time"));
                }
            }
        } else {
            resultSet1 = database.query("SELECT * FROM trade.message WHERE (`senderID`=" + users[0] + " AND `receiverID`=" + users[1] + ") OR(`senderID`=" + users[1] + " AND `receiverID`=" + users[0] + ")");
            msgData = new MsgData[1][];
            resultSet1.last();
            int n = resultSet1.getRow();
            resultSet1.beforeFirst();
            msgData[0] = new MsgData[n];
            for (int i = 0; resultSet1.next(); i++) {
                msgData[0][i] = new MsgData(resultSet1.getString("senderID"), resultSet1.getString("receiverID"), resultSet1.getString("text"), resultSet1.getString("time"));
            }
        }
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        out.println(JSON.toJSONString(msgData));
        database.close();
    }
}
