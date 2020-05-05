package client.itemState;

import client.itemList.BoughtItemInfoFrame;
import client.itemList.ItemInfo;
import client.itemList.ReceiveItemFrame;
import client.itemList.SoldItemInfoFrame;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ItemState extends Component {

    String itemName, userID;
    String itemStateCode;

    public ItemState(String itemName, String userID) throws IOException {
        this.itemName = itemName;
        this.userID = userID;
        NET_GetItemState net_getItemState = null;
        try {
            net_getItemState = new NET_GetItemState();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "无法获取商品状态！", "Oops", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        itemStateCode = net_getItemState.getResultCode();
        if (itemStateCode.equals("0")) {
            System.out.println("正在交易中");
            new ReceiveItemFrame(new ItemInfo(itemName),userID).setVisible(true);
        } else if (itemStateCode.equals("1")) {
            System.out.println("可购买");
            new BoughtItemInfoFrame(new ItemInfo(itemName), userID).setVisible(true);
        } else if (itemStateCode.equals("2")) {
            System.out.println("自己的商品");
            new SoldItemInfoFrame(new ItemInfo(itemName)).setVisible(true);
        }
    }

    public String getItemStateCode() {
        return itemStateCode;
    }

    private class NET_GetItemState {

        private final String Command = "GET ITEM STATE";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;
        private String resultCode;
        private String json;

        public NET_GetItemState() throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();

            dos.writeUTF(userID);
            dos.flush();

            dos.writeUTF(itemName);
            dos.flush();

            this.resultCode = dis.readUTF();
            this.socket.close();
        }

        public String getResultCode() {
            return resultCode;
        }

    }

}
