package toolbox;

import toolbox.util.Inspector;
import toolbox.util.StringFormatUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author liubo
 * @date 2021/1/11 11:51
 * @description
 */
public class TcpTool {

    static Logger logger = Logger.getLogger("TcpTool");

    /**
     * 发送TCP请求
     *
     * @param IP         远程主机地址
     * @param port       远程主机端口
     * @param reqData    待发送报文的中文字符串形式
     * @param reqCharset 该方法与远程主机间通信报文的编码字符集(编码为byte[]发送到Server)
     * @return localPort--本地绑定的端口,reqData--请求报文,respData--响应报文,respDataHex--远程主机响应的原始字节的十六进制表示
     * @see 本方法默认的连接超时和读取超时均为30秒
     * @see 编码与解码请求响应字节时,均采用双方约定的字符集,即本方法的第四个参数reqCharset
     */
    public static Map<String, String> sendTCPRequest(String IP, Integer port, String reqData, String reqCharset) {


        Map<String, String> respMap = new HashMap<String, String>();
        OutputStream out = null;      //写
        InputStream in = null;        //读
        String localPort = null;      //本地绑定的端口(java socket, client, /127.0.0.1:50804 => /127.0.0.1:9901)
        String respData = null;       //响应报文
        String respDataHex = null;    //远程主机响应的原始字节的十六进制表示
        Socket socket = new Socket(); //客户机
        try {
            socket.setTcpNoDelay(true);
            socket.setReuseAddress(true);
            socket.setSoTimeout(30000);
            socket.setSoLinger(true, 5);
            socket.setSendBufferSize(1024);
            socket.setReceiveBufferSize(1024);
            socket.setKeepAlive(true);
            socket.connect(new InetSocketAddress(IP, port), 30000);
            localPort = String.valueOf(socket.getLocalPort());

            String time = new SimpleDateFormat("[HH:mm:ss SSS]").format(new Date());

            logger.info(String.format("%s建立TCP连接 %s - %s  | Port: %s - > %s", time, socket.getLocalAddress().getHostAddress(), IP, localPort, port));

            printReq(reqData);

            /**
             * 发送TCP请求
             */
            out = socket.getOutputStream();
            out.write(reqData.getBytes());
            /**
             * 接收TCP响应
             */
            in = socket.getInputStream();

            ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            byte[] buffer = new byte[4];
            int len;
            try {
                while ((len = in.read(buffer)) != -1) {
                    bytesOut.write(buffer, 0, len);
//                    if (len < buffer.length)
//                        break;
                }
            } catch (IOException e) {
                System.err.println(e);
            }
            /**
             * 解码TCP响应的完整报文
             */
            respData = bytesOut.toString(reqCharset);
            //respDataHex = formatToHexStringWithASCII(bytesOut.toByteArray(), "收到数据");
            Inspector.inspect(bytesOut.toByteArray(), "收到数据");
        } catch (Exception e) {
            System.out.println("与[" + IP + ":" + port + "]通信遇到异常,堆栈信息如下");
            e.printStackTrace();
        } finally {
            if (null != socket && socket.isConnected() && !socket.isClosed()) {
                try {
                    out.close();
                    in.close();
                    socket.close();
                    String time = new SimpleDateFormat("[HH:mm:ss SSS]").format(new Date());

                    logger.info(String.format("%s关闭TCP连接", time));

                } catch (IOException e) {
                    System.out.println("关闭客户机Socket时发生异常,堆栈信息如下");
                    e.printStackTrace();
                }
            }
        }
        respMap.put("localPort", localPort);
        respMap.put("reqData", reqData);
        respMap.put("respData", respData);
        //respMap.put("respDataHex", respDataHex);
        return respMap;
    }

    /**
     * 通过ASCII码将十进制的字节数组格式化为十六进制字符串
     *
     * @see 该方法会将字节数组中的所有字节均格式化为字符串
     * @see 使用说明详见<code>formatToHexStringWithASCII(byte[], int, int)</code>方法
     */
    private static String formatToHexStringWithASCII(byte[] data, String label) {
        return StringFormatUtil.formatToHexStringWithASCII(data, 0, data.length, label);
    }

    private static void printReq(String req) {
//        System.out.println("TCP打印参数：-->");
//        String str = formatToHexStringWithASCII(req.getBytes(), "请求参数");
//        System.out.println(str);
//        System.out.println("\r\n<--结束参数打印");
        Inspector.inspect(req.getBytes(), "TCP发送数据");
    }


}
