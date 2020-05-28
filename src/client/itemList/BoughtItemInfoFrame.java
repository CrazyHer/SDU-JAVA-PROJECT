package client.itemList;

import client.ClientMain;
import client.talking.TalkingFrame;
import com.alibaba.fastjson.JSON;
import dataObjs.BuyItemData;
import dataObjs.MsgData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static client.ClientMain.Address;
import static client.ClientMain.PORT;

public class BoughtItemInfoFrame extends JFrame implements ActionListener {
    public JPanel panel;
    public JButton btChat;
    public JButton btBuy;
    private BuyItemData buyItemData;
    ItemInfo itemInfo;
    String userID;

    public BoughtItemInfoFrame(ItemInfo itemInfo, String userID) {
        this.itemInfo = itemInfo;
        this.userID = userID;
        setBg();
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setTitle("商品购买信息");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setOpaque(false);
        itemInfo.setOpaque(false);
        panel.add(itemInfo);
        c.add(panel, BorderLayout.CENTER);

        panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());

        btChat = new JButton("联系卖家");
        btChat.addActionListener(this);
        btBuy = new JButton("确定购买");
        btBuy.addActionListener(this);
        panel.add(btBuy, new GBC(0, 0, 1, 1).setWeight(0.8, 0.6).setAnchor(GridBagConstraints.NORTHEAST));
        panel.add(btChat, new GBC(0, 1, 1, 1).setWeight(0.8, 0.6).setAnchor(GridBagConstraints.NORTHEAST));
        panel.add(new JPanel(), new GBC(0, 2, 1, 1));
        c.add(panel, BorderLayout.SOUTH);

    }

    public void setBg() {
        ((JPanel) this.getContentPane()).setOpaque(false);
        ImageIcon img = new ImageIcon(ClientMain.class.getResource("bgImg/背景9.jpg"));
        img.setImage(img.getImage().getScaledInstance(400, 300, Image.SCALE_DEFAULT));
        JLabel background = new JLabel(img);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("确定购买")) {
            int i = JOptionPane.showConfirmDialog(null, "是否购买", "提示", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                buyItemData = new BuyItemData(userID, itemInfo.getItemName());
                System.out.println(userID + " " + itemInfo.getItemName());
                NET_BuyItem net_buyItem = null;
                try {
                    net_buyItem = new NET_BuyItem(buyItemData);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "数据丢失！", "Oops", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                String resultCode = net_buyItem.getResultCode();
                if (resultCode.equals("1")) JOptionPane.showMessageDialog(this, "购买成功！");
                else if (resultCode.equals("0")) JOptionPane.showMessageDialog(this, "交易已在进行中，无法购买！");
                else if (resultCode.equals("-1")) JOptionPane.showMessageDialog(this, "购买失败！");
                try {
                    new NET_SendMSG(new MsgData(userID, itemInfo.ownerID, "我已经成功购买您的 " + itemInfo.getItemName() + " 商品"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                this.dispose();
            }

        } else if (e.getActionCommand().equals("联系卖家")) {
            try {
                new TalkingFrame(userID, itemInfo.ownerID).setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "打开失败", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class NET_BuyItem {
        private final String Command = "BUY ITEM";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private PrintWriter out;

        private String json, resultCode;

        public NET_BuyItem(BuyItemData buyItemData) throws IOException {
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            json = JSON.toJSONString(buyItemData);//使用JSON序列化对象传输过去
            out.println(json);
            this.resultCode = dis.readUTF();
            this.socket.close();

        }

        public String getResultCode() {
            return resultCode;
        }
    }

    private class NET_SendMSG {
        private final String Command = "SEND A MSG";//请求类型
        MsgData[][] msgData;
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;

        public NET_SendMSG(MsgData msg) throws IOException {
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            out.println(JSON.toJSONString(msg));
            socket.close();
        }
    }
}
