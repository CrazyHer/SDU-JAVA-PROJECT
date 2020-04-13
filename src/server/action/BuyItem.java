package server.action;

import server.dataBase.DB;
import server.dataObjs.BuyItemData;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
一般购买接口
    1.接收一个BuyItemData对象
    2.购买成功返回1，失败返回-1，交易已经在进行中无法重复购买返回0；
 */
public class BuyItem {
    Socket socket;
    ObjectInputStream obj;
    PrintWriter out;
    DB database;
    BuyItemData buyItemData;
    ResultSet resultSet;

    public BuyItem(Socket s) throws IOException, ClassNotFoundException, SQLException {
        this.socket = s;
        obj = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        database = new DB();
        buyItemData = (BuyItemData) obj.readObject();
        try {
            resultSet = database.query("SELECT * FROM `trade`.`orderlog` WHERE `ItemID`=(SELECT `ItemID` FROM `trade`.`item` WHERE `ItemName`='" + buyItemData.getItemName() + "' ) AND `buyerID`='" + buyItemData.getBuyerID() + "' AND `done`=0");
            if (!resultSet.next()) {
                String itemID = resultSet.getString("ItemID");
                database.update("INSERT INTO `trade`.`orderlog` (`itemID`, `buyerID`, `time`, `done`) VALUES (" + itemID + ", '" + buyItemData.getBuyerID() + "', NOW(), '0')");
                database.update("UPDATE `trade`.`item` SET `remains` = `remains`-1, `sale` = `sale`+1 WHERE (`ItemID` = " + itemID + ")");
                out.println("1");
            } else out.println("0");
        } catch (Exception e) {
            out.println("-1");
            e.printStackTrace();
        } finally {
            database.close();
        }
    }
}
