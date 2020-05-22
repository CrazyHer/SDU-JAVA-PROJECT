package client.userInfo;

import client.login.LoginFrame;
import client.refreshListener.RefreshListener;
import com.alibaba.fastjson.JSON;
import dataObjs.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

import static client.ClientMain.Address;
import static client.ClientMain.PORT;

public class UserInfoFrame extends JFrame implements ActionListener {
    JScrollPane scrollPane;
    JPanel c;
    public JMenu menu;
    public JMenuBar menuBar;
    public JMenuItem menuItem;
    public LoginFrame ParentFrame;
    public UserData userData;

    UserInfo userInfoPanel;
    MyItems myItemsPanel;
    //MyStatistics myStatisticsPanel;
    MyNotification myNotificationPanel;

    public UserInfoFrame(LoginFrame parentFrame, RefreshListener refreshListener) throws IOException {
        ParentFrame = parentFrame;
        refreshListener.setUserInfoFrame(this);
        new NET_GetUserData(parentFrame.userData.getID());
        parentFrame.dispose();
        setTitle("用户信息");
        setSize(550, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowClose());
        setLocationRelativeTo(null);
        addMenu();

        c = new JPanel();
        c.setLayout(new GridBagLayout());//分别为用户信息、我的物品、统计信息、消息通知

        userInfoPanel = new UserInfo(userData, parentFrame.img);
        myItemsPanel = new MyItems(userData.getID());
        //myStatisticsPanel = new MyStatistics();
        myNotificationPanel = new MyNotification(userData.getID());
        c.add(userInfoPanel, new GBC(0, 0, 1, 1).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST));
        c.add(myItemsPanel, new GBC(0, 1, 1, 1).setWeight(1, 0.4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
        //c.add(myStatisticsPanel, new GBC(0, 2, 1, 1).setWeight(1, 0.2));
        c.add(myNotificationPanel, new GBC(0, 3, 1, 1).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));

        scrollPane = new JScrollPane(c);
        getContentPane().add(scrollPane);

        setVisible(true);
    }

    public void refreshItems() throws IOException {//定向刷新MyItems面板的方法
        myItemsPanel = new MyItems(userData.getID());
        c.removeAll();
        c.add(userInfoPanel, new GBC(0, 0, 1, 1).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST));
        c.add(myItemsPanel, new GBC(0, 1, 1, 1).setWeight(1, 0.4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
        //c.add(myStatisticsPanel, new GBC(0, 2, 1, 1).setWeight(1, 0.2));
        c.add(myNotificationPanel, new GBC(0, 3, 1, 1).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
        c.repaint();
        c.revalidate();
    }

    public void refreshNotification() throws IOException {//定向刷新Notification面板的方法
        myNotificationPanel = new MyNotification(userData.getID());
        c.removeAll();
        c.add(userInfoPanel, new GBC(0, 0, 1, 1).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST));
        c.add(myItemsPanel, new GBC(0, 1, 1, 1).setWeight(1, 0.4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
        //c.add(myStatisticsPanel, new GBC(0, 2, 1, 1).setWeight(1, 0.2));
        c.add(myNotificationPanel, new GBC(0, 3, 1, 1).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
        c.repaint();
        c.revalidate();
    }

    public void addMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("菜单");
        menuBar.add(menu);

        menuItem = new JMenuItem("退出登录");
        menu.add(menuItem);
        menuItem.addActionListener(this);

        setJMenuBar(menuBar);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("退出登录")) {
            int i = JOptionPane.showConfirmDialog(null, "是否退出当前账号?", "提示", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                try {
                    new NET_Logout(userData.getID());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "退出成功！", "Oops", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        } else if (e.getActionCommand().equals("刷新")) {
            try {
                refreshNotification();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class NET_GetUserData {

        private final String Command = "GET USER DATA";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private DataOutputStream dos;

        private String json, resultCode;

        public NET_GetUserData(String ID) throws IOException {
            this.socket = new Socket(Address, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            dos.writeUTF(Command);
            dos.flush();
            //上面的可以照搬，只需要改一下请求类型Command即可
            /*
            注意：上面能不改就不改，因为Command只能用writeUTF发送；下面的对象传输只能用out.println()来传输JSON序列化的对象
             */
            //下面是对接操作，对象用下面的方式传就行了，不要再用ObjectOutputStream了
            out.println(ID);
            userData = JSON.parseObject(in.readLine(), UserData.class);
            this.socket.close();
        }
    }

    private class NET_Logout {
        private final String Command = "LOG OUT";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;
        private String userID;

        public NET_Logout(String userID) throws IOException {
            this.userID = userID;
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();

            dos.writeUTF(this.userID);
            dos.flush();

            this.socket.close();
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
