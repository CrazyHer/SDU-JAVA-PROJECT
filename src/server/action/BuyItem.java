package server.action;

import com.alibaba.fastjson.JSON;
import server.dataBase.DB;
import server.dataObjs.BuyItemData;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
一般购买接口
    1.接收一个BuyItemData对象，println JSON传输
    2.购买成功返回1，失败返回-1，交易已经在进行中无法重复购买返回0；dos.writeUTF传输
 */
public class BuyItem {
    Socket socket;
    BufferedReader in;
    DataOutputStream dos;
    DB database;
    BuyItemData buyItemData;
    ResultSet resultSet;

    public BuyItem(Socket s) throws IOException, SQLException {
        this.socket = s;
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        database = new DB();
        buyItemData = JSON.parseObject(in.readLine(), BuyItemData.class);
        try {
            resultSet = database.query("SELECT * FROM `trade`.`orderlog` WHERE `ItemID`=(SELECT `ItemID` FROM `trade`.`item` WHERE `ItemName`='" + buyItemData.getItemName() + "' ) AND `buyerID`='" + buyItemData.getBuyerID() + "' AND `done`=0");
            if (!resultSet.next()) {
                String itemID = resultSet.getString("ItemID");
                database.update("INSERT INTO `trade`.`orderlog` (`itemID`, `buyerID`, `time`, `done`) VALUES (" + itemID + ", '" + buyItemData.getBuyerID() + "', NOW(), '0')");
                database.update("UPDATE `trade`.`item` SET `remains` = `remains`-1, `sale` = `sale`+1 WHERE (`ItemID` = " + itemID + ")");
                dos.writeUTF("1");
                dos.flush();
            } else {
                dos.writeUTF("0");
                dos.flush();
            }
        } catch (Exception e) {
            dos.writeUTF("-1");
            dos.flush();
            e.printStackTrace();
        } finally {
            database.close();
        }
    }
}
