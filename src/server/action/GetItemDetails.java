package server.action;

import com.alibaba.fastjson.JSON;
import dataObjs.ItemData;
import server.dataBase.DB;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;

/*
获取商品详情接口
    1.直接接收字符串：商品名称 writerUTF传输
    2.返回该物品的ItemData对象 println JSON传输
    3.返回一个图像
*/
public class GetItemDetails {
    Socket socket;
    PrintWriter out;
    DataInputStream dis;
    DataOutputStream dos;
    String itemName, introduction, ownerID, photoPath;
    int quantity;
    double price;
    boolean auction;
    DB database;
    ResultSet resultSet;
    ItemData itemData;


    public GetItemDetails(Socket s) throws Exception {
        socket = s;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        itemName = dis.readUTF();
        database = new DB();
        resultSet = database.query("SELECT * FROM `trade`.`item` WHERE `ItemName`='" + itemName + "'");
        resultSet.next();
        introduction = resultSet.getString("Introduction");
            ownerID = resultSet.getString("ownerID");
            quantity = resultSet.getInt("remains");
            price = resultSet.getDouble("ItemPrice");
            auction = resultSet.getBoolean("auction");
            photoPath = resultSet.getString("photoPath");
        itemData = new ItemData(itemName, price, auction, quantity, introduction, ownerID);
        out.println(JSON.toJSONString(itemData));
        sendFile(photoPath);
        database.close();
    }

    private void sendFile(String path) throws Exception {//传图方法，直接用就行，参数是文件的绝对路径
        FileInputStream fis;
        File file = new File(path);
        if (file.exists()) {
            fis = new FileInputStream(file);
            // 文件名
            dos.writeUTF(file.getName());
            dos.flush();
            // 开始传输文件
            System.out.println("======== 开始传输文件 ========");
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();
            }
            System.out.println("======== 文件传输成功 ========");
        }
    }
}
