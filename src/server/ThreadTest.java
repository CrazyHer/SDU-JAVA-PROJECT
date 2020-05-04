package server;

import server.action.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class ThreadTest extends Thread {
    Socket socket;

    ThreadTest(Socket s) {
        socket = s;
        System.out.println("客户端连接：" + s.getInetAddress() + ":" + s.getPort());
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String str = dataInputStream.readUTF();

            System.out.println("收到客户端指令：" + str);//客户端发送一条指令，服务端接收后由action包中类响应然后关闭连接。一线程只处理一任务。因此这里只分析第一条语句，只处理一道命令。后续的收发数据由action包中的相应类完成
            switch (str) {
                case "CLOSE SERVER":
                    ServerMain.closeServer();
                    break;
                case "LOGIN":
                    new Login(socket);
                    break;
                case "GET USER DATA":
                    new GetUserData(socket);
                    break;
                case "GET USER PROFILE":
                    new GetUserProfile(socket);
                    break;
                case "GET MSGS":
                    new GetMsgs(socket);
                    break;
                case "SEND A MSG":
                    new SendAMsg(socket);
                    break;
                case "REGISTER":
                    new Register(socket);
                    break;
                case "RELEASE ITEM":
                    new ReleaseItem(socket);
                    break;
                case "BUY ITEM":
                    new BuyItem(socket);
                    break;
                case "GET COMMENT":
                    new GetComment(socket);
                    break;
                case "GET ITEM DETAILS":
                    new GetItemDetails(socket);
                    break;
                case "GET ITEM STATE":
                    new GetItemState(socket);
                    break;
                case "GET ITEM LIST":
                    new GetItemList(socket);
                    break;
                case "DELETE ITEM":
                    new DeleteItem(socket);
                    break;
                case "EDIT ITEM":
                    new EditItem(socket);
                    break;
                case "GET MY BOUGHT ITEM":
                    new GetMyBoughtItem(socket);
                    break;
                case "GET MY SOLD ITEM":
                    new GetMySoldItem(socket);
                    break;
                case "REMARK":
                    new Remark(socket);
                    break;
                case "LOGOUT":
                    new Logout(socket);
                    break;
                default:
                    System.out.println("未知的命令，socket直接关闭");
                    break;
            }
            socket.close();
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
