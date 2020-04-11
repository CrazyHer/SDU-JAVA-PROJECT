package client.itemList;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ItemInfo extends JPanel {
    JPanel photoPanel;
    JLabel nameLabel, priceLabel, quantityLabel, introductionLabel, commentLabel;
    Info item;
    String comment;

    ItemInfo() {
        setLayout(new GridBagLayout());
        item = new Info("马云", 66666.66, "https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2605178066,625446438&fm=26&gp=0.jpg", 1, "无价之宝", "");
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
        comment = item.getComment();
        if (comment.equals("")) {
            comment = "暂无";
        }
        commentLabel = new JLabel("商品评价:" + comment);
        commentLabel.setFont(new Font("黑体", Font.ITALIC, 12));
        add(commentLabel, new GBC(5, 4, 1, 1).setWeight(0.8, 0.5).setAnchor(GridBagConstraints.NORTHWEST));
    }

    class Info {
        String name, imageURL, introduction, comment;
        int quantity;
        double price;
        ImageIcon image;

        Info(String name, Double price, String imageURL, int quantity, String introduction, String comment) {
            this.name = name;
            this.price = price;
            this.imageURL = imageURL;
            this.quantity = quantity;
            this.introduction = introduction;
            this.comment = comment;

            //以下将图片缩放为100*100尺寸
            try {
                image = new ImageIcon(new URL(this.imageURL));
            } catch (Exception e) {
                System.out.println(e);
            }
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

        String getComment() {
            return comment;
        }
    }
}
