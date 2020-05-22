package client.itemList;

import client.talking.TalkingFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ReceiveItemFrame extends JFrame implements ActionListener {

    JPanel panel;
    JButton btReceive;
    JButton btChat;
    public static ItemInfo itemInfo;
    String userID;

    public ReceiveItemFrame(ItemInfo itemInfo, String userID) {
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

        btReceive = new JButton("确认收货");
        btReceive.addActionListener(this);
        panel.add(btReceive, new GBC(0, 0, 1, 1).setWeight(0.8, 0.6).setAnchor(GridBagConstraints.NORTHEAST));
        btChat = new JButton("联系卖家");
        btChat.addActionListener(this);
        panel.add(btChat, new GBC(0, 1, 1, 1).setWeight(0.8, 0.6).setAnchor(GridBagConstraints.NORTHEAST));
        panel.add(new JPanel(), new GBC(0, 2, 1, 1));
        c.add(panel, BorderLayout.SOUTH);

    }

    public void setBg() {
        ((JPanel) this.getContentPane()).setOpaque(false);
        ImageIcon img = new ImageIcon("./src/client/bgImg/背景9.jpg");
        img.setImage(img.getImage().getScaledInstance(400, 300, Image.SCALE_DEFAULT));
        JLabel background = new JLabel(img);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("确认收货")) {
            new CommentFrame(itemInfo, userID).setVisible(true);
        } else if (e.getActionCommand().equals("联系卖家")) {
            try {
                new TalkingFrame(userID, itemInfo.ownerID).setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "打开失败", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

    }
}
