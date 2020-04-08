package server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerMain{
    static int PORT = 2333;//端口号
    static ServerSocket serverSocket;
    static Socket socket;
    public static void main(String[] args) throws IOException{
            serverSocket = new ServerSocket(PORT);
            System.out.println("服务端启动，端口:"+PORT);
            while (true){
                try {
                    socket = serverSocket.accept();
                }catch (SocketException e){
                    System.out.println("收到服务器关闭指令！服务器已关闭");
                    break;
                }
                new ThreadTest(socket).start();
            }
    }
    public static void closeServer() throws IOException{//安全关闭服务器的方法
        serverSocket.close();
    }
}
