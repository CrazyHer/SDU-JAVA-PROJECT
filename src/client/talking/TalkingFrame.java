package client.talking;

import javax.swing.*;
import java.awt.*;

public class TalkingFrame extends JFrame {

    JTextField tfSend;//声明文本框，用来写内容
    JScrollPane spShow;//声明大型文本区，用来显示聊天记录
    JPanel pp;
    JButton btSend;//发送按钮

    public TalkingFrame() {
        setTitle("聊天");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container con = this.getContentPane();
        con.setLayout(new BorderLayout());//设置窗体布局为BorderLayout

        pp = new JPanel();//安置消息输入框与发送按钮
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();

        spShow = new JScrollPane();

        pp.setLayout(new GridLayout(2, 1));//把pp设成（2，1）网格布局
        pp.add(p1);
        pp.add(p2);

        con.add(pp, BorderLayout.SOUTH);//Container把pp放在窗体南边
        con.add(spShow, BorderLayout.CENTER);//Container把文本区放在中间

        JLabel label = new JLabel("内容");

        tfSend = new JTextField(30);//实例化文本框

        btSend = new JButton("发送");

        p1.add(label);
        p1.add(tfSend);
        p2.add(btSend);

    }

    public static void main(String[] args) {
        TalkingFrame testFrame = new TalkingFrame();
        testFrame.setVisible(true);
    }

}
