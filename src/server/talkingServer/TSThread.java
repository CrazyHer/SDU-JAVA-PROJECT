package server.talkingServer;

import server.dataObjs.UserData;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class TSThread extends Thread {
    Socket socket;

    public TSThread(Socket s) {
        socket = s;
    }

    @Override
    public void run() {
        System.out.println("聊天服务器有新连接");
        try {
            ObjectInputStream obj = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            UserData userData = (UserData) obj.readObject();
            OnlineUserPool.add(userData, socket);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}