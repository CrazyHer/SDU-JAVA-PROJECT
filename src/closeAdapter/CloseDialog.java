package closeAdapter;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;


//closeDialog myclose =new closeDialog();
//addWindowListener(myclose);


public class CloseDialog extends WindowAdapter {
	
	public static JDialog jdlClose=new CloseWindowFrame();

	public void windowClosing(WindowEvent e) {
		jdlClose.setVisible(true);
	}
	
	public static void invisible() {
		jdlClose.setVisible(false);
	}

}




