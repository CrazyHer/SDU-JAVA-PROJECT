package client.itemList;

import client.itemState.ItemState;
import client.login.LoginFrame;
import com.alibaba.fastjson.JSON;
import dataObjs.ItemData;
import dataObjs.ItemListFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static client.ClientMain.Address;
import static client.ClientMain.PORT;

public class ItemListFrame extends JFrame implements ActionListener {

    public JMenu menu;
    public JMenuBar menuBar;
    public JMenuItem menuItemLogout;
    public JMenuItem menuDefaultSort;
    public JMenuItem menuTimeSort;
    public JMenuItem menuSaleSort;
    public JTextField tfSearch;
    public JButton btSearch;
    public JScrollPane scPanel;//滚动面板
    public JPanel panel;
    public JButton btDetail;
    public NET_GetItemList net_getItemList = null;
    public String userID;

    public ItemListFrame(String userID) {
        this.userID = userID;
        setBg();
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setSize(700, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("商品列表");
        addMenu();

        //添加搜索模块
        panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        tfSearch = new JTextField(20);
        btSearch = new JButton("搜索");
        btSearch.addActionListener(this);
        panel.add(tfSearch);
        panel.add(btSearch);
        c.add(panel, BorderLayout.NORTH);

        //添加商品列表
        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        //panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        scPanel = new JScrollPane(panel);
        scPanel.setOpaque(false);
        panel.setPreferredSize(new Dimension(400, 400));
        c.add(scPanel, BorderLayout.CENTER);
        System.out.println("开始创建初始商品列表");

        try {
            net_getItemList = new NET_GetItemList(new ItemListFilter("*", 0));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "服务器的头像文件不见了！", "Oops", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        ShowEveryItem();
        System.out.println("初始商品列表创建完成");

    }

    public void setBg() {
        ((JPanel) this.getContentPane()).setOpaque(false);
        ImageIcon img = new ImageIcon("C:\\Users\\Public\\背景\\背景4.jpg");
        img.setImage(img.getImage().getScaledInstance(700, 800, Image.SCALE_DEFAULT));
        JLabel background = new JLabel(img);
        this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
        background.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
    }

    public void addMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("菜单");
        menuBar.add(menu);
        menuItemLogout = new JMenuItem("退出登录");
        menu.add(menuItemLogout);
        menuDefaultSort = new JMenuItem("默认排序");
        menu.add(menuDefaultSort);
        menuTimeSort = new JMenuItem("按时间排序");
        menu.add(menuTimeSort);
        menuSaleSort = new JMenuItem("按销量排序");
        menu.add(menuSaleSort);
        setJMenuBar(menuBar);
        menuItemLogout.addActionListener(this);
        menuDefaultSort.addActionListener(this);
        menuTimeSort.addActionListener(this);
        menuSaleSort.addActionListener(this);
    }

    public void deleteAll(String path) {
        File filePar = new File(path);
        if (filePar.exists()) {
            File files[] = filePar.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                } else if (files[i].isDirectory()) {
                    deleteAll(files[i].getAbsolutePath());
                    files[i].delete();
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        String search = tfSearch.getText();
        if (tfSearch.getText().equals("")) search = "*";
        if (e.getSource().equals(menuItemLogout)) {
            int i = JOptionPane.showConfirmDialog(null, "是否退出当前账号?", "提示", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                try {
                    new NET_Logout(userID);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(this, "退出成功！", "Oops", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new LoginFrame().setVisible(true);
            }

        } else if (e.getSource().equals(menuDefaultSort) || e.getSource().equals(btSearch)) {
            System.out.println(search);

            panel.removeAll();
            panel.repaint();
            net_getItemList = null;
            try {
                net_getItemList = new NET_GetItemList(new ItemListFilter(search, 0));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "错误！", "Oops", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            ShowEveryItem();
            panel.revalidate();
        } else if (e.getSource().equals(menuTimeSort)) {
            panel.removeAll();
            panel.repaint();
            net_getItemList = null;
            try {
                net_getItemList = new NET_GetItemList(new ItemListFilter(search, 2));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "错误！", "Oops", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            ShowEveryItem();
            panel.revalidate();
        } else if (e.getSource().equals(menuSaleSort)) {
            panel.removeAll();
            panel.repaint();
            net_getItemList = null;
            try {
                net_getItemList = new NET_GetItemList(new ItemListFilter(search, 1));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "错误！", "Oops", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            ShowEveryItem();

        }
    }

    public void ShowEveryItem() {
        String itemList[];
        itemList = net_getItemList.getItemList();
        System.out.println("开始逐个添加商品小面板");
        NET_GetItemDetails net_getItemDetails = null;
        for (int i = 0; i < itemList.length; i++) {
            try {
                net_getItemDetails = new NET_GetItemDetails(itemList[i]);
                System.out.println(itemList[i]);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "错误！", "Oops", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            AddAItem(net_getItemDetails.getItemData(), net_getItemDetails.getPath());
        }
        deleteAll("C:/Users/Public/client/temp/");
        System.out.println("暂存图像删除完成");
    }

    public void AddAItem(ItemData itemData, String Path) {
        ImageIcon itemImage = new ImageIcon(Path);
        itemImage.setImage(itemImage.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT));
        JPanel tempPanel = new JPanel();
        tempPanel.setOpaque(false);
        tempPanel.setSize(150, 200);
        tempPanel.setLayout(new BorderLayout());
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.add(new JLabel(itemData.getName()));
        p.add(new JLabel("￥ " + itemData.getPrice()));
        tempPanel.add(p, BorderLayout.NORTH);
        tempPanel.add(new JLabel(itemImage), BorderLayout.CENTER);
        btDetail = new JButton("详情");
        btDetail.addActionListener(e -> {
            try {
                new ItemState(itemData.getName(), userID);
                System.out.println("创建对应商品详情窗口");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });//内部类监听器
        System.out.println("完成对应监听");
        System.out.println("对应详情窗口创建完成");
        tempPanel.add(btDetail, BorderLayout.SOUTH);
        panel.repaint();
        panel.add(tempPanel);
        panel.revalidate();
        System.out.println("完成商品列表添加");
    }

    private class NET_GetItemList {
        private final String Command = "GET ITEM LIST";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;
        private String Path;
        private String json;
        public String[] itemList = null;

        public NET_GetItemList(ItemListFilter itemListFilter) throws IOException {
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            /*
            注意：上面能不改就不改，因为Command只能用writeUTF发送；下面的对象传输只能用out.println()来传输JSON序列化的对象
             */
            //下面是对接操作，对象用下面的方式传就行了，不要再用ObjectOutputStream了
            json = JSON.toJSONString(itemListFilter);//使用JSON序列化对象传输过去
            out.println(json);

            itemList = JSON.parseObject(in.readLine(), String[].class);
            if (itemList.length != 0) System.out.println("商品库里有" + itemList.length + "件商品");
            else if (itemList.length == 0) System.out.println("商品库里没有商品");

            this.socket.close();
        }

        public String[] getItemList() {
            return itemList;
        }

    }

    private class NET_GetItemDetails {
        private final String Command = "GET ITEM DETAILS";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;
        private String Path;
        private String json;
        private ItemData itemData;

        public NET_GetItemDetails(String item) throws IOException {
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            System.out.println("要搜索的商品:" + item);
            dos.writeUTF(item);
            dos.flush();
            itemData = JSON.parseObject(in.readLine(), ItemData.class);
            System.out.println("接收到要搜索的商品数据了");

            getFile("C:/Users/Public/client/temp/");

            this.socket.close();
        }

        public void getFile(String path) throws IOException {//接收文件的方法，直接用即可,参数为存放路径
            FileOutputStream fos;
            // 文件名
            String fileName = dis.readUTF();

            System.out.println("接收到文件:" + fileName);
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
            Path = file.getAbsolutePath().replace('\\', '/');
            System.out.println("文件路径:" + Path);
            fos = new FileOutputStream(file);
            // 开始接收文件
            byte[] bytes = new byte[1024];
            int length;
            while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                fos.write(bytes, 0, length);
                fos.flush();
            }
            System.out.println("======== 文件接收成功========");
        }

        public String getPath() {
            return Path;
        }

        public ItemData getItemData() {
            return itemData;
        }
    }

    private class NET_Logout {
        private final String Command = "LOG OUT";//请求类型
        //private final String Address = "localhost";
        //private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;
        private String userID;

        public NET_Logout(String userID) throws IOException {
            this.userID = userID;
            this.socket = new Socket(Address, PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();

            dos.writeUTF(this.userID);
            dos.flush();

            this.socket.close();
        }

    }
}
