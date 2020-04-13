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
发布商品接口
    1.接收一个ItemData对象
    2.接收字符串头像的后缀名，例如JPG
    3.再接收一个图像，BufferedImage以ImageIO传输
    4.创建成功返回1，失败返回-1
 */
public class ReleaseItem {
    Socket socket;
    DB database;
    BufferedInputStream bufferedInputStream;
    BufferedReader in;
    PrintWriter out;
    ObjectInputStream obj;
    ItemData itemData;
    BufferedImage bufferedImage;
    String typename, photoPath;
    ResultSet resultSet;

    public ReleaseItem(Socket s) throws IOException, ClassNotFoundException, SQLException {
        socket = s;
        bufferedInputStream = new BufferedInputStream(socket.getInputStream());
        obj = new ObjectInputStream(bufferedInputStream);
        in = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
        database = new DB();
        itemData = (ItemData) obj.readObject();
        typename = in.readLine();
        bufferedImage = ImageIO.read(ImageIO.createImageInputStream(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        in = new BufferedReader(new InputStreamReader(bufferedInputStream));
        photoPath = "./src/server/images/items/" + itemData.getName() + "/" + "profile." + typename;

        resultSet = database.query("SELECT * FROM trade.item WHERE ItemName=" + itemData.getName());
        if (!resultSet.next()) {
            ImageIO.write(bufferedImage, typename, new File(photoPath));
            database.update("INSERT INTO trade.item VALUES ('" + itemData.getName() + "','" + itemData.getPrice() + "','" + itemData.getIntroduction() + "'," + itemData.isAuction() + ",'" + itemData.getOwnerID() + "',NOW()," + itemData.getQuantity() + ",0,'" + photoPath + "',null)");
            out.println("1");
        } else {
            out.println("-1");
        }
        database.close();
    }
}
