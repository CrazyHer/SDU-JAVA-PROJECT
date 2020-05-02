package client.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RepaintDemo extends JFrame implements ActionListener {
    JPanel panel = new JPanel(new GridLayout(4, 2));

    public RepaintDemo() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("商品列表");
        JButton bt = new JButton("重绘");
        JButton bt2 = new JButton("清空");
        bt.addActionListener(this);
        bt2.addActionListener(this);
        JPanel p = new JPanel();
        p.add(bt);
        p.add(bt2);
        c.add(p, BorderLayout.SOUTH);

        //sp.setViewportView(panel);
        panel.add(new JLabel("测试一"));
        JScrollPane sp = new JScrollPane(panel);
        c.add(sp, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new RepaintDemo().setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("重绘")) {
            panel.repaint();
            panel.add(new JLabel("测试二"));
            panel.revalidate();
        } else if (e.getActionCommand().equals("清空")) {
            //清空面板测试===============================================
            panel.removeAll();
            panel.repaint();
            panel.add(new JLabel("清空成功"));
            panel.revalidate();

        }
    }
}
