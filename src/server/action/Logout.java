package server.action;

import server.talkingServer.OnlineUserPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/*
    登出接口
    1.接收一行登出用户名的ID
 */
public class Logout {
    Socket socket;
    BufferedReader in;
    String userID;

    public Logout(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        userID = in.readLine();
        OnlineUserPool.delete(userID);
    }
}
