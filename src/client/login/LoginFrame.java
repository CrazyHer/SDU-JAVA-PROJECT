package client.login;

import client.userInfo.UserInfoFrame;
import com.alibaba.fastjson.JSON;
import server.dataObjs.UserData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

public class LoginFrame extends JFrame implements ActionListener {

    public JLabel lbAccount;
    public JLabel lbPassword;
    public JTextField tfAccount;
    public JPasswordField passwordField;
    public JButton btLogin;
    public JRadioButton remPswd;
    public JRadioButton autoLogin;
    public JMenu menu;
    public JMenuBar menuBar;
    public JMenuItem menuItem;
    public JPanel panel;
    public ImageIcon img;
    public UserData userData;
    public String Path;

    public LoginFrame() {
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowClose());
        setTitle("登录");
        addMenu();

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        lbAccount = new JLabel("账号");
        tfAccount = new JTextField(22);
        panel.add(lbAccount);
        panel.add(tfAccount);
        p.add(panel, BorderLayout.NORTH);
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        lbPassword = new JLabel("密码");
        passwordField = new JPasswordField(22);
        panel.add(lbPassword);
        panel.add(passwordField);
        p.add(panel, BorderLayout.SOUTH);
        c.add(p);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        remPswd = new JRadioButton("记住密码");
        panel.add(remPswd);
        autoLogin = new JRadioButton("自动登录");
        panel.add(autoLogin);
        c.add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        btLogin = new JButton("登录");
        btLogin.addActionListener(this);
        panel.add(btLogin);
        c.add(panel);

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        btLogin = new JButton("注册");
        btLogin.addActionListener(this);
        panel.add(btLogin);
        c.add(panel);
    }

    public void addMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("菜单");
        menuBar.add(menu);
        menuItem = new JMenuItem("退出");
        menu.add(menuItem);
        setJMenuBar(menuBar);
        menuItem.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "登录":
                userData = new UserData(tfAccount.getText(), String.valueOf(passwordField.getPassword()));
                NET_Login net_login = null;
                try {
                    net_login = new NET_Login(userData);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "服务器的头像文件不见了！", "Oops", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                String resultCode = net_login.getResultCode();
                if (resultCode.equals("1")) {
                    JOptionPane.showMessageDialog(this, "登陆成功！");
                    try {
                        new UserInfoFrame(this).setVisible(true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (resultCode.equals("0")) JOptionPane.showMessageDialog(this, "密码错误！");
                else if (resultCode.equals("-1")) JOptionPane.showMessageDialog(this, "无此用户！");
                break;
            case "注册":
                new RegisterFrame(this).setVisible(true);
                this.setVisible(false);
                break;
            case "退出":
                new WindowClose();
                break;
            default:
                System.out.println("???");
                break;
        }
    }

    private class NET_Login {
        private final String Command = "LOGIN";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private PrintWriter out;

        private String json, resultCode;

        public NET_Login(UserData userData) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            //上面的可以照搬，只需要改一下请求类型Command即可
            /*
            注意：上面能不改就不改，因为Command只能用writeUTF发送；下面的对象传输只能用out.println()来传输JSON序列化的对象
             */
            //下面是对接操作，对象用下面的方式传就行了，不要再用ObjectOutputStream了
            json = JSON.toJSONString(userData);//使用JSON序列化对象传输过去
            out.println(json);

            this.resultCode = dis.readUTF();
            if (resultCode.equals("1")) {
                getFile("C:/Users/Public/client");
                img = new ImageIcon(ImageIO.read(new File(Path)));
            }
            this.socket.close();
        }

        public void getFile(String path) throws IOException {//接收文件的方法，直接用即可,参数为存放文件夹路径，注意是文件夹
            FileOutputStream fos;
            // 文件名
            String fileName = dis.readUTF();
            System.out.println("接收到文件" + fileName);
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
            Path = file.getAbsolutePath().replace('\\', '/');
            System.out.println(Path);
            fos = new FileOutputStream(file);
            // 开始接收文件
            byte[] bytes = new byte[1024];
            int length;
            while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                fos.write(bytes, 0, length);
                fos.flush();
            }
            System.out.println("======== 文件接收成功========");
        }


        public String getResultCode() {
            return resultCode;
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



/*
文件传输方法样板：

        private void getFile(String path) throws IOException {//接收文件的方法，直接用即可,参数为存放文件夹路径，注意是文件夹
            FileOutputStream fos;
            // 文件名
            String fileName = dis.readUTF();
            System.out.println("接收到文件"+fileName);
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
            Path = file.getAbsolutePath().replace('\\','/');    //Path是类变量，赋了文件的绝对路径
            System.out.println(Path);
            fos = new FileOutputStream(file);
            // 开始接收文件
            byte[] bytes = new byte[1024];
            int length;
            while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                fos.write(bytes, 0, length);
                fos.flush();
            }
            System.out.println("======== 文件接收成功========");
        }

        private void sendFile(String path) throws Exception {//传图方法，直接用就行，参数是文件的绝对路径
            FileInputStream fis;
            File file = new File(path);
            if (file.exists()) {
                fis = new FileInputStream(file);
                // 文件名
                dos.writeUTF(file.getName());
                dos.flush();
                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                }
                System.out.println("======== 文件传输成功 ========");
            }
        }
 */