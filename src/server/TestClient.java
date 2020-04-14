package server;

import java.io.*;
import java.net.Socket;

public class TestClient {
    static int PORT = 2333;

    public static void main(String[] args) {
        Socket socket;
        try {
            socket = new Socket("localhost", PORT);
            System.out.println("成功连接" + socket.getRemoteSocketAddress());
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);//不自动刷新的话写完会阻塞
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println("CLOSE SERVER");//发送关闭服务器指令
            //out.println("LOGIN");

//            ObjectOutputStream obOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//            obOut.writeObject(new LoginData("何锐","201900301198","666666"));
//            obOut.flush();


            socket.close();
            System.out.println("连接已退出");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
