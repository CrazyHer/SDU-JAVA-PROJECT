package client.itemList;

import client.ClientMain;
import com.alibaba.fastjson.JSON;
import dataObjs.ItemData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static client.ClientMain.Address;
import static client.ClientMain.PORT;

public class EditItemInfoFrame extends JFrame implements ActionListener {

    public JPanel panel;
    public JPanel fillInPanel;
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
    public JButton btEdit;
    public JButton btCancel;
    public String Path = "";
    public String ItemName;
    SoldItemInfoFrame ParentFrame;

    public EditItemInfoFrame(SoldItemInfoFrame ParentFrame,String ItemName) {
        this.ParentFrame = ParentFrame;
        this.ItemName = ItemName;
        setBg();
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("修改商品信息");

        fillInPanel = new JPanel();//填写发布信息面板
        fillInPanel.setOpaque(false);
        fillInPanel.setLayout(new GridBagLayout());
        lbExplain = new JLabel("说明：填写要修改的信息,并长传商品图片(如果不换图片,请上传原来图片)");
        lbExplain.setForeground(Color.RED);
        lbExplain.setFont(new Font("楷体", Font.ITALIC, 16));
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

        c.add(lbExplain, BorderLayout.NORTH);

        fillInPanel.add(lbItemName, new GBC(0, 2, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(txItemName, new GBC(1, 2, 1, 10).setWeight(0.8, 1));
        fillInPanel.add(lbItemQuantity, new GBC(0, 4, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(txItemQuantity, new GBC(1, 4, 1, 10).setWeight(0.8, 1));
        fillInPanel.add(lbItemPrice, new GBC(0, 6, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(txItemPrice, new GBC(1, 6, 1, 10).setWeight(0.8, 1));
        fillInPanel.add(lbItemIntroduction, new GBC(0, 8, 1, 1).setWeight(0.8, 1));
        fillInPanel.add(taItemIntroduction, new GBC(1, 8, 4, 20).setWeight(0.8, 1));
        panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        panel.add(fillInPanel, BorderLayout.CENTER);

        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        changedPanel = new JPanel();
        changedPanel.setOpaque(false);
        changedPanel.add(btUpload);
        p.add(changedPanel);
        panel.add(p, BorderLayout.SOUTH);
        c.add(panel, BorderLayout.CENTER);

        btEdit = new JButton("提交修改信息");
        btEdit.addActionListener(this);
        btCancel = new JButton("取消");
        btCancel.addActionListener(this);
        panel = new JPanel(new FlowLayout());
        panel.setOpaque(false);
        panel.add(btEdit);
        panel.add(btCancel);
        c.add(panel, BorderLayout.SOUTH);
    }

    public void setBg() {
        ((JPanel) this.getContentPane()).setOpaque(false);
        ImageIcon img = new ImageIcon(ClientMain.class.getResource("bgImg/背景10.jpg"));
        img.setImage(img.getImage().getScaledInstance(600, 500, Image.SCALE_DEFAULT));
        JLabel background = new JLabel(img);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("提交修改信息")) {
            NET_EditItem net_editItem = null;
            if (Path.equals("")) {
                JOptionPane.showMessageDialog(this, "未上传图片！", "Oops", JOptionPane.WARNING_MESSAGE);
            } else {
                System.out.println("图片路径" + Path);
                try {
                    net_editItem = new NET_EditItem(ItemName, Path);
                    System.out.println("3");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "图片路径出错！", "Oops", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                String resultCode = net_editItem.getResultCode();
                if (resultCode.equals("1")) {
                    JOptionPane.showMessageDialog(this, "修改成功！");

                } else if (resultCode.equals("-1")) JOptionPane.showMessageDialog(this, "修改失败！");
                this.dispose();
                ParentFrame.dispose();
            }

        } else if (e.getActionCommand().equals("上传图片")) {
            new UpLoad_EditItemInfo(this);
        } else if (e.getActionCommand().equals("取消")) {
            this.dispose();
        }
    }

    private class NET_EditItem {
        private final String Command = "EDIT ITEM";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;
        private ItemData itemData;
        private String json, resultCode;

        public NET_EditItem(String itemName, String path) throws Exception {
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();

            dos.writeUTF(itemName);
            dos.flush();
            System.out.println(itemName);

            itemData = JSON.parseObject(in.readLine(), ItemData.class);

            System.out.println("已接收原商品数据 " + itemData.getItemID());

            if (itemData != null) {
                if (!(txItemName.getText().equals(""))) itemData.setName(txItemName.getText());
                if (!(txItemQuantity.getText().isEmpty()))
                    itemData.setQuantity(Integer.parseInt(txItemQuantity.getText()));
                if (!(txItemPrice.getText().isEmpty())) itemData.setPrice(Double.parseDouble(txItemPrice.getText()));
                if (!(taItemIntroduction.getText().isEmpty())) itemData.setIntroduction(taItemIntroduction.getText());
            }

            System.out.println("准备发送修改后的商品数据");

            json = JSON.toJSONString(itemData);//使用JSON序列化对象传输过去
            out.println(json);

            System.out.println("商品数据发送成功");

            this.resultCode = dis.readUTF();
            System.out.println("1");
            sendFile(path);
            System.out.println("2");
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
