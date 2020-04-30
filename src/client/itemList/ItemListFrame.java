package client.itemList;

import client.itemState.ItemState;
import client.login.LoginFrame;
import client.userInfo.UserInfo;
import com.alibaba.fastjson.JSON;
import server.dataObjs.ItemData;
import server.dataObjs.ItemListFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;


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
    public String[] itemList;

    public ItemListFrame() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setSize(600, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("商品列表");
        addMenu();

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        tfSearch = new JTextField(20);
        btSearch = new JButton("搜索");
        panel.add(tfSearch);
        panel.add(btSearch);
        c.add(panel, BorderLayout.NORTH);

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1024));
        try {
            new NET_GetItemList(new ItemListFilter("*", 0));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "服务器的头像文件不见了！", "Oops", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        scPanel = new JScrollPane();
        scPanel.add(panel);
        c.add(scPanel, BorderLayout.CENTER);

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

    public void actionPerformed(ActionEvent e) {
        String search = btSearch.getText();
        if (tfSearch.getText().equals("")) search = "*";
        if (e.getSource().equals(menuItemLogout)) {
            this.dispose();
            new LoginFrame();
        } else if (e.getSource().equals(menuDefaultSort) || e.getSource().equals(btSearch)) {
            panel.removeAll();
            panel.repaint();
            try {
                new NET_GetItemList(new ItemListFilter(search, 0));
                ShowEveryItem();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "错误！", "Oops", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            panel.revalidate();
        } else if (e.getSource().equals(menuTimeSort)) {
            panel.removeAll();
            panel.repaint();
            try {
                new NET_GetItemList(new ItemListFilter(search, 2));
                ShowEveryItem();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "错误！", "Oops", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            panel.revalidate();
        } else if (e.getSource().equals(menuSaleSort)) {
            panel.removeAll();
            panel.repaint();
            try {
                new NET_GetItemList(new ItemListFilter(search, 1));
                ShowEveryItem();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "错误！", "Oops", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            panel.revalidate();
        }
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

    public void ShowEveryItem() throws IOException {
        for (int i = 0; i < itemList.length; i++) {
            try {
                new NET_GetItemDetails(itemList[i]);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "错误！", "Oops", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
        deleteAll("C:/Users/Public/client/");
    }

    private class NET_GetItemList {
        private final String Command = "GET ITEM LIST";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;
        private String Path;
        private String json;

        public NET_GetItemList(ItemListFilter itemListFilter) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
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

            this.socket.close();
        }

    }

    private class NET_GetItemDetails {
        private final String Command = "GET ITEM DETAILS";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;
        private String Path;
        private String json;

        public NET_GetItemDetails(String item) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            json = JSON.toJSONString(item);//使用JSON序列化对象传输过去
            out.println(json);
            ItemData itemData = JSON.parseObject(in.readLine(), ItemData.class);
            getFile("C:/Users/Public/client/");
            ImageIcon itemImage = new ImageIcon(Path);
            JPanel tempPanel = new JPanel();
            tempPanel.setLayout(new BorderLayout());
            tempPanel.add(new JLabel(itemData.getName()), BorderLayout.NORTH);
            tempPanel.add(new JLabel(itemImage), BorderLayout.CENTER);
            btDetail = new JButton("详情");
            btDetail.addActionListener(e -> {
                try {
                    new ItemState(itemData.getName(), UserInfo.user.getID());
                    //new BoughtItemInfoFrame(new ItemInfo(itemData.getName()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });//内部类监听器
            tempPanel.add(btDetail, BorderLayout.SOUTH);
            panel.repaint();
            panel.add(tempPanel);
            panel.revalidate();

            this.socket.close();
        }

        public void getFile(String path) throws IOException {//接收文件的方法，直接用即可,参数为存放路径
            FileOutputStream fos;
            // 文件名
            String fileName = dis.readUTF();
            System.out.println("接收到文件" + fileName);
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
            Path = file.getAbsolutePath().replace('\\', '/');
            System.out.println(Path);
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
    }
}
