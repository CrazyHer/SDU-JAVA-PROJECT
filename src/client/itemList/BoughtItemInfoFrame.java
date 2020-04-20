package client.itemList;

import server.dataObjs.BuyItemData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import static client.userInfo.UserInfo.user;

public class BoughtItemInfoFrame extends JFrame implements ActionListener {
    static final String HOST = "192.168.1.103"; //连接地址
    static final int PORT = 2333; //连接端口
    public JPanel panel;
    public JButton btBuy;
    public ItemInfo itemInfo = new ItemInfo();
    Socket socket;

    public BoughtItemInfoFrame() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        setTitle("商品购买信息");
        setSize(500, 400);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new JPanel();
        panel.add(itemInfo);
        c.add(panel, BorderLayout.CENTER);

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        btBuy = new JButton("购买");
        panel.add(btBuy, new GBC(0, 0, 1, 1).setAnchor(GridBagConstraints.EAST));
        panel.add(new JPanel(), new GBC(0, 2, 1, 1));
        c.add(panel, BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("购买")) {
            try {
                socket = new Socket("localhost", PORT); //创建客户端套接字
                System.out.println("成功连接" + socket.getRemoteSocketAddress());
                //客户端输出流，向服务器发消息
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                PrintWriter out = new PrintWriter(bw, true);//不自动刷新的话写完会阻塞
                //out.println("LOGIN");
                ObjectOutputStream obOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                obOut.writeObject(new BuyItemData(user.getID(), itemInfo.getName()));
                //客户端输入流，接收服务器消息
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                if (in.readLine().equals("1")) System.out.println("购买成功");
                else if (in.readLine().equals("0")) System.out.println("交易已经在进行中，无法重复购买");
                else if (in.readLine().equals("-1")) System.out.println("购买失败");
                obOut.flush();
                out.println("CLOSE SERVER");//发送关闭服务器指令
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
                if (null != socket) {
                    try {
                        socket.close(); //断开连接
                        System.out.println("已断开连接");
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

}
