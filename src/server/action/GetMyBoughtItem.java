package server.action;

import com.alibaba.fastjson.JSON;
import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取我的购买物品列表接口
    1.接收字符串：用户ID writerUTF传输
    2.返回一个字符串数组：商品名称，请根据这些名称使用GetItemDetails接口逐一获取商品详情即可 println JSON传输
 */
public class GetMyBoughtItem {
    Socket socket;
    String userID;
    DB database = DB.instance;
    ResultSet resultSet;
    int n, m;
    String[] myBoughtItemList;
    DataInputStream dis;
    PrintWriter out;

    public GetMyBoughtItem(Socket s) throws IOException, SQLException {
        socket = s;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        userID = dis.readUTF();
        resultSet = database.query("SELECT DISTINCT `itemID` FROM trade.orderlog WHERE `buyerID`='" + userID + "'");
        resultSet.last();
        n = resultSet.getRow();
        resultSet.beforeFirst();
        int[] itemID = new int[n];
        for (int i = 0; resultSet.next(); i++) {
            itemID[i] = resultSet.getInt("itemID");
        }
        m = 0;
        myBoughtItemList = new String[n];
        for (int j : itemID) {
            resultSet = database.query("SELECT `ItemName` FROM trade.item WHERE `ItemID`= '" + j + "';");
            if (resultSet.next()) myBoughtItemList[m++] = resultSet.getString("ItemName");
        }
        out.println(JSON.toJSONString(myBoughtItemList));
    }
}
