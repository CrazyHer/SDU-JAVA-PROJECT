package client.talking;

import javax.swing.*;
import java.awt.*;

public class TalkingFrame extends JFrame {

    JTextField tsend;//在外部声明文本框，用来写留言
    JTextArea ta;//声明大型文本区，用来显示聊天记录

    public TalkingFrame() {
        setTitle("聊天");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container con = this.getContentPane();
        con.setLayout(new BorderLayout());//设置窗体布局为BorderLayout

        JPanel pp = new JPanel();
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();

        ta = new JTextArea();//实例化大型文本区
        ta.disable();//设置成只读属性

        pp.setLayout(new GridLayout(2, 1));//把pp设成（2，1）网格布局
        pp.add(p1);
        pp.add(p2);

        con.add(pp, BorderLayout.SOUTH);//Container把pp放在窗体北边
        con.add(ta, BorderLayout.CENTER);//Container把文本区放在中间

        JLabel l1 = new JLabel("内容");

        tsend = new JTextField(30);//实例化文本框

        JButton bSend = new JButton("发送");

        p1.add(l1);
        p1.add(tsend);
        p2.add(bSend);

    }

    public static void main(String[] args) {
        TalkingFrame testFrame = new TalkingFrame();
        testFrame.setVisible(true);
    }

}
