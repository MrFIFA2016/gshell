import toolbox.util.TcpTool;

import java.util.Map;

/**
 * @author liubo
 * @date 2021/1/11 14:52
 * @description
 */
public class TcpToolTest {
    public static void main(String[] args) {
//        Map<String, String> ping = TcpTool.sendTCPRequest("127.0.0.1", 58938, "ping", "utf-8");

        String req = "ping";

        Map<String, String> ping = TcpTool.sendTCPRequest("127.0.0.1", 12345, req, "utf-8");
        System.out.println(ping.toString());
    }
}
