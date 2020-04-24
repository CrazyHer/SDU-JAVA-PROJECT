package client.userInfo;

import server.dataObjs.UserData;

import javax.swing.*;
import java.awt.*;

public class UserInfo extends JPanel {
    JPanel photoPanel;
    JLabel nameLable, IDLable;
    public static Info user;

    UserInfo(UserData userData, ImageIcon img) {
        setLayout(new GridBagLayout());
        user = new Info(userData.getUsername(), userData.getID(), img);
        photoPanel = new JPanel();
        photoPanel.add(new JLabel(user.getImageIcon()));
        add(photoPanel, new GBC(0, 0, 2, 2).setWeight(0.1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE));
        nameLable = new JLabel(user.getName());
        nameLable.setFont(new Font("宋体", Font.BOLD, 24));
        add(nameLable, new GBC(3, 0, 1, 1).setWeight(0.8, 0.5).setAnchor(GridBagConstraints.SOUTHWEST));
        IDLable = new JLabel(user.getID());
        add(IDLable, new GBC(3, 1, 1, 1).setWeight(0.8, 0.5).setAnchor(GridBagConstraints.NORTHWEST));
    }

    public class Info {
        String name, ID;
        ImageIcon image;

        Info(String name, String ID, ImageIcon image) {
            this.name = name;
            this.ID = ID;
            this.image = image;
            //以下将头像缩放为100*100尺寸

            image.setImage(image.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        }

        String getName() {
            return name;
        }

        public String getID() {
            return ID;
        }

        ImageIcon getImageIcon() {
            return image;
        }
    }
}
