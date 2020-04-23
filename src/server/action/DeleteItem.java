package server.action;

import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

/*
删除商品接口
    1.接收商品名称 writeUTF传输
 */
public class DeleteItem {
    DataOutputStream dos;
    DataInputStream dis;
    Socket socket;
    DB database;
    String itemName;

    public DeleteItem(Socket s) throws IOException, SQLException {
        socket = s;
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        itemName = dis.readUTF();
        database = new DB();
        database.update("DELETE FROM `trade`.`item` WHERE (`ItemName` = '" + itemName + "')");
        database.close();
    }
}
