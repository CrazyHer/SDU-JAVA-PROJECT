package server.action;

import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取我的购买物品列表接口
    1.接收字符串：用户ID
    1.先输出一行数n，表示我的购买中有几项物品
    2.输出n行字符串：商品名称，请根据这些名称使用GetItemDetails接口逐一获取商品详情即可
 */
public class GetMyBoughtItem {
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    String userID;
    DB database = new DB();
    ResultSet resultSet;
    int n, i;

    public GetMyBoughtItem(Socket s) throws IOException, SQLException {
        socket = s;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        userID = in.readLine();
        resultSet = database.query("SELECT * FROM trade.orderlog WHERE `buyerID`='" + userID + "'");
        n = resultSet.getRow();
        i = 0;
        out.println(n);
        int[] itemID = new int[n];
        while (resultSet.next()) {
            itemID[i] = resultSet.getInt("itemID");
            i++;
        }
        for (int j : itemID) {
            resultSet = database.query("SELECT `ItemName` FROM trade.item WHERE `ItemID`= '" + j + "';");
            if (resultSet.next()) out.println(resultSet.getString("ItemName"));
        }
        database.close();
    }
}
