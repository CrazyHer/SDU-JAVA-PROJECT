package CloseAdapter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;


//closeDialog myclose =new closeDialog();
//addWindowListener(myclose);


public class closeDialog extends WindowAdapter {
	
	public static JDialog jdlClose=new close_window();

	public void windowClosing(WindowEvent e) {
		jdlClose.setVisible(true);
	}
	
	public static void invisible() {
		jdlClose.setVisible(false);
	}

}




