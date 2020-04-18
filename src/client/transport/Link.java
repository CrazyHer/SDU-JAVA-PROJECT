package client.transport;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Link {
    static final String HOST="192.168.1.103"; //连接地址
    static final int PORT=2333; //连接端口
    Socket socket;
    public Link() {
    }

    public void send(Object o) {
        try {
            socket=new Socket("localhost",PORT); //创建客户端套接字
            System.out.println("成功连接" + socket.getRemoteSocketAddress());
            //客户端输出流，向服务器发消息
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            PrintWriter out = new PrintWriter(bw, true);//不自动刷新的话写完会阻塞
            //客户端输入流，接收服务器消息
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //out.println("LOGIN");
            ObjectOutputStream obOut = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            //obOut.writeObject(new LoginData("何锐","201900301198","666666"));
            obOut.writeObject(o);
            obOut.flush();
            out.println("CLOSE SERVER");//发送关闭服务器指令
        } catch (IOException e1) {
            e1.printStackTrace();
        }finally{
            if(null!=socket){try {
                socket.close(); //断开连接
                System.out.println("已断开连接");
            } catch (IOException e2) {
                e2.printStackTrace();
            }}
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        new Link().send("hello");
    }



}
