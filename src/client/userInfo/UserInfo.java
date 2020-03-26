package client.userInfo;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class UserInfo extends JPanel {
    JPanel photoPanel;
    JLabel nameLable, IDLable;
    Info user;

    UserInfo() {
        setLayout(new GridBagLayout());
        user = new Info("马云", "201900306666", "https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2605178066,625446438&fm=26&gp=0.jpg");
        photoPanel = new JPanel();
        photoPanel.add(new JLabel(user.getImageIcon()));
        add(photoPanel, new GBC(0, 0, 2, 2).setWeight(0.1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE));
        nameLable = new JLabel(user.getName());
        nameLable.setFont(new Font("宋体", Font.BOLD, 24));
        add(nameLable, new GBC(3, 0, 1, 1).setWeight(0.8, 0.5).setAnchor(GridBagConstraints.SOUTHWEST));
        IDLable = new JLabel(user.getID());
        add(IDLable, new GBC(3, 1, 1, 1).setWeight(0.8, 0.5).setAnchor(GridBagConstraints.NORTHWEST));
    }

    class Info {
        String name, ID, imageURL;
        ImageIcon image;

        Info(String name, String ID, String imageURL) {
            this.name = name;
            this.ID = ID;
            this.imageURL = imageURL;
            //以下将头像缩放为100*100尺寸
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

        String getID() {
            return ID;
        }

        ImageIcon getImageIcon() {
            return image;
        }
    }
}
