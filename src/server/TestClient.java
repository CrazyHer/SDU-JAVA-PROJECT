package server;

import java.io.*;
import java.net.Socket;

public class TestClient {
    static int PORT = 2333;

    public static void main(String[] args) throws IOException {
        Socket socket;
        try {
            socket = new Socket("localhost", PORT);
            System.out.println("成功连接" + socket.getRemoteSocketAddress());
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            dos.writeUTF("CLOSE SERVER");//发送关闭服务器指令
            dos.flush();

            //    out.println("LOGIN");
            //     out.println(JSON.toJSONString(new UserData("多线程测试","密码")));

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
