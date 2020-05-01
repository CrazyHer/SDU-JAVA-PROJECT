package server.action;
/*
获取用户头像接口
    1.接收字符串，用户ID，writeUTF传输
    2.发送用户头像图片
 */


import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetUserProfile {
    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;
    String userID;
    DB database;
    ResultSet resultSet;


    public GetUserProfile(Socket s) throws IOException, SQLException {
        socket =s ;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        userID = dis.readUTF();
        database = new DB();
        resultSet = database.query("SELECT DISTINCT * FROM trade.user WHERE `ID`='" + userID + "'");
        if (resultSet.next()) {
            if (resultSet.getString("password").equals(passWord)) {
                dos.writeUTF("1");
                dos.flush();
                profilePath = resultSet.getString("profilePath");

                sendFile(profilePath);

            } else {
                dos.writeUTF("0");
                dos.flush();
            }
        } else {
            dos.writeUTF("-1");
            dos.flush();
        }
        dataBase.close();
    }
}
