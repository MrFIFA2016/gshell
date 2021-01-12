package test2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server01 {
    public static void main(String[] args) throws IOException {
        //创建一个服务器端的Socket
        ServerSocket serverSocket = new ServerSocket(8000);
        System.out.println("服务器已经启动，等待客户端连接");
        //侦听，如果连接成功，则返回一个Socket对象
        Socket socket = serverSocket.accept();
        System.out.println("收到socket:" + socket.toString());
        //读取客户端的数据
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String str = br.readLine();
        System.out.println("接受到客户端的消息：");
        System.out.println(str);

        //服务器向客户端写入数据
        PrintStream ps = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
        ps.println("you are so cute I like you so much");
        ps.flush();
        ps.close();
        br.close();
    }
}