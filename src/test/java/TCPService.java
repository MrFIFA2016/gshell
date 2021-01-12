import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPService {
    public static final String SERVICE_IP = "127.0.0.1";

    public static final int SERVICE_PORT = 8189;

    public static void main(String[] args) {
        TCPService service = new TCPService();
        //启动服务端
        service.startService(SERVICE_IP, SERVICE_PORT);
    }

    private void startService(String serverIP, int serverPort) {
        try {
            //封装服务端地址
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            //建立服务端
            try (ServerSocket service = new ServerSocket(serverPort, 10, serverAddress)) {
                while (true) {
                    //接受一个连接，该方法会阻塞程序，直到一个链接到来
                    System.out.println("监听连接...");
                    try (Socket connect = service.accept()) {
                        //获得输入流
                        System.out.println("有客户端连接！" + connect.getRemoteSocketAddress());

                        DataInputStream in = new DataInputStream(connect.getInputStream());
                        //获取输出流，并通过向输出流写数据的方式发送响应
                        OutputStream out = connect.getOutputStream();

                        InputStreamReader isr = new InputStreamReader(in);

                        BufferedReader br = new BufferedReader(isr);
                        String info = null;
                        while ((info = br.readLine()) != null) {
                            System.out.println("我是服务器，客户端说：" + info);
                        }
                        br.close();
                        isr.close();
                        in.close();
                        //connect.shutdownInput();//关闭输入流

                        //组建响应信息
                        //String response = "Hello world " + data;
//                        out.write("Hello!".getBytes());
//                        out.flush();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}