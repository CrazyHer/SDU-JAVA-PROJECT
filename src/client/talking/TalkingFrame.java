package client.talking;

import client.refreshListener.RefreshListener;
import com.alibaba.fastjson.JSON;
import dataObjs.MsgData;
import dataObjs.UserData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static client.ClientMain.Address;
import static client.ClientMain.PORT;

public class TalkingFrame extends JFrame implements ActionListener {

    JTextArea taSend;//声明文本框，用来写内容
    JScrollPane spShow;//用来安置聊天记录面板
    //JScrollPane spInput;//用来安置输入
    JPanel pp;//安置消息输入框与发送按钮
    JPanel talkPanel;//聊天记录面板
    JButton btSend;//发送按钮
    JLabel label;

    private String myID, friendID;

    public TalkingFrame(String myID, String friendID) throws IOException {
        this.myID = myID;
        this.friendID = friendID;
        setTitle("聊天");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());//设置窗体布局为BorderLayout

        pp = new JPanel();
        talkPanel = new JPanel(new GridLayout(1024, 1));
        JPanel p1 = new JPanel();//安置输入框
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));//安置发送按钮
        //下面初始化离线消息
        MsgData[][] msgData = new NET_GetMSG(myID, friendID).getMsgData();
        String myName = new NET_GetUserData(myID).getUserData().getUsername();
        String friendName = new NET_GetUserData(friendID).getUserData().getUsername();
        for (int i = 0; i < msgData[0].length; i++) {
            if (msgData[0][i].getSenderID().equals(myID)) {
                label = new JLabel(myName + " " + msgData[0][i].getTime());
                label.setForeground(new Color(51, 204, 82));
            } else {
                label = new JLabel(friendName + " " + msgData[0][i].getTime());
                label.setForeground(Color.BLUE);
            }
            label.setFont(new Font("隶书", Font.BOLD, 15));
            talkPanel.add(label);
            talkPanel.add(new JLabel(msgData[0][i].getText()));
        }

        spShow = new JScrollPane(talkPanel);
        //滚动条默认置底
        JScrollBar jscrollBar = spShow.getVerticalScrollBar();
        if (jscrollBar != null)
            jscrollBar.setValue(jscrollBar.getMaximum());

        pp.setLayout(new FlowLayout(FlowLayout.LEFT));
        pp.add(p1);
        pp.add(p2);

        c.add(pp, BorderLayout.SOUTH);//Container把pp放在窗体南边
        c.add(spShow, BorderLayout.CENTER);//Container把文本区放在中间

        taSend = new JTextArea(5, 40);//实例化文本框

        btSend = new JButton("发送");
        btSend.addActionListener(this);

        p1.add(taSend);
        p2.add(btSend);
        if (RefreshListener.talkingFrame != null) {
            RefreshListener.talkingFrame.dispose();
        }
        RefreshListener.setTalkingFrame(this);
    }

    private void send(String text) throws IOException {
        new NET_SendMSG(new MsgData(myID, friendID, text));
    }

    public void refresh() throws IOException {//更新目前消息
        MsgData[][] msgData = new NET_GetMSG(myID, friendID).getMsgData();
        String myName = new NET_GetUserData(myID).getUserData().getUsername();
        String friendName = new NET_GetUserData(friendID).getUserData().getUsername();
        talkPanel.removeAll();
        for (int i = 0; i < msgData[0].length; i++) {
            if (msgData[0][i].getSenderID().equals(myID)) {
                label = new JLabel(myName + " " + msgData[0][i].getTime());
                label.setForeground(new Color(51, 204, 82));
                label.setFont(new Font("隶书", Font.BOLD, 15));
                talkPanel.add(label);
                talkPanel.add(new JLabel(msgData[0][i].getText()));
            } else {
                label = new JLabel(friendName + " " + msgData[0][i].getTime());
                label.setForeground(Color.BLUE);
                label.setFont(new Font("隶书", Font.BOLD, 15));
                talkPanel.add(label);
                talkPanel.add(new JLabel(msgData[0][i].getText()));
            }
        }
        talkPanel.repaint();
        talkPanel.revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(btSend)) {
            try {
                this.send(taSend.getText());
                taSend.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "发送失败", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }


    private class NET_SendMSG {
        private final String Command = "SEND A MSG";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;
        MsgData[][] msgData;

        public NET_SendMSG(MsgData msg) throws IOException {
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            out.println(JSON.toJSONString(msg));
            socket.close();
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

        private UserData userData;

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

        public UserData getUserData() {
            return userData;
        }
    }

    private class NET_GetMSG {
        private final String Command = "GET MSGS";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        MsgData[][] msgData;
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;

        public NET_GetMSG(String myID, String youID) throws IOException {
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dos.writeUTF(Command);
            dos.flush();

            dos.writeUTF((myID.compareTo(youID) > 0 ? youID + ";" + myID : myID + ";" + youID));
            dos.flush();
            msgData = JSON.parseObject(in.readLine(), MsgData[][].class);
            this.socket.close();
        }

        public MsgData[][] getMsgData() {
            return msgData;
        }
    }
}
