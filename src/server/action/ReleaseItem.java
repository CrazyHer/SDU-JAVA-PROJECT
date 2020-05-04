package server.action;

import com.alibaba.fastjson.JSON;
import server.ServerMain;
import server.dataBase.DB;
import server.dataObjs.ItemData;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
发布商品接口
    1.接收一个ItemData对象 println JSON传输
    2.创建成功返回1，失败返回-1 writeUTF传输
    3.接收一个图像文件
 */
public class ReleaseItem {
    Socket socket;
    DB database;

    DataOutputStream dos;
    DataInputStream dis;
    BufferedReader in;

    ItemData itemData;

    String Path;
    ResultSet resultSet;

    public ReleaseItem(Socket s) throws IOException, SQLException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        itemData = JSON.parseObject(in.readLine(), ItemData.class);
        database = new DB();
        resultSet = database.query("SELECT * FROM trade.item WHERE `ItemName` = '" + itemData.getName() + "'");
        if (!resultSet.next()) {
            dos.writeUTF("1");
            dos.flush();
            getFile(ServerMain.PATH + itemData.getName());
            database.update("INSERT INTO `trade`.`item` (`ItemName`, `ItemPrice`, `Introduction`, `auction`, `ownerID`, `releaseTime`, `remains`, `sale`, `photoPath`, `ItemID`) VALUES ('" + itemData.getName() + "', '" + itemData.getPrice() + "', '" + itemData.getIntroduction() + "', '" + (itemData.isAuction() ? "0" : "1") + "', '" + itemData.getOwnerID() + "', NOW(), '" + itemData.getQuantity() + "', '0', '" + Path + "', null)");
        } else {
            dos.writeUTF("-1");
            dos.flush();
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
