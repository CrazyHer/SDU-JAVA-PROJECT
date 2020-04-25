package client.itemList;

import com.alibaba.fastjson.JSON;
import server.dataObjs.ItemData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class EditItemInfoFrame extends JFrame implements ActionListener {

    public JPanel panel;
    public JPanel changedPanel;//开始存放“上传图片”按钮，后来存放商品图片
    public JLabel lbExplain;
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
    public ItemData itemData;

    public EditItemInfoFrame() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("修改商品信息");

        JPanel fillInPanel = new JPanel();//填写发布信息面板
        fillInPanel.setLayout(new GridBagLayout());
        lbExplain = new JLabel("说明：填写要修改的信息,并长传商品图片(如果不换图片,请上传原来图片)");
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

        fillInPanel.add(lbExplain, new GBC(0, 0, 1, 2).setWeight(0.8, 1));
        fillInPanel.add(lbItemName, new GBC(0, 2, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(txItemName, new GBC(1, 2, 1, 10).setWeight(0.8, 1));
        fillInPanel.add(lbItemQuantity, new GBC(0, 4, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(txItemQuantity, new GBC(1, 4, 1, 10).setWeight(0.8, 1));
        fillInPanel.add(lbItemPrice, new GBC(0, 6, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(txItemPrice, new GBC(1, 6, 1, 10).setWeight(0.8, 1));
        fillInPanel.add(lbItemIntroduction, new GBC(0, 8, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(taItemIntroduction, new GBC(1, 8, 4, 20).setWeight(0.8, 1));
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

        btRelease = new JButton("提交修改信息");
        btRelease.addActionListener(this);
        panel = new JPanel();
        panel.add(btRelease);
        c.add(panel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("提交修改信息")) {
            if (txItemName.getText().isEmpty() || txItemQuantity.getText().isEmpty() || txItemPrice.getText().isEmpty() || taItemIntroduction.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "信息不完整！", "Oops", JOptionPane.ERROR_MESSAGE);
            } else if (Path.equals("")) {
                JOptionPane.showMessageDialog(this, "未上传图片！", "Oops", JOptionPane.ERROR_MESSAGE);
            } else {
            }

        } else if (e.getActionCommand().equals("上传图片")) {
            //new UpLoad(this);
        }
    }

    private class NET_ReleaseItem {
        private final String Command = "RELEASE ITEM";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private PrintWriter out;

        private String json, resultCode;

        public NET_ReleaseItem(ItemData itemData, String path) throws Exception {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();

            json = JSON.toJSONString(itemData);//使用JSON序列化对象传输过去
            out.println(json);

            sendFile(path);
            System.out.println("eee");
            this.resultCode = dis.readUTF();

            this.socket.close();
        }

        public String getResultCode() {
            return resultCode;
        }

        private void sendFile(String path) throws Exception {//传图方法，直接用就行
            FileInputStream fis;
            File file = new File(path);
            if (file.exists()) {
                fis = new FileInputStream(file);
                // 文件名
                dos.writeUTF(file.getName());
                dos.flush();
                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                }
                System.out.println("======== 文件传输成功 ========");
            }
        }
    }
}