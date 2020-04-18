package client.itemList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class ReleaseFrame extends JFrame implements ActionListener {

    public JPanel panel;
    public static JPanel changedPanel;//开始存放“上传图片”按钮，后来存放商品图片
    public JLabel lbItemName;
    public JLabel lbItemQuantity;
    public JLabel lbItemPrice;
    public JLabel lbItemIntroduction;
    public JTextField txItemName;
    public JTextField txItemQuantity;
    public JTextField txItemPrice;
    public JTextArea taItemIntroduction;
    public JButton btUpload;
    public JButton btRelease;
    public String Path = "";

    public ReleaseFrame() {

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(null);
        addWindowListener(new WindowClose());
        setTitle("发布商品");

        JPanel fillInPanel = new JPanel();//填写发布信息面板
        fillInPanel.setLayout(new GridBagLayout());
        lbItemName = new JLabel("商品名称");
        lbItemQuantity = new JLabel("商品数量");
        lbItemPrice = new JLabel("商品单价");
        lbItemIntroduction = new JLabel("商品介绍");
        txItemName = new JTextField(10);
        txItemQuantity = new JTextField(10);
        txItemPrice = new JTextField(10);
        taItemIntroduction = new JTextArea(4, 20);
        btUpload = new JButton("上传图片");
        btUpload.addActionListener(this);

        fillInPanel.add(lbItemName, new GBC(0, 0, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(txItemName, new GBC(1, 0, 1, 10).setWeight(0.8, 1));
        fillInPanel.add(lbItemQuantity, new GBC(0, 2, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(txItemQuantity, new GBC(1, 2, 1, 10).setWeight(0.8, 1));
        fillInPanel.add(lbItemPrice, new GBC(0, 4, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(txItemPrice, new GBC(1, 4, 1, 10).setWeight(0.8, 1));
        fillInPanel.add(lbItemIntroduction, new GBC(0, 6, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(taItemIntroduction, new GBC(1, 6, 4, 20).setWeight(0.8, 1));
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(fillInPanel, BorderLayout.CENTER);

        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        changedPanel = new JPanel();
        changedPanel.add(btUpload);
        p.add(changedPanel);
        panel.add(p, BorderLayout.SOUTH);
        c.add(panel, BorderLayout.CENTER);

        btRelease = new JButton("发布");
        btRelease.addActionListener(this);
        panel = new JPanel();
        panel.add(btRelease);
        c.add(panel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        ReleaseFrame releaseFrame = new ReleaseFrame();
        releaseFrame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("发布")) {
            if (txItemName.getText().isEmpty() || txItemQuantity.getText().isEmpty() || txItemPrice.getText().isEmpty() || taItemIntroduction.getText().isEmpty()) {
                System.out.println("信息不完整!");
            }
            else if (Path.equals("")){
                System.out.println("为上传图片");
            }
            else {
                //上传数据库，上传商品列表
                System.exit(0);
            }

        } else if (e.getActionCommand().equals("上传图片")) {
            Path = new UpLoad().getPath();
        }
    }

    //内部类
    class WindowClose extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}



class main{
    public static void main(String[] args) {
        ReleaseFrame testFrame = new ReleaseFrame();
        testFrame.setVisible(true);

    }
}
