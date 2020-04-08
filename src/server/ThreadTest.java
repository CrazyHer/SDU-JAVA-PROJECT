package server;

import server.action.Login;
import server.action.Register;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ThreadTest extends Thread{
    BufferedReader in;
    PrintWriter out;
    Socket socket;
    ThreadTest(Socket s) {
        socket=s;
        System.out.println("客户端连接："+s.getInetAddress()+":"+s.getPort());
    }
    @Override
    public void run(){
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String str = in.readLine();
            System.out.println("收到客户端指令："+str);//客户端发送一条指令，服务端接收后由action包中类响应然后关闭连接。一线程只处理一任务。因此这里只分析第一条语句，只处理一道命令。后续的收发数据由action包中的相应类完成
            switch (str){
                case "CLOSE SERVER":
                    ServerMain.closeServer();
                    break;
                case "LOGIN":
                    new Login(socket);
                    break;
                case "REGISTER":
                    new Register(socket);
                    break;
            }
            socket.close();
        }catch (IOException | ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
}
