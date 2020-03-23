package client.closeAdapter;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


//closeDialog myclose =new closeDialog();
//addWindowListener(myclose);


public class CloseDialog extends WindowAdapter {

    public static JDialog jdlClose = new CloseWindowFrame();

    public static void invisible() {
        jdlClose.setVisible(false);
    }

    public void windowClosing(WindowEvent e) {
        jdlClose.setVisible(true);
    }

}




