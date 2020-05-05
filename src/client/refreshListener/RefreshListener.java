package client.refreshListener;

import client.talking.TalkingFrame;
import client.userInfo.UserInfoFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RefreshListener extends Thread {//通过监听长连接中收到的数据来对客户端GUI界面刷新
    public static TalkingFrame talkingFrame;
    public UserInfoFrame userInfoFrame;
    Socket socket;

    public RefreshListener(Socket s) {
        super();
        socket = s;
    }

    public static void setTalkingFrame(TalkingFrame frame) {
        talkingFrame = frame;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setUserInfoFrame(UserInfoFrame frame) {
        userInfoFrame = frame;
    }

    @Override
    public void run() {
        super.run();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                switch (in.readLine()) {
                    case "NEW MSG":
                        if (userInfoFrame != null) userInfoFrame.refreshNotification();
                        if (talkingFrame != null) talkingFrame.refresh();
                        break;
                    case "NEW ITEM":
                        if (userInfoFrame != null) userInfoFrame.refreshItems();
                        break;
                    case "LOGOUT":
                        socket.close();
                        return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
