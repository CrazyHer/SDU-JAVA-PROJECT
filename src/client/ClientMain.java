package client;

import client.login.LoginFrame;

public class ClientMain {

    public static String Address = "localhost";
    public static final int PORT = 2333;

    public static void main(String[] args) {
        LoginFrame StartFrame = new LoginFrame();
        StartFrame.setVisible(true);
    }
}