package client.itemList;

import com.alibaba.fastjson.JSON;
import server.dataObjs.Comment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static client.userInfo.UserInfo.user;

public class CommentFrame extends JFrame implements ActionListener {

    public JTextArea taComment;
    public JButton btComment;
    public JPanel panel;
    private Comment comment;
    ItemInfo itemInfo;

    public CommentFrame(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
        Container c = getContentPane();
        c.setLayout(new FlowLayout(FlowLayout.LEFT));
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("评论");

        panel = new JPanel();
        panel.add(itemInfo);
        c.add(panel);

        panel = new JPanel(new BorderLayout());
        taComment = new JTextArea(10, 30);
        panel.add(taComment, BorderLayout.CENTER);

        btComment = new JButton("提交评论");
        btComment.addActionListener(this);
        JPanel p0 = new JPanel();
        p0.add(btComment);
        panel.add(p0, BorderLayout.SOUTH);
        c.add(panel);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("提交评论")) {
            if (taComment.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "未进行评论！");
            } else {
                comment = new Comment(itemInfo.getName(), taComment.getText(), user.getID());
                NET_Remark net_remark = null;
                try {
                    net_remark = new NET_Remark(comment);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "错误！", "Oops", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                String resultCode = net_remark.getResultCode();
                if (resultCode.equals("1")) JOptionPane.showMessageDialog(this, "评论成功！");
                else if (resultCode.equals("-1")) JOptionPane.showMessageDialog(this, "评论失败！");
            }

        }
    }

    private class NET_Remark {
        private final String Command = "REMARK";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private PrintWriter out;

        private String json, resultCode;

        public NET_Remark(Comment comment) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            json = JSON.toJSONString(comment);//使用JSON序列化对象传输过去
            out.println(json);
            this.resultCode = dis.readUTF();
            this.socket.close();
        }

        public String getResultCode() {
            return resultCode;
        }
    }
}

