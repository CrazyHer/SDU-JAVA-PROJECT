package server.action;

import com.alibaba.fastjson.JSON;
import server.dataBase.DB;
import server.dataObjs.Comment;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
/*
评论接口
    1.接收一个Comment对象 println JSON传输
    2.评论成功返回1，失败返回-1 writeUTF传输
 */

public class Remark {
    Socket socket;
    Comment comment;
    DB database;
    BufferedReader in;
    DataOutputStream dos;

    public Remark(Socket s) throws IOException, SQLException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        database = new DB();
        try {
            comment = JSON.parseObject(in.readLine(), Comment.class);
            database.update("INSERT INTO `trade`.`remark` (`ItemID`, `text`, `authorID`, `releaseTime`, `remarkID`) VALUES ((SELECT `ItemID` FROM `trade`.`item` WHERE `ItemName`='" + comment.getItemName() + "' ), '" + comment.getText() + "', '" + comment.getAuthorID() + "', NOW(), null);");
            database.update("UPDATE `trade`.`orderlog` SET `done` = '1' WHERE `itemID` = (SELECT `ItemID` FROM `trade`.`item` WHERE `ItemName` = '" + comment.getItemName() + "') AND `buyerID` = '" + comment.getAuthorID() + "'");
            dos.writeUTF("1");
            dos.flush();
        } catch (Exception e) {
            dos.writeUTF("-1");
            dos.flush();
            e.printStackTrace();
        } finally {
            database.close();
        }
    }
}
