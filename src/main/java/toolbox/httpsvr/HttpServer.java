package toolbox.httpsvr;

import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable {

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();
                // 当有客户端请求过来，为连接创建Request和Response对象
                HttpRequest request = new HttpRequest(socket.getInputStream());
                HttpResponse response = new HttpResponse(socket.getOutputStream(), request);
                // 交给处理器去处理
                new HttpProcessor().process(request, response);
                socket.shutdownOutput();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new HttpServer().start();
    }
}