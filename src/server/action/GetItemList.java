package server.action;

import server.dataBase.DB;
import server.dataObjs.ItemListFilter;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取所有商品列表的接口
    1.接收一个搜索过滤器对象ItemListFilter
    2.返回一个字符串数组：商品名称，请根据这些名称使用GetItemDetails接口逐一获取商品详情即可
 */
public class GetItemList {
    Socket socket;
    ObjectInputStream obj;
    ItemListFilter filter;
    DB database = new DB();
    ObjectOutputStream out;
    ResultSet resultSet;
    String[] itemList;

    public GetItemList(Socket s) throws IOException, ClassNotFoundException, SQLException {
        socket = s;
        obj = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        filter = (ItemListFilter) obj.readObject();
        if (!filter.getKeyWord().equals("*")) {
            resultSet = database.query("SELECT ItemName FROM trade.item WHERE MATCH (`ItemName`,`Introduction`) AGAINST ('" + filter.getKeyWord() + "' WITH QUERY EXPANSION);");
        } else resultSet = database.query("SELECT * FROM trade.item");

        itemList = new String[resultSet.getRow()];
        for(int i =0;resultSet.next();i++){
            itemList[i] = resultSet.getString("ItemName");
        }
        out.writeObject(itemList);
        database.close();
    }
}
