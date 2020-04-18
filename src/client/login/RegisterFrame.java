package client.login;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.net.Socket;



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
    static final int PORT=2333; //连接端口
    Socket socket;
    ImageIcon image;

    public RegisterFrame() {
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowClose());
        setTitle("注册");

        /*try {
            image = new ImageIcon(new URL("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2605178066,625446438&fm=26&gp=0.jpg"));
        } catch (Exception e) {
            System.out.println(e);
        }
        image.setImage(image.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));*/
        fillInPanel = new JPanel();//填写注册信息面板
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
        changedPanel=new JPanel();
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
                //new Link().send(new LoginData(txName.getText(), txPassword.getText(), txUserID.getText()));
                //new Link().send(FileName.substring(FileName.lastIndexOf(".") + 1));
                String suffix = FileName.substring(FileName.lastIndexOf(".") + 1);//后缀名
                try {
                    socket=new Socket("localhost",PORT); //创建客户端套接字
                    System.out.println("成功连接" + socket.getRemoteSocketAddress());
                    //客户端输出流，向服务器发消息
                    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    PrintWriter out = new PrintWriter(bw, true);//不自动刷新的话写完会阻塞
                    //客户端输入流，接收服务器消息
                    //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //out.println("LOGIN");
                    ObjectOutputStream obOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    //发送用户信息
                    obOut.writeObject(new RegisterData(txName.getText(), txUserID.getText(), txPassword.getText()));
                    obOut.flush();
                    //发送头像图片文件后缀名
                    out.println(suffix);
                    BufferedImage bufferedImaged = ImageIO.read(new File(Path));
                    ImageIO.write(bufferedImaged, suffix, socket.getOutputStream());
                    //客户端输入流，接收服务器消息
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    if(in.readLine().equals("1")){ System.out.println("注册成功");}
                    else if(in.readLine().equals("-1")){ System.out.println("注册失败");}
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


                System.exit(0);
            } else if (txName.getText().isEmpty() || txUserID.getText().isEmpty() || txPassword.getText().isEmpty()) {
                System.out.println("信息不完整!");
            } else if (!(txConfirmPassword.getPassword().equals(txPassword.getText()))) {
                System.out.println("确认密码与密码不符!");
            }
        }
        else if (e.getSource().equals(btAddPhoto)) {
            UpLoad temp = new UpLoad();
            Path = temp.getPath();
            FileName = temp.getFileName();
        }

    }
    class RegisterData{
        public RegisterData(String name, String ID, String password){
        }
    }

}
