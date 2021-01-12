import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * TCP模拟单人聊天室
 * @author liuzeyu12a
 *
 */
public class Client {
    public static void main(String[] args) throws IOException, IOException {
        //建立客户端套接字
        Socket client = new Socket("localhost",9999);
        System.out.println("启动Client...");
        //发送数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        DataInputStream dis = new DataInputStream(client.getInputStream());
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        boolean isRunning = true;
        while(isRunning) {
            String msg = reader.readLine();
            dos.writeUTF(msg);
            //客户端接收服务器的响应

            String respond = dis.readUTF();
            System.out.println(respond);
        }
        //关闭
        client.close();
    }
}