package client.login;

import client.ClientMain;
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

public class RegisterFrame extends JFrame implements ActionListener {

    //private static AbstractButton showPic;
    public JPanel panel;
    public static JPanel changedPanel;//开始时存放“插入头像”按钮，后来存放头像图片
    public JPanel fillInPanel;
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
    public String Path;
    public String FileName;

    public JFrame ParentFrame;

    public RegisterFrame(JFrame parentFrame) {
        this.ParentFrame = parentFrame;
        setBg();
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new Window(this));
        setTitle("注册");

        /*try {
            image = new ImageIcon(new URL("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2605178066,625446438&fm=26&gp=0.jpg"));
        } catch (Exception e) {
            System.out.println(e);
        }
        image.setImage(image.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));*/
        fillInPanel = new JPanel();//填写注册信息面板
        fillInPanel.setOpaque(false);
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
        btAddPhoto.addActionListener(this);
        btRegister = new JButton("注册");
        btRegister.addActionListener(this);

        fillInPanel.add(lbPhoto);
        changedPanel = new JPanel();
        changedPanel.setOpaque(false);
        changedPanel.add(btAddPhoto);
        fillInPanel.add(changedPanel);
        fillInPanel.add(lbName);
        fillInPanel.add(txName);
        fillInPanel.add(lbUserID);
        fillInPanel.add(txUserID);
        fillInPanel.add(lbPassword);
        fillInPanel.add(txPassword);
        fillInPanel.add(lbConfirmPassword);
        fillInPanel.add(txConfirmPassword);

        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(btRegister);
        panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.add(fillInPanel, BorderLayout.CENTER);
        panel.add(p, BorderLayout.SOUTH);

        c.add(panel);

    }

    public void setBg() {
        ((JPanel) this.getContentPane()).setOpaque(false);
        ImageIcon img = new ImageIcon(ClientMain.class.getResource("bgImg/背景7.jpg"));
        img.setImage(img.getImage().getScaledInstance(500, 450, Image.SCALE_DEFAULT));
        JLabel background = new JLabel(img);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("注册")) {
            if (String.valueOf(txConfirmPassword.getPassword()).equals(txPassword.getText()) && txName.getText() != null && txUserID.getText() != null && txPassword.getText() != null) {
                try {
                    NET_Register net_register = new NET_Register(new UserData(txName.getText(), txUserID.getText(), txPassword.getText()), Path);
                    if (net_register.getResultCode().equals("1")) {
                        JOptionPane.showMessageDialog(this, "注册成功！");
                        //this.setVisible(false);
                        this.dispose();
                        ParentFrame.setVisible(true);
                    } else JOptionPane.showMessageDialog(this, "注册失败");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

            } else if (txName.getText().isEmpty() || txUserID.getText().isEmpty() || txPassword.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "信息不完整！");
            } else if (!(String.valueOf(txConfirmPassword.getPassword()).equals(txPassword.getText()))) {
                JOptionPane.showMessageDialog(this, "确认密码与密码不符!");
            }
        } else if (e.getSource().equals(btAddPhoto)) {
            new UpLoad(this);
        }
    }

    private class Window extends WindowAdapter {
        private RegisterFrame frame;

        Window(RegisterFrame registerFrame) {
            frame = registerFrame;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            frame.dispose();
            frame.ParentFrame.setVisible(true);
        }
    }

    private class NET_Register {
        private final String Command = "REGISTER";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataOutputStream dos;
        private DataInputStream dis;
        private PrintWriter out;//输出

        private String json, resultCode;

        public NET_Register(UserData userData, String path) throws Exception {
            this.socket = new Socket(Address, PORT);
            dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
            dos.writeUTF(Command);
            dos.flush();
            //上面的照搬，只需要改一下请求类型Command即可
            //下面是对接操作，对象用下面的方式传就行了，不要再用ObjectOutputStream了
            json = JSON.toJSONString(userData);//使用JSON序列化对象传输过去
            this.out.println(json);
            resultCode = dis.readUTF();

            sendFile(path);

            this.socket.close();
        }

        private void sendFile(String path) throws Exception {//传图方法，直接用就行
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

        public String getResultCode() {
            return resultCode;
        }
    }
}