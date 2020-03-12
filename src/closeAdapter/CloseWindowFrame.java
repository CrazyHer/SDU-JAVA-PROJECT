package closeAdapter;

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


public class CloseWindowFrame extends JDialog{
	
	private static final long serialVersionUID = 1L;

	public JLabel lbclose=new JLabel("是否确定关闭？");
	public JButton btyes=new JButton("是");
	public JButton btno=new JButton("否");
	public IfListener ifclose=new IfListener();
	public JPanel CenterPanel=new JPanel();

	public CloseWindowFrame() {
		setSize(250, 120);
		setLayout(new BorderLayout());
		add(lbclose,BorderLayout.NORTH);
		add(CenterPanel,BorderLayout.CENTER);
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
		if(e.getActionCommand().equals("是")) {
			System.exit(0);
			System.out.println("窗口已关闭!");
		}
		else if(e.getActionCommand().equals("否")) {
			//new closeDemo();初始化调用窗口**************************************
			CloseDialog.invisible();
			System.out.println("已取消关闭窗口!");
		}

	}

}

