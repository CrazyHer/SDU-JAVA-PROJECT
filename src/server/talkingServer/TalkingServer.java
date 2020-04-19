package server.talkingServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TalkingServer extends Thread {
    static ServerSocket serverSocket;
    static Socket socket;

    public TalkingServer(int PORT) throws IOException {
        serverSocket = new ServerSocket(PORT + 1);
        System.out.println("聊天服务器已启动，端口：" + (PORT + 1));
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                socket = serverSocket.accept();
                new TSThread(socket).start();
            } catch (Exception e) {
                System.out.println("聊天服务器已退出");
                break;
            }
        }
    }
}
