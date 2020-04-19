package server.action;

import server.dataBase.DB;
import server.dataObjs.UserData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
登录接口
    1.接收一个UserData对象，里面包含学号、密码
    除此之外，客户端还要连接聊天服务器端口，并把UserData传给聊天服务器
    2.无此用户，返回字符串-1；用户名存在，密码错误，返回字符串0；登陆成功，返回字符串1，再传输头像。
 */
public class Login {
    Socket socket;
    ObjectInputStream in;
    PrintWriter out;
    String userName, passWord, ID, profilePath;
    DB dataBase = new DB();
    FileInputStream fileInputStream;
    DataOutputStream dataOutputStream;

    public Login(Socket s) throws IOException, ClassNotFoundException, SQLException {
        socket = s;
        in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
        UserData user = (UserData) in.readObject();
        userName = user.getUsername();
        passWord = user.getPassword();
        ID = user.getID();
        System.out.println("用户名:" + userName + "\nID:" + passWord);
        ResultSet resultSet = dataBase.query("SELECT DISTINCT * FROM trade.user WHERE \"ID\"=" + ID);
        if (resultSet.next()) {
            if (resultSet.getString("password").equals(passWord)) {
                out.println("1");
                profilePath = resultSet.getString("profilePath");
                //使用ImageIO进行传输头像图片
                BufferedImage bufferedImage = ImageIO.read(new File(profilePath));
                ImageIO.write(bufferedImage, profilePath.substring(profilePath.lastIndexOf(".") + 1), socket.getOutputStream());

                //客户端接收图片的方法：
                /*
                BufferedImage bufferedImage = ImageIO.read(ImageIO.createImageInputStream(socket.getInputStream()));
                ImageIcon img = new ImageIcon(bufferedImage);
                 */

            } else {
                out.println("0");
            }
        } else {
            out.println("-1");
        }
        dataBase.close();
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