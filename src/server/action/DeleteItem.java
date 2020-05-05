package server.action;

import server.dataBase.DB;
import server.talkingServer.OnlineUserPool;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
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
    String itemName, itemID, ownerID;
    ResultSet resultSet;

    public DeleteItem(Socket s) throws IOException, SQLException {
        socket = s;
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        itemName = dis.readUTF();
        database = new DB();
        resultSet = database.query("SELECT * FROM `trade`.`item` WHERE `ItemName`='" + itemName + "'");
        resultSet.next();
        ownerID = resultSet.getString("ownerID");
        itemID = resultSet.getString("ItemID");
        database.update("DELETE FROM `trade`.`item` WHERE (`ItemName` = '" + itemName + "')");
        PrintWriter out = new PrintWriter(new BufferedOutputStream(OnlineUserPool.getSocket(ownerID).getOutputStream()), true);
        out.println("NEW ITEM");
        database.query("SELECT DISTINCT `buyerID` FROM `trade`.`orderlog` WHERE `itemID`='" + itemID + "'");
        while (resultSet.next()) {
            Socket Lsocket = OnlineUserPool.getSocket(resultSet.getString("buyerID"));
            if (Lsocket != null) {
                out = new PrintWriter(new BufferedOutputStream(Lsocket.getOutputStream()), true);
                out.println("NEW ITEM");
            }
        }
        database.update("DELETE FROM `trade`.`orderlog` WHERE (`itemID` = '" + itemID + "')");
        database.close();
    }
}
