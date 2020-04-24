package server.action;

import com.alibaba.fastjson.JSON;
import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取我发布（出售）的物品列表接口
1.接收字符串：用户ID  writerUTF传输
2.返回一个字符串数组：商品名称，请根据这些名称使用GetItemDetails接口逐一获取商品详情即可 println JSON传输
*/
public class GetMySoldItem {
    Socket socket;
    PrintWriter out;
    DataInputStream dis;
    String userID;
    DB database = new DB();
    ResultSet resultSet;
    int n;
    String[] mySoldItemList;

    public GetMySoldItem(Socket s) throws IOException, SQLException {
        socket = s;
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        userID = dis.readUTF();
        resultSet = database.query("SELECT * FROM `trade`.`item` WHERE `ownerID`='" + userID + "'");
        resultSet.last();
        n = resultSet.getRow();
        resultSet.beforeFirst();
        mySoldItemList = new String[n];
        for (int i = 0; resultSet.next(); i++) {
            mySoldItemList[i] = resultSet.getString("ItemName");
        }
        out.println(JSON.toJSONString(mySoldItemList));
        database.close();
    }
}
