package server.talkingServer;

import server.dataObjs.UserData;

import java.net.Socket;
import java.util.Vector;

public class OnlineUserPool {
    static Vector<OnlineUser> onlineUsers = new Vector<>();

    static public void add(UserData userData, Socket socket) {
        onlineUsers.add(new OnlineUser(userData, socket));
        System.out.println("添加成功");
    }

    static public void delete(UserData userData) {
        for (int i = 0; i < onlineUsers.size(); i++) {
            if (onlineUsers.elementAt(i).userData.getID().equals(userData.getID())) {
                onlineUsers.removeElementAt(i);
                break;
            }
        }
    }

    static public void delete(String userID) {
        for (int i = 0; i < onlineUsers.size(); i++) {
            if (onlineUsers.elementAt(i).userData.getID().equals(userID)) {
                onlineUsers.removeElementAt(i);
                break;
            }
        }
    }

    static public Socket getSocket(UserData userData) {
        Socket socket = null;
        for (int i = 0; i < onlineUsers.size(); i++) {
            if (onlineUsers.elementAt(i).userData.getID().equals(userData.getID())) {
                socket = onlineUsers.elementAt(i).socket;
                break;
            }
        }
        return socket;
    }

    static public Socket getSocket(String userID) {
        Socket socket = null;
        for (int i = 0; i < onlineUsers.size(); i++) {
            System.out.println(userID);
            if (onlineUsers.elementAt(i).userData.getID().equals(userID)) {
                socket = onlineUsers.elementAt(i).socket;
                break;
            } else System.out.println("没找到");
        }
        return socket;
    }

    private static class OnlineUser {
        UserData userData;
        Socket socket;

        OnlineUser(UserData userData, Socket socket) {
            this.userData = userData;
            this.socket = socket;
        }
    }
}
