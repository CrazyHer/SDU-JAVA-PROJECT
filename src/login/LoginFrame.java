package login;
import test.SimpleDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    public LoginFrame(){
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        setSize(300,300);
        setLocationRelativeTo(null);
        addWindowListener(new WindowClose());
        setTitle("登陆");
        addMenu();

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        lbAccount = new JLabel("账号");
        tfAccount = new JTextField(22);
        panel.add(lbAccount);
        panel.add(tfAccount);
        p.add(panel,BorderLayout.NORTH);
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        lbPassword = new JLabel("密码");
        passwordField = new JPasswordField(22);
        panel.add(lbPassword);
        panel.add(passwordField);
        p.add(panel,BorderLayout.SOUTH);
        c.add(p);

        panel= new JPanel();
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

    public void addMenu(){
        menuBar = new JMenuBar();
        menu = new JMenu("菜单");
        menuBar.add(menu);
        menuItem = new JMenuItem("退出");
        menu.add(menuItem);
        setJMenuBar(menuBar);
        menuItem.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "登录":
                System.out.println("login:\nAccount:"+tfAccount.getText().trim()+"\nPassword:"+passwordField.getText());
                System.out.println("记住密码？"+ (remPswd.isSelected()?"true":"false") + "\n自动登录？" + (autoLogin.isSelected()?"true":"false"));
                setVisible(false);
                JFrame frame = new SimpleDemo( );
                frame.setSize(500, 400);
                frame.setVisible(true);
                break;
            case "退出":
                System.exit(0);
            default:
                System.out.println("???");
                break;
        }
    }
}

class WindowClose extends WindowAdapter{
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }
}
