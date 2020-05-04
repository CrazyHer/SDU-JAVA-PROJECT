package client.talking;

import javax.swing.*;
import java.awt.*;

public class TalkingFrame extends JFrame {

    JTextArea taSend;//声明文本框，用来写内容
    JScrollPane sp;//用来安置聊天记录面板
    JPanel pp;//安置消息输入框与发送按钮
    JPanel talkPanel;//聊天记录面板
    JButton btSend;//发送按钮
    JLabel lbMe;//标签，显示自己的用户名
    JLabel lbYou;//标签，显示对方的用户名

    public TalkingFrame() {
        setTitle("聊天");
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());//设置窗体布局为BorderLayout

        pp = new JPanel();
        talkPanel = new JPanel(new GridLayout(1024, 1));
        JPanel p1 = new JPanel();//安置输入框
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));//安置发送按钮
        lbMe = new JLabel("李梓昕");
        lbMe.setForeground(Color.BLUE);
        lbMe.setFont(new Font("隶书", Font.BOLD, 24));
        lbYou = new JLabel("何大佬");
        lbYou.setForeground(Color.RED);
        lbYou.setFont(new Font("隶书", Font.BOLD, 24));

        sp = new JScrollPane(talkPanel);
        //滚动条默认置底
        JScrollBar jscrollBar = sp.getVerticalScrollBar();
        if (jscrollBar != null)
            jscrollBar.setValue(jscrollBar.getMaximum());

        talkPanel.add(lbMe);
        talkPanel.add(new JLabel("何大佬nb"));
        talkPanel.add(lbYou);
        talkPanel.add(new JLabel("被发现了"));

        pp.setLayout(new FlowLayout(FlowLayout.LEFT));
        pp.add(p1);
        pp.add(p2);

        c.add(pp, BorderLayout.SOUTH);//Container把pp放在窗体南边
        c.add(sp, BorderLayout.CENTER);//Container把文本区放在中间

        taSend = new JTextArea(5, 40);//实例化文本框

        btSend = new JButton("发送");

        p1.add(taSend);
        p2.add(btSend);

    }

    public static void main(String[] args) {
        TalkingFrame testFrame = new TalkingFrame();
        testFrame.setVisible(true);
    }

}
