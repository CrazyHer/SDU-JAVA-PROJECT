package client.itemList;

import server.dataObjs.Comment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

import static client.itemList.ReceiveItemFrame.itemInfo;
import static client.userInfo.UserInfo.user;

public class CommentFrame extends JFrame implements ActionListener {

    public JTextArea taComment;
    public JButton btComment;
    public JPanel panel;
    static final String HOST = "192.168.1.103"; //连接地址
    static final int PORT = 2333; //连接端口
    Socket socket;

    public CommentFrame() {
        Container c = getContentPane();
        c.setLayout(new FlowLayout(FlowLayout.LEFT));
        setSize(400, 450);
        setLocationRelativeTo(null);
        addWindowListener(new WindowClose());
        setTitle("评论");

        panel = new JPanel();
        panel.add(itemInfo);
        c.add(panel);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        taComment = new JTextArea(10, 30);
        panel.add(taComment);

        btComment = new JButton("提交评论");
        btComment.addActionListener(this);
        JPanel p0 = new JPanel();
        p0.add(btComment);
        panel.add(p0);
        c.add(panel);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("提交评论")) {
            if (taComment.getText().isEmpty()) {
                System.out.println("未进行评论");
            } else {
                try {
                    socket = new Socket("localhost", PORT); //创建客户端套接字
                    System.out.println("成功连接" + socket.getRemoteSocketAddress());
                    //客户端输出流，向服务器发消息
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    PrintWriter out = new PrintWriter(bw, true);//不自动刷新的话写完会阻塞
                    //out.println("LOGIN");
                    ObjectOutputStream obOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    obOut.writeObject(new Comment(itemInfo.getName(), taComment.getText(), user.getID()));
                    obOut.flush();
                    //客户端输入流，接收服务器消息
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    if (in.readLine().equals("1")) System.out.println("评论成功");
                    else if (in.readLine().equals("-1")) System.out.println("评论失败");
                    out.println("CLOSE SERVER");//发送关闭服务器指令
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    if (null != socket) {
                        try {
                            socket.close(); //断开连接
                            System.out.println("已断开连接");
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }

        }
    }

    //内部类
    class WindowClose extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

}

