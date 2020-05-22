package server.action;

import com.alibaba.fastjson.JSON;
import dataObjs.UserData;
import server.ServerMain;
import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
注册接口
    1.先接受UserData用户信息； println JSON传输
    2.注册成功返回1，注册失败返回-1，字符串类型； writeUTF传输
    3.接收头像文件传输
 */
public class Register {
    Socket socket;


    UserData userData;
    String userName, passWord, ID, profilePath, Path;
    DB database;
    ResultSet resultSet;
    DataOutputStream dos;
    DataInputStream dis;
    BufferedReader in;

    public Register(Socket s) throws IOException, SQLException {
        socket = s;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        userData = JSON.parseObject(in.readLine(), UserData.class);
        userName = userData.getUsername();
        passWord = userData.getPassword();
        ID = userData.getID();
        database = new DB();
        resultSet = database.query("SELECT * FROM trade.user WHERE `ID` =" + ID);
        profilePath = ServerMain.PATH + ID;
        if (!resultSet.next()) {
            dos.writeUTF("1");
            dos.flush();
            getFile(profilePath);
            database.update("INSERT INTO trade.user VALUES ('" + userName + "','" + ID + "','" + passWord + "','" + Path + "')");
        } else {
            dos.writeUTF("-1");
            dos.flush();
        }
        database.close();
    }

    public void getFile(String path) throws IOException {//接收文件的方法，直接用即可,参数为存放路径
        FileOutputStream fos;
        // 文件名
        String fileName = dis.readUTF();
        System.out.println("接收到文件" + fileName);
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
        Path = file.getAbsolutePath().replace('\\', '/');
        System.out.println(Path);
        fos = new FileOutputStream(file);
        // 开始接收文件
        byte[] bytes = new byte[1024];
        int length;
        while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
            fos.write(bytes, 0, length);
            fos.flush();
        }
        System.out.println("======== 文件接收成功========");
    }
}
