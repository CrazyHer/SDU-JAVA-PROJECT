package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FaceDemo extends JFrame implements ActionListener {
    public final int x_init_face = 10, y_init_face = 50;
    public final int width_face = 100, height_face = 100;
    public final int noseSize = 5;
    public JButton button;
    public boolean isSmile = false;
    public int xFace = x_init_face, yFace = y_init_face;
    public int xNose = xFace + 50, yNose = yFace + 50;

    public FaceDemo() {
        super();
        setTitle("画图测试");
        setLocationRelativeTo(null);
        setSize(400,400);
        Container c = this.getContentPane();
        addWindowListener(new WindowClose());
        c.setLayout(new BorderLayout());
        button = new JButton("点我画个脸");
        button.addActionListener(this);
        c.add(button,BorderLayout.SOUTH);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawArc(xFace, yFace, width_face, height_face, 0, 360);
        g.fillArc(xNose, yNose, noseSize, noseSize, 0, 360);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("点我画个脸")) {
            isSmile = !isSmile;
                xFace+=20;
                yFace+=20;
                xNose = xFace + 50;
                yNose = yFace + 50;
        } else {

        }
        repaint();
    }
    class WindowClose extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e) {
            setVisible(false);
        }
    }
}