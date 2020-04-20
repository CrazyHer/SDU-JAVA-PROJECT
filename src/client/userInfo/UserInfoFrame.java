package client.userInfo;

import client.login.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInfoFrame extends JFrame implements ActionListener {
    JScrollPane scrollPane;
    JPanel c;
    public JMenu menu;
    public JMenuBar menuBar;
    public JMenuItem menuItem;

    public UserInfoFrame() {
        setTitle("用户信息");
        setSize(500, 400);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addMenu();

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

    public void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        menu = new JMenu("菜单");
        menuBar.add(menu);
        menuItem = new JMenuItem("退出登录");
        menu.add(menuItem);
        setJMenuBar(menuBar);
        menuItem.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(menuItem)) {
            setVisible(false);
            new LoginFrame();
        }
    }

    public static void main(String[] args) {
        new UserInfoFrame();
    }
}
