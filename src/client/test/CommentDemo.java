package client.test;

import client.itemList.ItemInfo;
import server.dataObjs.Comment;

import javax.swing.*;
import java.awt.*;

public class CommentDemo extends JFrame {
    public JTextArea taComment;
    public JButton btComment;
    public JPanel panel;
    ItemInfo itemInfo = null;
    private Comment comment;

    public CommentDemo() {
        Container c = getContentPane();
        c.setLayout(new FlowLayout(FlowLayout.LEFT));
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("评论");

        panel = new JPanel();
        panel.add(new JLabel("这里是商品信息"));
        c.add(panel);

        panel = new JPanel(new BorderLayout());
        taComment = new JTextArea(10, 30);
        panel.add(taComment, BorderLayout.CENTER);

        btComment = new JButton("提交评论");

        JPanel p0 = new JPanel();
        p0.add(btComment);
        panel.add(p0, BorderLayout.SOUTH);
        c.add(panel);
    }

    public static void main(String[] args) {
        CommentDemo testFrame = new CommentDemo();
        testFrame.setVisible(true);
    }
}
