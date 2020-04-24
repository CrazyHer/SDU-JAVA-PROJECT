package client.itemList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReceiveItemFrame extends JFrame implements ActionListener {

    JPanel panel;
    JButton btReceive;
    public static ItemInfo itemInfo;

    public ReceiveItemFrame(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setTitle("商品购买信息");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.add(itemInfo);
        c.add(panel, BorderLayout.CENTER);


        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        btReceive = new JButton("确认收货");
        panel.add(btReceive, new GBC(0, 0, 1, 1).setAnchor(GridBagConstraints.NORTHEAST));
        panel.add(new JPanel(), new GBC(0, 2, 1, 1));
        c.add(panel, BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("确认收货")) {
            new CommentFrame(itemInfo);
        }

    }
}
