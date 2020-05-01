package client.userInfo;

import com.alibaba.fastjson.JSON;
import server.dataObjs.MsgData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.URL;

public class MyNotification extends JPanel implements ActionListener {

    String URL1 = "https://i.guancha.cn/bbs/2020/03/19/20200319110252686.jpg?imageMogr2/cut/634x634x28x28/thumbnail/60x60/format/jpg";
    String URL2 = "https://i.guancha.cn/bbs/2019/03/22/20190322174945138.png?imageMogr2/thumbnail/90x90";
    MySession mySession[] = {new MySession("张三", "201900301234", URL1, "2020-03-26", "干啥呢，吃了屎吗"), new MySession("赵四", "201900308888", URL2, "2019-12-12", "吃屎啦你")};

    JPanel p;
    JLabel label;
    JButton button;

    public MyNotification() {
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
            button.addActionListener(this);
            p.add(button, new GBC(3, 1, 1, 1).setWeight(0.1, 0.5).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE));
            add(p);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("打开聊天")) {
            //直接打开聊天模块面板
        }
    }
    private class NET_GetMessages{
        private final String Command = "GET MSGS";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出

        private BufferedReader in;
        String myID;
        MsgData[][] msgData;

        public NET_GetMessages(String myID) throws IOException {
            this.myID = myID;
            this.socket = new Socket(this.Address, this.PORT);
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
            dos.writeUTF(myID+";ALL");
            dos.flush();

            msgData = JSON.parseObject(in.readLine(),MsgData[][].class);

        }
        public MySession[] getMySessions(){
            MySession[] mySessions = new MySession[msgData.length];
            for (int i =0;i<msgData.length;i++){
                String userID = (myID.equals(msgData[i][0].getSenderID())?msgData[i][0].getReceiverID():msgData[i][0].getSenderID());

                mySessions[i] = new MySession()
            }
        }
    }
    private class NET_GetUserInfo{
        private final String Command = "GET MSGS";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出

        private BufferedReader in;
        String myID;
        MsgData[][] msgData;

        public NET_GetUserInfo(String myID) throws IOException {
            this.myID = myID;
            this.socket = new Socket(this.Address, this.PORT);
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
            dos.writeUTF(myID+";ALL");
            dos.flush();
            //....这个
            msgData = JSON.parseObject(in.readLine(),MsgData[][].class);
        }
    }

    private class MySession {
        String withWhoName, withWhoID, imageURL, body, time;
        ImageIcon image;

        MySession(String name, String ID, String imageURL, String time, String body) {
            this.withWhoName = name;
            this.withWhoID = ID;
            this.body = body;
            this.imageURL = imageURL;
            this.time = time;
            try {
                image = new ImageIcon(new URL(imageURL));
            } catch (Exception e) {
                System.out.println(e);
            }
            image.setImage(image.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
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
