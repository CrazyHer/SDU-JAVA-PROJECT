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
    2.输出一行数n，表示将显示n个物品
    3.输出n行字符串：商品名称，请根据这些名称使用GetItemDetails接口逐一获取商品详情即可
 */
public class GetItemList {
    Socket socket;
    ObjectInputStream obj;
    ItemListFilter filter;
    DB database = new DB();
    PrintWriter out;
    ResultSet resultSet;

    public GetItemList(Socket s) throws IOException, ClassNotFoundException, SQLException {
        socket = s;
        obj = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        filter = (ItemListFilter) obj.readObject();
        if (!filter.getKeyWord().equals("*")) {
            resultSet = database.query("SELECT ItemName FROM trade.item WHERE MATCH (`ItemName`,`Introduction`) AGAINST ('" + filter.getKeyWord() + "' WITH QUERY EXPANSION);");
        } else resultSet = database.query("SELECT * FROM trade.item");
        out.println(resultSet.getRow());
        while (resultSet.next()) {
            out.println(resultSet.getString("ItemName"));
        }
        database.close();
    }
}
