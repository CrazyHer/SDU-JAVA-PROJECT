package server.action;

import server.dataBase.DB;
import server.dataObjs.Comment;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
/*
评论接口
    1.接收一个Comment对象
    2.评论成功返回1，失败返回-1
 */

public class Remark {
    Socket socket;
    ObjectInputStream obj;
    Comment comment;
    DB database;
    PrintWriter out;

    public Remark(Socket s) throws IOException, SQLException {
        socket = s;
        obj = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
        database = new DB();
        try {
            comment = (Comment) obj.readObject();
            database.update("INSERT INTO `trade`.`remark` (`ItemID`, `text`, `authorID`, `releaseTime`, `remarkID`) VALUES ((SELECT `ItemID` FROM `trade`.`item` WHERE `ItemName`='" + comment.getItemName() + "' ), '" + comment.getText() + "', '" + comment.getAuthorID() + "', NOW(), null);");
            out.println("1");
        } catch (Exception e) {
            out.println("-1");
            e.printStackTrace();
        } finally {
            database.close();
        }
    }
}
