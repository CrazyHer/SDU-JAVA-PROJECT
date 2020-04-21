package client.itemList;

import client.login.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ItemListFrame extends JFrame implements ActionListener {

    public JMenu menu;
    public JMenuBar menuBar;
    public JMenuItem menuItemLogout;
    public JMenuItem menuItemSort;
    public JTextField tfSearch;
    public JButton btSearch;
    public JPanel panel;

    public ItemListFrame() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowClose());
        setTitle("商品列表");
        addMenu();

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tfSearch = new JTextField(20);
        btSearch = new JButton("搜索");
        panel.add(tfSearch);
        panel.add(btSearch);
        c.add(panel, BorderLayout.NORTH);

        panel = new JPanel();
        //访问端口，获取商品数组，循环输出展示
        panel.add(new JLabel("2"));


    }

    public static void main(String[] args) {
        new ItemListFrame().setVisible(true);
    }

    public void addMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("菜单");
        menuBar.add(menu);
        menuItemLogout = new JMenuItem("退出登录");
        menu.add(menuItemLogout);
        menuItemSort = new JMenuItem("按时间排序");//尚未实现******************************
        menu.add(menuItemSort);
        setJMenuBar(menuBar);
        menuItemLogout.addActionListener(this);
        //menuItemSort.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(menuItemLogout)) {
            System.exit(0);
            new LoginFrame();
        }
    }

    class WindowClose extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            int i = JOptionPane.showConfirmDialog(null, "是否关闭", "提示", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                System.exit(0);
            }
        }
    }

}
