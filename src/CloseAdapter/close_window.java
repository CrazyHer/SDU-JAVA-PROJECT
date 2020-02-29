package CloseAdapter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class close_window extends JDialog{
	
	private static final long serialVersionUID = 1L;

	public close_window() {
		setSize(250, 120);
		JLabel lbclose=new JLabel("是否确定关闭？");
		JButton btyes=new JButton("是");
		JButton btno=new JButton("否");
		setLayout(new BorderLayout());
		add(lbclose,BorderLayout.NORTH);
		JPanel CenterPanel=new JPanel();
		add(CenterPanel,BorderLayout.CENTER);
		CenterPanel.setLayout(new FlowLayout());
		CenterPanel.add(btyes);	
		CenterPanel.add(btno);	
		ifListener ifclose=new ifListener();
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


class ifListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("是")) {
			System.exit(0);
		}
		else if(e.getActionCommand().equals("否")) {
			//new closeDemo();初始化调用窗口**************************************
			closeDialog.invisible();
		}

	}

}

