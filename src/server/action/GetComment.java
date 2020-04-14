package server.action;

import server.dataBase.DB;
import server.dataObjs.Comment;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取商品评论接口
    1.接收字符串：商品名称
    2.返回一个Comment对象数组
 */
public class GetComment {
    Socket socket;
    Comment[] comment;
    BufferedReader in;
    String itemName;
    DB database = new DB();
    ResultSet resultSet;
    ObjectOutputStream obj;
    public GetComment(Socket s) throws IOException, SQLException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
        itemName = in.readLine();
        resultSet = database.query("SELECT * FROM `trade`.`remark` WHERE `ItemID`=(SELECT `ItemID` FROM `trade`.`item` WHERE `ItemName`='"+itemName+"')");
        comment = new Comment[resultSet.getRow()];
        for (int i =0;resultSet.next();i++){
            comment[i] = new Comment(itemName,resultSet.getString("text"),resultSet.getString("authorID"));
        }
        obj = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        obj.writeObject(comment);
        database.close();
    }
}
