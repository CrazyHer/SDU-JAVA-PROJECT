package server.action;

import server.dataBase.DB;
import server.dataObjs.ItemData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
获取商品详情接口
    1.直接接收字符串：商品名称
    2.返回该物品的ItemData对象
    3.通过ImageIO返回一个图像
*/
public class GetItemDetails {
    Socket socket;
    BufferedReader in;
    ObjectOutputStream obj;
    String itemName, introduction, ownerID, photoPath;
    int quantity;
    double price;
    boolean auction;
    DB database;
    ResultSet resultSet;
    ItemData itemData = null;
    BufferedImage img;

    public GetItemDetails(Socket s) throws IOException, SQLException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        obj = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        itemName = in.readLine();
        database = new DB();
        resultSet = database.query("SELECT * FROM `trade`.`item` WHERE `ItemName`='" + itemName + "'");
        if (resultSet.next()) {
            introduction = resultSet.getString("Introduction");
            ownerID = resultSet.getString("ownerID");
            quantity = resultSet.getInt("remains");
            price = resultSet.getDouble("ItemPrice");
            auction = resultSet.getBoolean("auction");
            photoPath = resultSet.getString("photoPath");
            itemData = new ItemData(itemName, price, auction, quantity, introduction, ownerID);
            obj.writeObject(itemData);
            img = ImageIO.read(new File(photoPath));
            ImageIO.write(img, photoPath.substring(photoPath.lastIndexOf(".") + 1), socket.getOutputStream());
        }
        database.close();
    }
}
