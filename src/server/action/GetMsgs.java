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
    1.接收一行字符串。ALL代表获取所有的消息，201900301198;201900301200代表获取两个用户之间的对话，学号小的在前大的在后，中间用英文分号;分隔。 writerUTF传输
    2.返回一个MsgData二维数组，这里面是用户收到和发送的所有消息。第一个维度为聊天对象的所有消息的数组，第二个维度为具体的消息内容。即MsgData[i][j],i代表对话对象，j代表具体信息对象。MsgData[i][]表示这个发送者的所有消息对象的一维数组. println JSON传输
    获取两个用户间的对话时，第一个维度长度只为1；
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
        if (filter.equals("ALL")) {
            resultSet = database.query("SELECT * FROM trade.dialogue");
            msgData = new MsgData[resultSet.getRow()][];
            for (int i = 0; resultSet.next(); i++) {
                user1 = resultSet.getString("user1");
                user2 = resultSet.getString("user2");
                resultSet1 = database.query("SELECT * FROM trade.message WHERE (`senderID`=" + user1 + " AND `receiverID`=" + user2 + ") OR(`senderID`=" + user2 + " AND `receiverID`=" + user1 + ")");
                msgData[i] = new MsgData[resultSet1.getRow()];
                for (int j = 0; resultSet1.next(); j++) {
                    msgData[i][j] = new MsgData(resultSet1.getString("senderID"), resultSet1.getString("receiverID"), resultSet1.getString("text"), resultSet1.getString("time"));
                }
            }
        } else {
            users = filter.split(";");
            resultSet1 = database.query("SELECT * FROM trade.message WHERE (`senderID`=" + users[0] + " AND `receiverID`=" + users[1] + ") OR(`senderID`=" + users[1] + " AND `receiverID`=" + users[0] + ")");
            msgData = new MsgData[1][];
            msgData[0] = new MsgData[resultSet1.getRow()];
            for (int i = 0; resultSet1.next(); i++) {
                msgData[0][i] = new MsgData(resultSet1.getString("senderID"), resultSet1.getString("receiverID"), resultSet1.getString("text"), resultSet1.getString("time"));
            }
        }
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        out.println(JSON.toJSONString(msgData));
        database.close();
    }
}
