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

public class GetUserProfile {
    Socket socket;
    DataOutputStream dos;
    DataInputStream dis;
    String userID;
    DB database;
    ResultSet resultSet;
    String profileURL;


    public GetUserProfile(Socket s) throws Exception {
        socket = s;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        userID = dis.readUTF();
        System.out.println("a a a a " + userID);
        database = new DB();
        resultSet = database.query("SELECT * FROM `trade`.`user` WHERE `ID`='" + userID + "'");
        if (resultSet.next()) {
            profileURL = resultSet.getString("profilePath");
            sendFile(profileURL);
        }
        database.close();
    }

    private void sendFile(String path) throws Exception {//传图方法，直接用就行
        FileInputStream fis;
        File file = new File(path);
        if (file.exists()) {
            fis = new FileInputStream(file);
            // 文件名
            dos.writeUTF(file.getName());
            dos.flush();
            // 开始传输文件
            System.out.println("======== 开始传输文件 ========");
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();
            }
            System.out.println("======== 文件传输成功 ========");
        } else System.out.println("文件不存在");
    }
}
