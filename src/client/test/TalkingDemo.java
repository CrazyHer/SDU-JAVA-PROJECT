package client.test;

import javax.swing.*;
import java.awt.*;

public class TalkingDemo extends JFrame {

    //taSend = new JTextArea(5, 40);//实例化文本框
    JTextArea taSend = new JTextArea();//声明文本框，用来写内容
    JScrollPane spShow;//用来安置聊天记录面板
    JScrollPane spInput;//用来安置输入
    JPanel inputPanel;//安置消息输入框与发送按钮
    JPanel showPanel;//聊天记录面板
    JButton btSend;//发送按钮
    JLabel lbMe;//标签，显示自己的用户名
    JLabel lbYou;//标签，显示对方的用户名

    public TalkingDemo() {
        setTitle("聊天");
        setSize(550, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = this.getContentPane();
        c.setLayout(new BorderLayout());//设置窗体布局为BorderLayout

        inputPanel = new JPanel(new BorderLayout());
        showPanel = new JPanel(new GridLayout(1024, 1));
        spInput = new JScrollPane(inputPanel);
        spShow = new JScrollPane(showPanel);


        lbMe = new JLabel("李梓昕");
        lbMe.setForeground(Color.BLUE);
        lbMe.setFont(new Font("隶书", Font.BOLD, 24));
        lbYou = new JLabel("何大佬");
        lbYou.setForeground(Color.RED);
        lbYou.setFont(new Font("隶书", Font.BOLD, 24));


        //滚动条默认置底
        JScrollBar jscrollBar = spShow.getVerticalScrollBar();
        if (jscrollBar != null)
            jscrollBar.setValue(jscrollBar.getMaximum());

        showPanel.add(lbMe);
        showPanel.add(new JLabel("何大佬nb"));
        showPanel.add(lbYou);
        JLabel lb = new JLabel("被发现了...............................................................................................................................................................................................................................................................................................................");
        lb.setMaximumSize(new Dimension(40, 40));
        showPanel.add(lb);

        JPanel p1 = new JPanel();//安置输入框
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));//安置发送按钮
        inputPanel.add(p1, BorderLayout.CENTER);
        inputPanel.add(p2, BorderLayout.SOUTH);

        c.add(inputPanel, BorderLayout.SOUTH);//Container把pp放在窗体南边
        c.add(spShow, BorderLayout.CENTER);//Container把文本区放在中间

        btSend = new JButton("发送");

        p1.add(spInput);

        JPanel p0 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p0.add(btSend);
        p2.add(p0);

    }

    public static void main(String[] args) {
        TalkingDemo testFrame = new TalkingDemo();
        testFrame.setVisible(true);
    }
}
