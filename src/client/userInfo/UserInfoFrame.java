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

        c.setLayout(new GridLayout(4, 0));//分别为用户信息、我的物品、统计信息、消息通知
        c.add(new UserInfo());
        c.add(new MyItems());
        c.add(new MyStatistics());
        c.add(new MyNotification());

        scrollPane = new JScrollPane(c);
        getContentPane().add(scrollPane);

        setVisible(true);
    }

    public static void main(String[] args) {
        new UserInfoFrame();
    }
}
