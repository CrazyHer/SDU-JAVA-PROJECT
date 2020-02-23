package StudyInWinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EventDemo extends JFrame {
    public static final int width = 300;
    public static final int height = 200;

    public EventDemo() {

        JButton jbtpr = new JButton("print1");
        JButton jbtpr0 = new JButton("print2");

        add(jbtpr);
        add(jbtpr0);
        ActionListener lsn = new prListener();
        ActionListener lsn0 = new prListener0();
        jbtpr.addActionListener(lsn);
        jbtpr0.addActionListener(lsn0);
        setLayout(new FlowLayout());
    }


}
