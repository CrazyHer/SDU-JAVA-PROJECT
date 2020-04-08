package server.action;

import server.dataBase.DB;
import server.dataObjs.UserData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
注册接口
    1.先接受UserData用户信息；
    2.再接收字符串头像的后缀名，无.，例如JPG
    3.接收头像图形，BufferedImage以ImageIO传输
    3.注册成功返回1，注册失败返回-1，字符串类型；
 */
public class Register {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    ObjectInputStream obj;

    UserData userData;
    String userName, passWord,ID,profilePath,profileType;
    DB database;
    ResultSet resultSet;

    public Register(Socket s) throws IOException, ClassNotFoundException, SQLException {
        socket = s;
        obj = new ObjectInputStream(socket.getInputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        userData = (UserData) obj.readObject();
        userName = userData.getUsername();
        passWord = userData.getPassword();
        ID = userData.getID();
        profileType = in.readLine();
        resultSet = database.query("SELECT DISTINCT * FROM trade.user WHERE \"ID\"="+ID);
        profilePath = "./src/server/images/"+ID+"/"+"profile."+profileType;
        if(!resultSet.next()){
            database.update("INSERT INTO trade.user VALUES ('"+userName+"','"+ID+"','"+passWord+"','"+profilePath+"')");
            BufferedImage bufferedImage = ImageIO.read(ImageIO.createImageInputStream(socket.getInputStream()));
            ImageIO.write(bufferedImage,profileType,new File(profilePath));
            out.println("1");
        }else {
            out.println("-1");
        }
        database.close();
    }
}
