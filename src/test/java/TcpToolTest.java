import toolbox.util.TcpTool;

import java.util.Map;

/**
 * @author liubo
 * @date 2021/1/11 14:52
 * @description
 */
public class TcpToolTest {
    public static void main(String[] args) {
        Map<String, String> ping = TcpTool.sendTCPRequest("localhost", 8000, "ping", "utf-8");
        System.out.printf(ping.toString());
    }
}
