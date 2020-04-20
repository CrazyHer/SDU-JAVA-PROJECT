package server.action;

import com.alibaba.fastjson.JSON;
import server.dataBase.DB;
import server.dataObjs.UserData;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;

/*
登录接口
    1.接收一个UserData对象，里面包含学号、密码
    除此之外，客户端还要连接聊天服务器端口，并把UserData传给聊天服务器
    2.无此用户，返回字符串-1；用户名存在，密码错误，返回字符串0；登陆成功，返回字符串1，再传输头像。
 */
public class Login {
    Socket socket;
    String passWord, ID, profilePath;
    DB dataBase;
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
        dataBase = new DB();
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
        dataBase.close();
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
        }
    }
}

/*  另一种传输头像文件的方法，比较复杂，不如ImageIO的方法简单
                //下面开始传送头像文件
                try {
                    fileInputStream = new FileInputStream(profilePath);//通过数据库中的路径创建一个读入储存在服务器端的用户头像的文件流
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());//创建输出数据流
                    int len = 0;
                    byte[] buf = new byte[1024];//字节缓冲区，read()每读一个字符，存进这里
                    while ((len = fileInputStream.read(buf, 0, buf.length)) != -1) {//一个字节一个字节传输,如果后面没有数据读了，read()就会返回-1，作为读完标识
                        dataOutputStream.write(buf, 0, len);//将缓冲区的数据输出给客户端
                        dataOutputStream.flush();
                    }

                }catch (IOException e){
                    System.out.println("传输数据异常");
                    e.printStackTrace();
                }finally {
                    fileInputStream.close();
                    dataOutputStream.close();
                }
                //头像文件传送完成
 */