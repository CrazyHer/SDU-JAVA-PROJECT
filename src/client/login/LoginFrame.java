package client.login;

import client.userInfo.UserInfoFrame;

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
    static final int PORT=2333; //连接端口
    Socket socket;

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
                System.out.println("client.client.test.test.login:\nAccount:" + tfAccount.getText().trim() + "\nPassword:" + passwordField.getText());
                System.out.println("记住密码？" + (remPswd.isSelected() ? "true" : "false") + "\n自动登录？" + (autoLogin.isSelected() ? "true" : "false"));
                try {
                    socket=new Socket("localhost",PORT); //创建客户端套接字
                    System.out.println("成功连接" + socket.getRemoteSocketAddress());
                    //客户端输出流，向服务器发消息
                    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    PrintWriter out = new PrintWriter(bw, true);//不自动刷新的话写完会阻塞
                    //客户端输入流，接收服务器消息
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //out.println("LOGIN");

                    ObjectOutputStream obOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    //obOut.writeObject(new LoginData("何锐","201900301198","666666"));
                    obOut.writeObject(new LoginData(tfAccount.getText(),passwordField.getText()));
                    obOut.flush();
                    out.println("CLOSE SERVER");//发送关闭服务器指令
                } catch (IOException e1) {
                    e1.printStackTrace();
                }finally{
                    if(null!=socket){try {
                        socket.close(); //断开连接
                        System.out.println("已断开连接");
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }}
                }
                setVisible(false);
                JFrame frame = new UserInfoFrame();
                frame.setVisible(true);
                break;
            case "退出":
                System.exit(0);
            default:
                System.out.println("???");
                break;
        }
    }
    class LoginData{
        public LoginData(String name, String ID, String password){
        }
    }
}

class WindowClose extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
        int i = JOptionPane.showConfirmDialog(null,"是否关闭","提示",JOptionPane.YES_NO_OPTION);
        if(i==0){
            System.exit(0);
        }
    }
}
