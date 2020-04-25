package server.action;

import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取物品交易状态接口
    1.接收用户ID writeUTF传输
    2.接收物品名称 writeUTF传输
    3.返回物品交易状态：0:正在交易中 1可购买 2是自己出售的物品 writeUTF传输
 */
public class GetItemState {
    DataInputStream dis;
    DataOutputStream dos;
    DB database;
    Socket socket;
    String userID, itemName;
    ResultSet resultSet;

    public GetItemState(Socket s) throws IOException, SQLException {
        socket = s;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        userID = dis.readUTF();
        itemName = dis.readUTF();
        database = new DB();
        resultSet = database.query("SELECT * FROM `trade`.`orderlog` WHERE `buyerID`= '" + userID + "' AND `itemID`= (SELECT `ItemID` FROM `trade`.`item` WHERE `ItemName` = '" + itemName + "')");
        if (resultSet.next()) {
            int state = resultSet.getInt("done");
            dos.writeUTF("" + state);
        } else {
            resultSet = database.query("SELECT * FROM `trade`.`item` WHERE `ownerID` = '" + userID + "' AND `ItemName` = '" + itemName + "'");
            if (resultSet.next()) {
                dos.writeUTF("2");
            } else dos.writeUTF("1");
        }
        database.close();
    }
}