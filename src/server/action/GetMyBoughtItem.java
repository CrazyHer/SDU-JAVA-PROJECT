package server.action;

import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取我的购买物品列表接口
    1.接收字符串：用户ID
    2.返回一个字符串数组：商品名称，请根据这些名称使用GetItemDetails接口逐一获取商品详情即可
 */
public class GetMyBoughtItem {
    Socket socket;
    ObjectOutputStream out;
    BufferedReader in;
    String userID;
    DB database = new DB();
    ResultSet resultSet;
    int n,m;
    String[] myBoughtItemList;

    public GetMyBoughtItem(Socket s) throws IOException, SQLException {
        socket = s;
        out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        userID = in.readLine();
        resultSet = database.query("SELECT DISTINCT * FROM trade.orderlog WHERE `buyerID`='" + userID + "'");
        n = resultSet.getRow();
        int[] itemID = new int[n];
        for(int i =0;resultSet.next();i++){
            itemID[i] = resultSet.getInt("itemID");
        }
        m=0;
        myBoughtItemList = new String[n];
        for (int j : itemID) {
            resultSet = database.query("SELECT `ItemName` FROM trade.item WHERE `ItemID`= '" + j + "';");
            if (resultSet.next()) myBoughtItemList[m++] = resultSet.getString("ItemName");
        }
        out.writeObject(myBoughtItemList);
        database.close();
    }
}
