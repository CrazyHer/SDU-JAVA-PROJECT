package client.itemList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SoldItemInfoFrame extends JFrame implements ActionListener {

    JPanel panel;
    JButton btDelete;
    JButton btAuction;

    public SoldItemInfoFrame() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setTitle("商品销售信息");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.add(new ItemInfo());
        c.add(panel, BorderLayout.CENTER);


        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        btAuction = new JButton("拍卖");
        btDelete = new JButton("删除");
        panel.add(btAuction, new GBC(0, 0, 1, 1).setWeight(0.8, 0.6).setAnchor(GridBagConstraints.NORTHEAST));
        panel.add(btDelete, new GBC(0, 1, 1, 1).setWeight(0.8, 0.6).setAnchor(GridBagConstraints.NORTHEAST));
        panel.add(new JPanel(), new GBC(0, 2, 1, 1));
        c.add(panel, BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("删除")) {
            //从数据库中删除
        } else if (e.getActionCommand().equals("拍卖")) {
            //弹出拍卖框
        }
    }

}
