package server.action;

import com.alibaba.fastjson.JSON;
import dataObjs.UserData;
import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取用户信息接口
    1.接收一行字符串：学号，println 传输
    2.返回UserData对象 printlnJSON传输
 */
public class GetUserData {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String userID;
    DB database = DB.instance;
    ResultSet resultSet;
    UserData userData;

    public GetUserData(Socket s) throws IOException, SQLException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
        userID = in.readLine();
        resultSet = database.query("SELECT * FROM `trade`.`user` WHERE `ID`=" + userID + "");
        if (resultSet.next()) {
            userData = new UserData(resultSet.getString("username"), resultSet.getString("ID"), null);
            out.println(JSON.toJSONString(userData));
        }
    }
}
