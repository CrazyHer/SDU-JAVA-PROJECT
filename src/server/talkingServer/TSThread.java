package server.talkingServer;

import com.alibaba.fastjson.JSON;
import server.dataObjs.UserData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TSThread extends Thread {
    Socket socket;

    public TSThread(Socket s) {
        socket = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            UserData userData = JSON.parseObject(in.readLine(), UserData.class);
            OnlineUserPool.add(userData, socket);
            System.out.println("聊天服务器有长连接：" + userData.getID());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}