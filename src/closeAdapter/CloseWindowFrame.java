package closeAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class CloseWindowFrame extends JDialog {

	private static final long serialVersionUID = 1L;

	public JLabel lbclose = new JLabel("是否确定关闭？");
	public JButton btyes = new JButton("是");
	public JButton btno = new JButton("否");
	public JPanel CenterPanel = new JPanel();
	public IfListener ifclose = new IfListener();

	public CloseWindowFrame() {
		setSize(250, 120);
		setLayout(new BorderLayout());
		add(lbclose, BorderLayout.NORTH);
		add(CenterPanel, BorderLayout.CENTER);
		CenterPanel.setLayout(new FlowLayout());
		CenterPanel.add(btyes);
		CenterPanel.add(btno);
		btyes.addActionListener(ifclose);
		btno.addActionListener(ifclose);
		addWindowListener(new WindowClose());
	}
	
}


class WindowClose extends WindowAdapter{
    public void windowClosing(WindowEvent e) {
    	//new closeDemo();初始化调用窗口**************************************
    }
}


class IfListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("是")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("否")) {
			//new closeDemo();初始化调用窗口**************************************
			CloseDialog.invisible();
		}

	}

}

