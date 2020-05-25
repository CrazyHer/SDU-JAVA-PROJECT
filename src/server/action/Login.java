package server.action;

import com.alibaba.fastjson.JSON;
import dataObjs.UserData;
import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;

/*
登录接口
    1.接收一个UserData对象，里面包含学号、密码 println JSON传输
    除此之外，客户端还要连接聊天服务器端口，并把UserData传给聊天服务器
    2.无此用户，返回字符串-1；用户名存在，密码错误，返回字符串0；登陆成功，返回字符串1，再传输头像。 writeUTF传输
 */
public class Login {
    Socket socket;
    String passWord, ID, profilePath;
    DB dataBase = DB.instance;
    ResultSet resultSet;


    DataInputStream dis;
    DataOutputStream dos;
    BufferedReader in;

    public Login(Socket s) throws Exception {
        socket = s;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        UserData user = JSON.parseObject(in.readLine(), UserData.class);
        ID = user.getID();
        passWord = user.getPassword();
        System.out.println("ID:" + ID + "\npassword:" + passWord);
        resultSet = dataBase.query("SELECT DISTINCT * FROM trade.user WHERE `ID`='" + ID + "'");
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

/*
文件传输方法样板：

        private void getFile(String path) throws IOException {//接收文件的方法，直接用即可,参数为存放文件夹路径，注意是文件夹
            FileOutputStream fos;
            // 文件名
            String fileName = dis.readUTF();
            System.out.println("接收到文件"+fileName);
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
            Path = file.getAbsolutePath().replace('\\','/');    //Path是类变量，赋了文件的绝对路径
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

        private void sendFile(String path) throws Exception {//传图方法，直接用就行，参数是文件的绝对路径
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
            }
        }
 */