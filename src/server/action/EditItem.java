package server.action;

import com.alibaba.fastjson.JSON;
import server.dataBase.DB;
import server.dataObjs.ItemData;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
修改商品接口
    1.接收字符串商品名称 writeUTF传输
    2.返回该商品带有itemID的一个ItemData对象 println JSON传输
    3.接收带有该itemID的修改后的一个ItemData对象,即把传过去的对象改一改传回来就行了 println JSON传输
    4.接收该商品的图片文件
    5.成功返回1，商品名与其他商品重复或失败都返回-1 writeUTF传输
 */
public class EditItem {
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    PrintWriter out;
    BufferedReader in;
    DB database;
    ItemData itemData;
    ResultSet resultSet;
    String itemName, Path;

    public EditItem(Socket s) throws IOException, SQLException {
        socket = s;
        out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        itemName = dis.readUTF();
        database = new DB();
        resultSet = database.query("SELECT * FROM `trade`.`item` WHERE (`ItemName` = '" + itemName + "')");
        if (resultSet.next()) {
            String name, introduction, ownerID, itemID;
            int quantity;
            double price;
            boolean auction;
            name = resultSet.getString("ItemName");
            introduction = resultSet.getString("Introduction");
            ownerID = resultSet.getString("ownerID");
            itemID = resultSet.getString("ItemID");
            quantity = resultSet.getInt("remains");
            price = resultSet.getDouble("ItemPrice");
            auction = resultSet.getBoolean("auction");
            itemData = new ItemData(name, price, auction, quantity, introduction, ownerID);
            itemData.setItemID(itemID);
            out.println(JSON.toJSONString(itemData));
            itemData = JSON.parseObject(in.readLine(), ItemData.class);
            resultSet = database.query("SELECT * FROM `trade`.`item` WHERE (`ItemName` = '" + itemData.getName() + "' AND `ItemID` != `" + itemData.getItemID() + "`)");
            if (resultSet.next()) {
                dos.writeUTF("-1");
                dos.flush();
            } else {
                getFile("C:/Users/Public/items/" + itemData.getName());
                database.update("UPDATE `trade`.`item` SET `ItemName` = '" + itemData.getName() + "', `ItemPrice` = '" + itemData.getPrice() + "', `Introduction` = '" + itemData.getIntroduction() + "', `auction` = '" + (itemData.isAuction() ? "1" : "0") + "', `remains` = '" + itemData.getQuantity() + "', `photoPath` = '" + Path + "' WHERE (`ItemID` = '" + itemData.getItemID() + "')");
                dos.writeUTF("1");
                dos.flush();
            }
        }
        database.close();
    }


    private void getFile(String path) throws IOException {//接收文件的方法，直接用即可,参数为存放文件夹路径，注意是文件夹
        FileOutputStream fos;
        // 文件名
        String fileName = dis.readUTF();
        System.out.println("接收到文件" + fileName);
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
        Path = file.getAbsolutePath().replace('\\', '/');    //Path是类变量，赋了文件的绝对路径
        System.out.println(Path);
        fos = new FileOutputStream(file);
        // 开始接收文件
        byte[] bytes = new byte[1024];
        int length;
        while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
            fos.write(bytes, 0, length);
            fos.flush();
        }
        System.out.println("======== 文件接收成功========");
    }
}
