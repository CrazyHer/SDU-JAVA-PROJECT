package server.action;

import com.alibaba.fastjson.JSON;
import dataObjs.ItemListFilter;
import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取所有商品列表的接口
    1.接收一个搜索过滤器对象ItemListFilter println JSON传输
    2.返回一个字符串数组：商品名称，请根据这些名称使用GetItemDetails接口逐一获取商品详情即可 println JSON传输
 */
public class GetItemList {
    Socket socket;

    ItemListFilter filter;
    DB database = DB.instance;
    PrintWriter out;
    BufferedReader in;

    ResultSet resultSet;
    String[] itemList;
    String OderTYPE;

    public GetItemList(Socket s) throws IOException, SQLException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        filter = JSON.parseObject(in.readLine(), ItemListFilter.class);
        switch (filter.getODER_TYPE()) {
            case 1:
                OderTYPE = "sale";
                break;
            case 2:
                OderTYPE = "ItemPrice";
                break;
            default:
                OderTYPE = "ItemID";
                break;
        }
        if (!filter.getKeyWord().equals("*")) {
            resultSet = database.query("SELECT ItemName FROM trade.item WHERE MATCH (`ItemName`) AGAINST ('" + filter.getKeyWord() + "' WITH QUERY EXPANSION) ORDER BY `" + OderTYPE + "`;");
        } else resultSet = database.query("SELECT * FROM trade.item ORDER BY `" + OderTYPE + "`");
        resultSet.last();
        int n = resultSet.getRow();
        resultSet.beforeFirst();
        itemList = new String[n];
        for (int i = 0; resultSet.next(); i++) {
            itemList[i] = resultSet.getString("ItemName");
        }
        out.println(JSON.toJSONString(itemList));
        System.out.println(JSON.toJSONString(itemList));
    }
}
