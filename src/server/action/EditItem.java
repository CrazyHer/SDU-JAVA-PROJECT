package server.action;

import com.alibaba.fastjson.JSON;
import dataObjs.ItemData;
import server.ServerMain;
import server.dataBase.DB;
import server.talkingServer.OnlineUserPool;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
修改商品接口
    1.接收字符串商品名称 writeUTF传输
    2.返回该商品带有itemID的一个ItemData对象 println JSON传输
    3.接收带有该itemID的修改后的一个ItemData对象,即把传过去的对象改一改传回来就行了 println JSON传输
    4.成功返回1，商品名与其他商品重复或失败都返回-1 writeUTF传输
    5.接收该商品的图片文件
 */
public class EditItem {
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    PrintWriter out;
    BufferedReader in;
    DB database = DB.instance;
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
            itemData = new ItemData(name, price, auction, quantity, introduction, ownerID, itemID);
            out.println(JSON.toJSONString(itemData));
            itemData = JSON.parseObject(in.readLine(), ItemData.class);
            resultSet = database.query("SELECT * FROM `trade`.`item` WHERE (`ItemName` = '" + itemData.getName() + "' AND `ItemID` != '" + itemData.getItemID() + "')");
            if (resultSet.next()) {
                dos.writeUTF("-1");
                dos.flush();
            } else {
                dos.writeUTF("1");
                dos.flush();
                getFile(ServerMain.PATH + itemData.getName());
                database.update("UPDATE `trade`.`item` SET `ItemName` = '" + itemData.getName() + "', `ItemPrice` = '" + itemData.getPrice() + "', `Introduction` = '" + itemData.getIntroduction() + "', `auction` = '" + (itemData.isAuction() ? "1" : "0") + "', `remains` = '" + itemData.getQuantity() + "', `photoPath` = '" + Path + "' WHERE (`ItemID` = '" + itemData.getItemID() + "')");
                //通知卖家
                Socket Lsocket = OnlineUserPool.getSocket(itemData.getOwnerID());
                if (Lsocket != null) {
                    out = new PrintWriter(new BufferedOutputStream(Lsocket.getOutputStream()), true);
                    out.println("NEW ITEM");
                }
                //通知有关买家
                database.query("SELECT DISTINCT `buyerID` FROM `trade`.`orderlog` WHERE `itemID`='" + itemID + "'");
                while (resultSet.next()) {
                    Lsocket = OnlineUserPool.getSocket(resultSet.getString("buyerID"));
                    if (Lsocket != null) {
                        out = new PrintWriter(new BufferedOutputStream(Lsocket.getOutputStream()), true);
                        out.println("NEW ITEM");
                    }
                }

            }
        }
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
