package client.itemList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CommentFrame extends JFrame implements ActionListener {

    public JTextArea taComment;
    public JButton btComment;
    public JPanel panel;

    public CommentFrame() {
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        setSize(400, 250);
        setLocationRelativeTo(null);
        addWindowListener(new WindowClose());
        setTitle("评论");

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
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
                System.out.println("未进行评论");
            } else {
                //上传数据库
                System.exit(0);
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

