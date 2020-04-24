package client.itemList;

import com.alibaba.fastjson.JSON;
import server.dataObjs.Comment;
import server.dataObjs.ItemData;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ItemInfo extends JPanel {
    JPanel photoPanel;
    JLabel nameLabel, priceLabel, quantityLabel, introductionLabel, commentLabel;
    Info item;
    String comment = "";
    ItemData itemData;
    ImageIcon itemImage;
    Comment[] comments;

    public ItemInfo(String itemName) throws IOException {
        try {
            new NET_GetOneItemDetail(itemName);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "图片丢失！", "Oops", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        try {
            new NET_GetComment(itemName);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "评论丢失！", "Oops", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        setLayout(new GridBagLayout());
        item = new Info(itemData.getName(), itemData.getPrice(), itemImage, itemData.getQuantity(), itemData.getIntroduction(), comments);
        photoPanel = new JPanel();
        photoPanel.add(new JLabel(item.getImageIcon()));
        add(photoPanel, new GBC(0, 0, 4, 4).setWeight(0.5, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE));
        nameLabel = new JLabel("商品名称:" + item.getName());
        nameLabel.setFont(new Font("隶书", Font.BOLD, 20));
        nameLabel.setForeground(Color.BLACK);
        add(nameLabel, new GBC(5, 0, 1, 1).setWeight(0.8, 0.5).setAnchor(GridBagConstraints.SOUTHWEST));
        priceLabel = new JLabel("单价:" + String.valueOf(item.getPrice()));
        priceLabel.setForeground(Color.RED);
        add(priceLabel, new GBC(5, 1, 1, 1).setWeight(0.8, 0.5).setAnchor(GridBagConstraints.NORTHWEST));
        quantityLabel = new JLabel("剩余数量:" + String.valueOf(item.getQuantity()));
        add(quantityLabel, new GBC(5, 2, 1, 1).setWeight(0.8, 0.5).setAnchor(GridBagConstraints.NORTHWEST));
        introductionLabel = new JLabel("商品介绍:" + item.getIntroduction());
        introductionLabel.setFont(new Font("楷体", Font.BOLD, 16));
        add(introductionLabel, new GBC(5, 3, 1, 1).setWeight(0.8, 0.5).setAnchor(GridBagConstraints.NORTHWEST));
        comments = item.getComment();
        if (comments.length == 0) {
            comment = "暂无";
        } else {
            for (int i = 0; i < comments.length; i++) comment = comment + (i + 1) + "." + comments[i].getText() + "\n";
        }
        commentLabel = new JLabel("商品评价:\n" + comment);
        commentLabel.setFont(new Font("黑体", Font.ITALIC, 12));
        add(commentLabel, new GBC(5, 4, 1, 1).setWeight(0.8, 0.5).setAnchor(GridBagConstraints.NORTHWEST));
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

    private class Info {
        private String name, introduction;
        private int quantity;
        private double price;
        private Comment[] comments;
        private ImageIcon image;

        Info(String name, Double price, ImageIcon image, int quantity, String introduction, Comment[] comments) {
            this.name = name;
            this.price = price;
            this.image = image;
            this.quantity = quantity;
            this.introduction = introduction;
            this.comments = comments;

            //以下将图片缩放为100*100尺寸
            image.setImage(image.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        }

        String getName() {
            return name;
        }

        double getPrice() {
            return price;
        }

        int getQuantity() {
            return quantity;
        }

        ImageIcon getImageIcon() {
            return image;
        }

        String getIntroduction() {
            return introduction;
        }

        Comment[] getComment() {
            return comments;
        }
    }

    private class NET_GetOneItemDetail {
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

        public NET_GetOneItemDetail(String itemName) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            json = JSON.toJSONString(itemName);//使用JSON序列化对象传输过去
            out.println(json);
            itemData = JSON.parseObject(in.readLine(), ItemData.class);
            getFile("C:/Users/Public/Item");
            itemImage = new ImageIcon(Path);
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

    private class NET_GetComment {
        private final String Command = "GET COMMENT";//请求类型
        private final String Address = "localhost";
        private final int PORT = 2333;//服务器端口
        private Socket socket;
        private DataInputStream dis;//输入
        private DataOutputStream dos;//输出
        private BufferedReader in;
        private PrintWriter out;
        private String json;

        public NET_GetComment(String itemName) throws IOException {
            this.socket = new Socket(this.Address, this.PORT);
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            dos = new DataOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            dos.writeUTF(Command);
            dos.flush();
            json = JSON.toJSONString(itemName);//使用JSON序列化对象传输过去
            out.println(json);
            comments = (Comment[]) JSON.parseArray(in.readLine()).toArray();

            this.socket.close();
        }
    }
}
