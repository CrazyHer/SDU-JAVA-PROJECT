package client.itemList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class SoldItemInfoFrame extends JFrame implements ActionListener {

    JPanel panel;
    JButton btEdit;
    JButton btDelete;
    JButton btAuction;
    ItemInfo itemInfo;

    public SoldItemInfoFrame(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setTitle("商品销售信息");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.add(itemInfo);
        c.add(panel, BorderLayout.CENTER);


        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        btAuction = new JButton("拍卖销售");
        btAuction.addActionListener(this);
        btEdit = new JButton("修改信息");
        btEdit.addActionListener(this);
        btDelete = new JButton("删除商品");
        btDelete.addActionListener(this);
        panel.add(btAuction, new GBC(0, 0, 1, 1).setWeight(0.8, 0.6).setAnchor(GridBagConstraints.NORTHEAST));
        panel.add(btEdit, new GBC(0, 1, 1, 1).setWeight(0.8, 0.6).setAnchor(GridBagConstraints.NORTHEAST));
        panel.add(btDelete, new GBC(0, 2, 1, 1).setWeight(0.8, 0.6).setAnchor(GridBagConstraints.NORTHEAST));
        panel.add(new JPanel(), new GBC(0, 2, 1, 1));
        c.add(panel, BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("拍卖销售")) {
            //从数据库中删除
        } else if (e.getActionCommand().equals("修改信息")) {
            new EditItemInfoFrame(itemInfo.getItemName()).setVisible(true);
        } else if (e.getActionCommand().equals("删除商品")) {
            int i = JOptionPane.showConfirmDialog(null, "是否删除该商品?", "提示", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                try {
                    new NET_DeleteItem(itemInfo.getItemName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "删除成功！", "Oops", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }

        }
    }

    private class NET_DeleteItem {
        private final String Command = "DELETE ITEM";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private PrintWriter out;

        public NET_DeleteItem(String itemName) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();

            dos.writeUTF(itemName);
            dos.flush();

            this.socket.close();
        }
    }
}
