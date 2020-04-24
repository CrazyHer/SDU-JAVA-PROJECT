package client.userInfo;

import client.login.LoginFrame;
import com.alibaba.fastjson.JSON;
import server.dataObjs.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class UserInfoFrame extends JFrame implements ActionListener {
    JScrollPane scrollPane;
    JPanel c;
    public JMenu menu;
    public JMenuBar menuBar;
    public JMenuItem menuItem;
    public LoginFrame ParentFrame;
    public ImageIcon Profile;
    UserData userData;

    public UserInfoFrame(LoginFrame parentFrame) throws IOException {
        ParentFrame = parentFrame;
        new NET_GetUserData(parentFrame.userData.getID());
        parentFrame.setVisible(false);
        setTitle("用户信息");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addMenu();

        c = new JPanel();

        c.setLayout(new GridBagLayout());//分别为用户信息、我的物品、统计信息、消息通知
        c.add(new UserInfo(userData, parentFrame.img), new GBC(0, 0, 1, 1).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST));
        c.add(new MyItems(userData.getID()), new GBC(0, 1, 1, 1).setWeight(1, 0.4).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.BOTH));
        c.add(new MyStatistics(), new GBC(0, 2, 1, 1).setWeight(1, 0.2));
        c.add(new MyNotification(), new GBC(0, 3, 1, 1).setWeight(1, 0.2).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));

        scrollPane = new JScrollPane(c);
        getContentPane().add(scrollPane);

        setVisible(true);
    }

    public void addMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("菜单");
        menuBar.add(menu);
        menuItem = new JMenuItem("退出登录");
        menu.add(menuItem);
        setJMenuBar(menuBar);
        menuItem.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(menuItem)) {
            //setVisible(false);
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }

    private class NET_GetUserData {

        private final String Command = "GET USER DATA";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private DataOutputStream dos;

        private String json, resultCode;

        public NET_GetUserData(String ID) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
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
}
