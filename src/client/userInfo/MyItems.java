package client.userInfo;

import client.itemList.ItemListFrame;
import client.itemList.ReleaseFrame;
import client.itemState.ItemState;
import com.alibaba.fastjson.JSON;
import server.dataObjs.ItemData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

public class MyItems extends JPanel implements ActionListener {
    JPanel panel, bp, sp;
    JButton button;
    Items[] bought, sold;
    OnClick onClick;
    String userID;

    MyItems(String ID) throws IOException {
        userID = ID;
        bought = new NET_GetMyBoughtItem(ID).getBought();
        sold = new NET_GetMySoldItem(ID).getSold();
        onClick = new OnClick();
        setLayout(new GridBagLayout());
        add(new JLabel("我买的商品"), new GBC(0, 0, 1, 1).setWeight(0.1, 1).setAnchor(GridBagConstraints.WEST));
        bp = new JPanel();
        bp.setLayout(new GridLayout(0, 4, 30, 10));
        for (int i = 0; i < bought.length; i++) {
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(new JLabel(bought[i].getImageIcon()), BorderLayout.CENTER);
            panel.add(new JLabel(bought[i].getName()), BorderLayout.SOUTH);
            panel.setName(bought[i].getName());
            panel.addMouseListener(onClick);
            bp.add(panel);
        }
        button = new JButton("购买商品");
        button.addActionListener(this);
        bp.add(button);
        add(bp, new GBC(0, 2, 1, 1).setWeight(0.4, 1).setAnchor(GridBagConstraints.WEST));


        add(new JLabel("我卖的商品"), new GBC(0, 4, 1, 1).setWeight(0.1, 1).setAnchor(GridBagConstraints.WEST));
        sp = new JPanel();
        sp.setLayout(new GridLayout(0, 4, 30, 10));
        for (int i = 0; i < sold.length; i++) {
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(new JLabel(sold[i].getImageIcon()), BorderLayout.CENTER);
            panel.add(new JLabel(sold[i].getName()), BorderLayout.SOUTH);
            panel.setName(sold[i].getName());
            panel.addMouseListener(onClick);
            sp.add(panel);
        }
        button = new JButton("发布商品");
        button.addActionListener(this);
        sp.add(button);
        add(sp, new GBC(0, 6, 1, 1).setWeight(0.4, 1).setAnchor(GridBagConstraints.WEST));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "购买商品":
                new ItemListFrame().setVisible(true);
                break;
            case "发布商品":
                new ReleaseFrame().setVisible(true);
                break;
            default:
                break;
        }
    }

    private class OnClick extends MouseAdapter {
        public OnClick() {
            super();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                new ItemState(e.getComponent().getName(), userID);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private class Items {
        String name;
        ImageIcon imageIcon;

        Items(String name, ImageIcon image) {
            this.name = name;
            this.imageIcon = image;
            imageIcon.setImage(image.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        }

        String getName() {
            return name;
        }

        ImageIcon getImageIcon() {
            return imageIcon;
        }
    }

    private class NET_GetMyBoughtItem {
        private final String Command = "GET MY BOUGHT ITEM";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出

        private BufferedReader in;
        private String[] myBoughtList;
        private Items[] bought;

        public NET_GetMyBoughtItem(String userID) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dos.writeUTF(Command);
            dos.flush();
            //上面的可以照搬，只需要改一下请求类型Command即可
            /*
            注意：上面能不改就不改，因为Command只能用writeUTF发送；下面的对象传输只能用out.println()来传输JSON序列化的对象
             */
            //下面是对接操作，对象用下面的方式传就行了，不要再用ObjectOutputStream了
            dos.writeUTF(userID);
            dos.flush();
            myBoughtList = JSON.parseObject(in.readLine(), String[].class);
            int n = myBoughtList.length;
            bought = new Items[n];
            for (int i = 0; i < n; i++) {
                NET_GetItemDetails get = new NET_GetItemDetails(myBoughtList[i]);
                bought[i] = new Items(get.getItemData().getName(), get.getImageIcon());
            }
            socket.close();
        }

        public Items[] getBought() {
            return bought;
        }
    }

    private class NET_GetMySoldItem {
        private final String Command = "GET MY SOLD ITEM";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private PrintWriter out;

        private BufferedReader in;
        private String[] mySoldList;
        private Items[] sold;

        public NET_GetMySoldItem(String userID) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dos.writeUTF(Command);
            dos.flush();
            //上面的可以照搬，只需要改一下请求类型Command即可
            /*
            注意：上面能不改就不改，因为Command只能用writeUTF发送；下面的对象传输只能用out.println()来传输JSON序列化的对象
             */
            //下面是对接操作，对象用下面的方式传就行了，不要再用ObjectOutputStream了
            dos.writeUTF(userID);
            dos.flush();
            mySoldList = JSON.parseObject(in.readLine(), String[].class);
            int n = mySoldList.length;
            sold = new Items[n];
            for (int i = 0; i < n; i++) {
                NET_GetItemDetails get = new NET_GetItemDetails(mySoldList[i]);
                sold[i] = new Items(get.getItemData().getName(), get.getImageIcon());
            }
            socket.close();
        }

        public Items[] getSold() {
            return sold;
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
        private ItemData itemData;
        private String Path;
        private ImageIcon imageIcon;

        public NET_GetItemDetails(String itemName) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            dos.writeUTF(Command);
            dos.flush();

            dos.writeUTF(itemName);
            dos.flush();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            itemData = JSON.parseObject(in.readLine(), ItemData.class);
            getFile("C:/Users/Public/Item");
            imageIcon = new ImageIcon(ImageIO.read(new File(Path)));
            socket.close();
        }

        private void getFile(String path) throws IOException {//接收文件的方法，直接用即可,参数为存放文件夹路径，注意是文件夹
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

        public ImageIcon getImageIcon() {
            return imageIcon;
        }

        public ItemData getItemData() {
            return itemData;
        }
    }

}
