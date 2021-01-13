package toolbox.util;


import toolbox.client.NetClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MyClient {
    public static void main(String[] args) throws IOException {
        NetClient instance = NetClient.getInstance("TCP", "127.0.0.1", 12345, 6000);

        instance.send("hello".getBytes(StandardCharsets.UTF_8));
        byte[] bytes = instance.receive();
        System.out.println(new String(bytes));
    }
}
