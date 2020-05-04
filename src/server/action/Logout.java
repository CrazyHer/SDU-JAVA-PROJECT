package server.action;

import server.talkingServer.OnlineUserPool;

import java.io.*;
import java.net.Socket;

/*
    登出接口
    1.接收一行登出用户名的ID writerUTF传输
    2.向登出用户的长连接发送登出讯息，println传输
 */
public class Logout {
    Socket socket;
    DataInputStream dis;
    String userID;
    PrintWriter out;

    public Logout(Socket s) throws IOException {
        socket = s;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        userID = dis.readUTF();
        out = new PrintWriter(new BufferedOutputStream(OnlineUserPool.getSocket(userID).getOutputStream()), true);
        out.println("LOGOUT");
        OnlineUserPool.delete(userID);
    }
 }
