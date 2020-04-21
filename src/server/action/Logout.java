package server.action;

import server.talkingServer.OnlineUserPool;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/*
    登出接口
    1.接收一行登出用户名的ID writerUTF传输
 */
public class Logout {
    Socket socket;
    DataInputStream dis;
    String userID;

    public Logout(Socket s) throws IOException {
        socket = s;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        userID = dis.readUTF();
        OnlineUserPool.delete(userID);
    }
 }
