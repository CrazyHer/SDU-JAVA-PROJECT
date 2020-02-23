package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class SimpleDemo extends JFrame implements ActionListener, WindowListener {
    public Container container = this.getContentPane();
    public JTextField textField;

    public SimpleDemo() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        addWindowListener(this);//为框架注册窗口监听器
        this.addLabel("你是不是何锐的儿子？");
        this.addButton("OK");
        this.addButton("Close");
        textField = new JTextField(20);
        container.add(textField);
        setTitle("儿子鉴定器");
        addButton("拿到内容");
        setLocationRelativeTo(null);
        addButton("打开画图");
    }

    public void addLabel(String text) {
        JLabel label = new JLabel(text);
        container.add(label);
    }

    public void addButton(String text) {
        JButton jbt = new JButton(text);
        jbt.addActionListener(this);
        container.add(jbt);
    }

    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "OK":
                System.out.println("Click OK!!");
                break;
            case "Close":
                System.out.println("Click Close!!");
                System.exit(0);
                break;
            case "拿到内容":
                System.out.println(textField.getText().trim());
                textField.setText("");
                break;
            case "打开画图":
                FaceDemo faceDemo = new FaceDemo();
                faceDemo.setVisible(true);
                break;
            default:
                System.out.println("???");
                break;
        }
    }

    public void windowOpened(WindowEvent e) {
        System.out.println("windowOpened");
    }

    public void windowClosing(WindowEvent e) {
        System.out.println("windowClosing");
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
        System.out.println("windowClosed");
    }

    public void windowIconified(WindowEvent e) {
        System.out.println("windowIconified");
    }

    public void windowDeiconified(WindowEvent e) {
        System.out.println("windowDeiconified");
    }

    public void windowActivated(WindowEvent e) {
        System.out.println("windowActivated");
    }

    public void windowDeactivated(WindowEvent e) {
        System.out.println("windowDeactivated");
    }
}
