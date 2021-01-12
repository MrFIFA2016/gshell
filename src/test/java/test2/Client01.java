package test2;

import java.io.*;
import java.net.Socket;

public class Client01 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("192.168.1.21", 8000);
        PrintStream ps = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        ps.println("hello word!!");
        ps.flush();

        String info = br.readLine();
        System.out.println(info);
        ps.close();
        br.close();
    }
}