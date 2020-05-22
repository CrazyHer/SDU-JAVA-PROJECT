package client.userInfo;

import client.talking.TalkingFrame;
import com.alibaba.fastjson.JSON;
import dataObjs.MsgData;
import dataObjs.UserData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static client.ClientMain.Address;
import static client.ClientMain.PORT;

public class MyNotification extends JPanel implements ActionListener {

    MySession[] mySession;
    String myID;
    JPanel p;
    JLabel label;
    JButton button;

    public MyNotification(String myID) throws IOException {
        this.myID = myID;
        mySession = new NET_GetMessages(myID).getMySessions();
        setLayout(new GridLayout(0, 1, 20, 20));
        add(new JLabel("聊天消息"));
        for (int i = 0; i < mySession.length; i++) {
            p = new JPanel();
            p.setLayout(new GridBagLayout());
            label = new JLabel(mySession[i].getImageIcon());
            p.add(label, new GBC(0, 0, 2, 1).setWeight(0.1, 1).setAnchor(GridBagConstraints.WEST));
            label = new JLabel(mySession[i].getName());
            label.setFont(new Font("微软雅黑", Font.BOLD, 16));
            p.add(label, new GBC(1, 0, 1, 3).setWeight(0.6, 0.5).setAnchor(GridBagConstraints.WEST));
            label = new JLabel(mySession[i].getBody());
            p.add(label, new GBC(1, 1, 1, 2).setWeight(0.6, 0.5).setAnchor(GridBagConstraints.WEST));

            button = new JButton("打开聊天");
            button.setActionCommand(mySession[i].getID());
            button.addActionListener(this);
            p.add(button, new GBC(3, 1, 1, 1).setWeight(0.1, 0.5).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE));
            add(p);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            try {
                new TalkingFrame(myID, e.getActionCommand()).setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "打开失败", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class NET_GetMessages {
        private final String Command = "GET MSGS";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出

        private BufferedReader in;
        String myID;
        MsgData[][] msgData;

        public NET_GetMessages(String myID) throws IOException {//获取对话消息通知
            this.myID = myID;
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dos.writeUTF(Command);
            dos.flush();
            //上面的可以照搬，只需要改一下请求类型Command即可
            /*
            注意：上面能不改就不改，因为Command只能用writeUTF发送；下面的对象传输只能用out.println()来传输JSON序列化的对象
             */
            //下面是对接操作，对象用下面的方式传就行了，不要再用ObjectOutputStream了
            dos.writeUTF(myID + ";ALL");
            dos.flush();

            msgData = JSON.parseObject(in.readLine(), MsgData[][].class);
            socket.close();
        }

        public MySession[] getMySessions() throws IOException {
            MySession[] mySessions = new MySession[msgData.length];
            for (int i = 0; i < msgData.length; i++) {
                String userID = (myID.equals(msgData[i][0].getSenderID()) ? msgData[i][0].getReceiverID() : msgData[i][0].getSenderID());
                String name = new NET_GetUserInfo(userID).getUserName();
                ImageIcon img = new NET_GetUserProfile(userID).getUserProfile();
                String time = msgData[i][msgData[i].length - 1].getTime();
                String body = msgData[i][msgData[i].length - 1].getText();
                mySessions[i] = new MySession(name, userID, img, time, body);
            }
            return mySessions;
        }
    }

    private class NET_GetUserInfo {//获取对话用户的用户名
        private final String Command = "GET USER DATA";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private PrintWriter out;

        private BufferedReader in;
        private UserData userData;


        public NET_GetUserInfo(String userID) throws IOException {
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            //上面的可以照搬，只需要改一下请求类型Command即可
            /*
            注意：上面能不改就不改，因为Command只能用writeUTF发送；下面的对象传输只能用out.println()来传输JSON序列化的对象
             */
            //下面是对接操作，对象用下面的方式传就行了，不要再用ObjectOutputStream了
            out.println(userID);
            userData = JSON.parseObject(in.readLine(), UserData.class);
            socket.close();
        }

        public String getUserName() {
            return userData.getUsername();
        }
    }

    private class NET_GetUserProfile {//获取对话用户的头像
        private final String Command = "GET USER PROFILE";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出

        private String Path;
        private ImageIcon img;

        public NET_GetUserProfile(String userID) throws IOException {
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            dos.writeUTF(Command);
            dos.flush();
            //上面的可以照搬，只需要改一下请求类型Command即可
            /*
            注意：上面能不改就不改，因为Command只能用writeUTF发送；下面的对象传输只能用out.println()来传输JSON序列化的对象
             */
            //下面是对接操作，对象用下面的方式传就行了，不要再用ObjectOutputStream了
            dos.writeUTF(userID);
            dos.flush();
            getFile("C:/Users/Public/client");
            img = new ImageIcon(ImageIO.read(new File(Path)));
            socket.close();
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

        public ImageIcon getUserProfile() {
            return img;
        }
    }

    private class MySession {//每一栏对话信息的载体
        String withWhoName, withWhoID, body, time;
        ImageIcon image;

        MySession(String name, String ID, ImageIcon img, String time, String body) {
            this.withWhoName = name;
            this.withWhoID = ID;
            this.body = body;
            this.time = time;
            this.image = img;
            this.image.setImage(image.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        }

        String getBody() {
            return body;
        }

        String getName() {
            return withWhoName;
        }

        String getID() {
            return withWhoID;
        }

        String getTime() {
            return time;
        }

        ImageIcon getImageIcon() {
            return image;
        }
    }
}
