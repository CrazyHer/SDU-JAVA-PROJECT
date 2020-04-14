package client.userInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class MyItems extends JPanel implements ActionListener {
    JPanel panel, bp, sp;
    JButton button;
    String URL1 = "https://bkimg.cdn.bcebos.com/pic/c8ea15ce36d3d539940b028b3a87e950352ab047?x-bce-process=image/resize,m_lfit,w_220,h_220,limit_1";
    String URL2 = "https://bkimg.cdn.bcebos.com/pic/7acb0a46f21fbe09f0de5c656b600c338744ad3b?x-bce-process=image/resize,m_lfit,w_220,h_220,limit_1";
    String URL3 = "https://bkimg.cdn.bcebos.com/pic/5fdf8db1cb134954b37001e85c4e9258d0094ad4?x-bce-process=image/resize,m_lfit,w_268,limit_1/format,f_jpg";
    Items bought[] = {new Items("商品1", URL1), new Items("商品2", URL2), new Items("商品3", URL3)};

    MyItems() {

        setLayout(new GridBagLayout());
        add(new JLabel("我买的商品"), new GBC(0, 0, 1, 1).setWeight(0.1, 1).setAnchor(GridBagConstraints.WEST));
        bp = new JPanel();
        bp.setLayout(new GridLayout(1, 0, 30, 10));
        for (int i = 0; i < 3; i++) {
            if (i == bought.length) break;
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(new JLabel(bought[i].getImageIcon()), BorderLayout.CENTER);
            panel.add(new JLabel(bought[i].getName()), BorderLayout.SOUTH);
            bp.add(panel);
        }
        if (bought.length > 3) {
            button = new JButton("更多");
            button.addActionListener(this);
            bp.add(button);
        }
        button = new JButton("购买商品");
        button.addActionListener(this);
        bp.add(button);
        add(bp, new GBC(0, 2, 1, 1).setWeight(0.4, 1).setAnchor(GridBagConstraints.WEST));


        add(new JLabel("我卖的商品"), new GBC(0, 4, 1, 1).setWeight(0.1, 1).setAnchor(GridBagConstraints.WEST));
        sp = new JPanel();
        sp.setLayout(new GridLayout(1, 0, 30, 10));
        for (int i = 0; i < 3; i++) {
            if (i == bought.length) break;
            panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(new JLabel(bought[i].getImageIcon()), BorderLayout.CENTER);
            panel.add(new JLabel(bought[i].getName()), BorderLayout.SOUTH);
            sp.add(panel);
        }
        if (bought.length > 3) {
            button = new JButton("更多");
            button.addActionListener(this);
            sp.add(button);
        }
        button = new JButton("发布商品");
        button.addActionListener(this);
        sp.add(button);
        add(sp, new GBC(0, 6, 1, 1).setWeight(0.4, 1).setAnchor(GridBagConstraints.WEST));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "更多":

                break;
            case "购买商品":

                break;
            case "发布商品":

                break;

            default:
                break;
        }
    }

    class Items {
        String name, imageURL;
        ImageIcon image;

        Items(String name, String imageURL) {
            this.name = name;
            this.imageURL = imageURL;
            try {
                image = new ImageIcon(new URL(imageURL));
            } catch (Exception e) {
                System.out.println(e);
            }
            image.setImage(image.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        }

        String getName() {
            return name;
        }

        ImageIcon getImageIcon() {
            return image;
        }
    }
}
