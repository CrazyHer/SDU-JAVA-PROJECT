package client.userInfo;

import javax.swing.*;
import java.awt.*;

public class UserInfoFrame extends JFrame {
    JScrollPane scrollPane;
    JPanel c;

    public UserInfoFrame() {
        setTitle("用户信息");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        c = new JPanel();

        c.setLayout(new GridBagLayout());//分别为用户信息、我的物品、统计信息、消息通知
        c.add(new UserInfo(), new GBC(0, 0, 1, 1).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST));
        c.add(new MyItems(), new GBC(0, 1, 1, 1).setWeight(1, 0.4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
        c.add(new MyStatistics(), new GBC(0, 2, 1, 1).setWeight(1, 0.2));
        c.add(new MyNotification(), new GBC(0, 3, 1, 1).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));

        scrollPane = new JScrollPane(c);
        getContentPane().add(scrollPane);

        setVisible(true);
    }

    public static void main(String[] args) {
        new UserInfoFrame();
    }
}
