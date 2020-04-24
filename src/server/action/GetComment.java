package server.action;

import com.alibaba.fastjson.JSON;
import server.dataBase.DB;
import server.dataObjs.Comment;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取商品评论接口
    1.接收字符串：商品名称,writeUTF传输
    2.返回一个Comment对象数组，println JSON传输
 */
public class GetComment {
    Socket socket;
    Comment[] comment;
    PrintWriter out;
    String itemName;
    DB database = new DB();
    ResultSet resultSet;
    DataInputStream dis;
    public GetComment(Socket s) throws IOException, SQLException {
        socket = s;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        itemName = dis.readUTF();
        resultSet = database.query("SELECT * FROM `trade`.`remark` WHERE `ItemID`=(SELECT `ItemID` FROM `trade`.`item` WHERE `ItemName`='" + itemName + "')");
        int n;
        resultSet.last();
        n = resultSet.getRow();
        resultSet.beforeFirst();
        comment = new Comment[n];
        for (int i = 0; resultSet.next(); i++) {
            comment[i] = new Comment(itemName, resultSet.getString("text"), resultSet.getString("authorID"));
        }
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        out.println(JSON.toJSONString(comment));
        database.close();
    }
}
