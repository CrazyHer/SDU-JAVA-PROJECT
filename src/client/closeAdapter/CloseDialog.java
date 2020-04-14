package client.closeAdapter;


import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//用法说明***************************************************************************************

//LoginFrame已经实现，可以参考！！！！！！！！！！！

//在构造方法中加上下两句
// setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
// addWindowListener(new WindowClose());
//在窗口最后编写这个类
class WindowClose extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
        int i = JOptionPane.showConfirmDialog(null,"是否关闭","提示",JOptionPane.YES_NO_OPTION);
        if(i==0){
            System.exit(0);
        }
    }
}