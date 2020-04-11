package client.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame implements ActionListener {

    private static AbstractButton showPic;
    public JPanel panel;
    public JLabel lbPhoto;
    public JLabel lbName;//姓名
    public JLabel lbUserID;//学号
    public JLabel lbPassword;//密码
    public JLabel lbConfirmPassword;//确认密码
    public JTextField txName;
    public JTextField txUserID;
    public JTextField txPassword;
    public JPasswordField txConfirmPassword;
    public JButton btRegister;
    public JButton btAddPhoto;
    ImageIcon image;

    public RegisterFrame() {
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        setSize(600, 450);
        setLocationRelativeTo(null);
        addWindowListener(new WindowClose());
        setTitle("注册");

        /*try {
            image = new ImageIcon(new URL("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2605178066,625446438&fm=26&gp=0.jpg"));
        } catch (Exception e) {
            System.out.println(e);
        }
        image.setImage(image.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));*/
        JPanel fillInPanel = new JPanel();//填写注册信息面板
        fillInPanel.setLayout(new GridLayout(6, 2));
        lbPhoto = new JLabel("头像");
        lbName = new JLabel("姓名");
        lbUserID = new JLabel("学号");
        lbPassword = new JLabel("密码");
        lbConfirmPassword = new JLabel("确认密码");
        txName = new JTextField(5);
        txName.setSize(20, 10);
        txUserID = new JTextField(22);
        txPassword = new JTextField(22);
        txConfirmPassword = new JPasswordField(22);
        btAddPhoto = new JButton("插入头像");
        btRegister = new JButton("注册");
        btRegister.addActionListener(this);

        fillInPanel.add(lbPhoto);
        fillInPanel.add(btAddPhoto);
        //fillInPanel.add(new JLabel(image));
        fillInPanel.add(lbName);
        fillInPanel.add(txName);
        fillInPanel.add(lbUserID);
        fillInPanel.add(txUserID);
        fillInPanel.add(lbPassword);
        fillInPanel.add(txPassword);
        fillInPanel.add(lbConfirmPassword);
        fillInPanel.add(txConfirmPassword);

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(btRegister);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(fillInPanel, BorderLayout.CENTER);
        panel.add(p, BorderLayout.SOUTH);

        c.add(panel);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("注册")) {
            if (txConfirmPassword.getPassword().equals(txPassword.getText()) && txName.getText() != null && txUserID.getText() != null && txPassword.getText() != null) {
            /*try {
            //连接数据库,将填写的信息导入数据库
            Class.forName("com.mysql.JDBCDriver");
            Connection con;
            con= DriverManager.getConnection(dbUrl);
            con=DriverManager.getConnection(dbUrl,user,password);

             } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            }*/
                System.exit(0);
            } else if (txName.getText().isEmpty() || txUserID.getText().isEmpty() || txPassword.getText().isEmpty()) {
                System.out.println("信息不完整!");
            } else if (!(txConfirmPassword.getPassword().equals(txPassword.getText()))) {
                System.out.println("确认密码与密码不符!");
            }
        } else if (e.getActionCommand().equals("插入头像")) {
            new UpLoad().UpLoadFile("上传头像");
        }

    }


}
