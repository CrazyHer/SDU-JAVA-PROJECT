package client.itemList;

import com.alibaba.fastjson.JSON;
import server.dataObjs.BuyItemData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static client.userInfo.UserInfo.user;

public class BoughtItemInfoFrame extends JFrame implements ActionListener {
    public JPanel panel;
    public JButton btChat;
    public JButton btBuy;
    private BuyItemData buyItemData;
    ItemInfo itemInfo;

    public BoughtItemInfoFrame(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setTitle("商品购买信息");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.add(itemInfo);
        c.add(panel, BorderLayout.CENTER);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        btChat = new JButton("联系卖家");
        btChat.addActionListener(this);
        btBuy = new JButton("购买");
        btBuy.addActionListener(this);
        panel.add(btChat, new GBC(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER));
        panel.add(btBuy, new GBC(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER));
        panel.add(new JPanel(), new GBC(0, 2, 1, 1));
        c.add(panel, BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("购买")) {
            int i = JOptionPane.showConfirmDialog(null, "是否购买", "提示", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                buyItemData = new BuyItemData(user.getID(), itemInfo.getName());
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

            }

        } else if (e.getActionCommand().equals("联系卖家")) {
            //new聊天窗口
        }
    }

    private class NET_BuyItem {
        private final String Command = "BUY ITEM";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private PrintWriter out;

        private String json, resultCode;

        public NET_BuyItem(BuyItemData buyItemData) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
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
}
