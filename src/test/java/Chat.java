import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 模拟单人聊天室
 *
 * @author liuzeyu12a
 */
public class Chat {
    public static void main(String[] args) throws Exception {
        //建立服务器端套接字，绑定本地端口
        ServerSocket server = new ServerSocket(9999);
        // while (true) {
        // new Thread(() -> {
        Socket client = null;
        try {
            //监听客户端
            client = server.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("一个客户端建立了连接...");

        //接受客户端的消息
        DataInputStream dis = null;
        DataOutputStream dos = null;
        try {
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean isRunning = true;
        while (isRunning) {
            String msg = null;
            try {
                msg = dis.readUTF();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //返回回去给客户端
            try {
                dos.writeUTF("Hello " + msg);
                dos.flush();
            } catch (IOException e) {
                isRunning = false;  //客户端断开即停止读写数据
            }
        }

        //释放资源
        try {
            if (null != dos)
                dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (null != dis)
                dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (null != client)
                client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //}).start();

        // }
    }
}