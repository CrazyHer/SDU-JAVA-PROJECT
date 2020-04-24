package client.itemList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static client.itemList.ReleaseFrame.changedPanel;

public class UpLoad implements ActionListener {
    JFrame frame = new JFrame("选择头像图片");// 框架布局
    JTabbedPane tabPane = new JTabbedPane();// 选项卡布局
    Container con = new Container();//
    JLabel label = new JLabel("选择图片");
    JTextField text = new JTextField();// 文件的路径
    JButton btChoose = new JButton("...");// 选择
    JFileChooser jfc = new JFileChooser();// 文件选择器
    JButton btSure = new JButton("确定");//确定按钮
    File f;//f为选择好的图片文件
    ImageIcon img;//头像图片
    ReleaseFrame parentFrame;

    public UpLoad(ReleaseFrame parentFrame) {
        this.parentFrame = parentFrame;
        jfc.setCurrentDirectory(new File("d://"));// 文件选择器的初始目录定为d盘

        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
        frame.setSize(280, 200);// 设定窗口大小
        frame.setContentPane(tabPane);// 设置布局
        label.setBounds(10, 35, 70, 20);
        text.setBounds(75, 35, 120, 20);
        btChoose.setBounds(210, 35, 50, 20);
        btSure.setBounds(180, 90, 60, 20);
        btChoose.addActionListener(this); // 添加事件处理
        btSure.addActionListener(this); // 添加事件处理
        con.add(label);
        con.add(text);
        con.add(btChoose);
        con.add(btSure);
        frame.setVisible(true);// 窗口可见
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 使能关闭窗口，结束程序
        tabPane.add(con);// 添加布局1

    }
    /**
     * 事件监听的方法
     */
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        // 绑定到选择文件，先择文件事件
        if (e.getSource().equals(btChoose)) {
            jfc.setFileSelectionMode(0);// 设定只能选择到文件
            int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
            if (state == 1) {
                return;// 撤销则返回
            }
            else {
                f = jfc.getSelectedFile();// f为选择到的文件
                text.setText(f.getAbsolutePath());
            }
        }
        if (e.getSource().equals(btSure) && !text.getText().isEmpty()) {
            //尝试头像缩放
            try {
                img = new ImageIcon(f.getAbsolutePath());
            } catch (Exception ex) {
                System.out.println(ex);
            }
            img.setImage(img.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
            changedPanel.removeAll();
            changedPanel.repaint();
            changedPanel.add(new JLabel(img));
            changedPanel.revalidate();
            parentFrame.Path = getPath();
            frame.dispose();
        }
    }
    public String getPath() {
        return text.getText();
    }

    public String getFileName() {
        return f.getName();
    }
}
