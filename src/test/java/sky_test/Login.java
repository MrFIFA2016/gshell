package sky_test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Headers;
import toolbox.GoodClient;
import toolbox.util.HeaderParser;

import java.util.Date;

public class Login {

    static Headers createHeaders() {
        String headers =
                "Host: live-as-sky-adr.game.163.com\n" +
                        "User-Agent: Sky-Live-com.netease.sky/0.6.8.158448 (Google Pixel; android 26.0.0; zh-Hans)\n" +
                        "X-Session-ID: 5c64cb17-fdfa-4f2a-b8dc-9a07367eafb0\n" +
                        //"Content-Length: 635\n" +
                        "Connection: close";
        return HeaderParser.forMaoHaoStr(headers);
    }

    public static void main(String[] args) {
        GoodClient client = GoodClient.getHttpsClient(true);
        client.setHeaders(createHeaders());

//        String content = FileUtil.readString("xxx.json", Charset.defaultCharset());
//        JSONArray arr = (JSONArray) JSONArray.parse(content);

        String json = "{\"user\":\"32e1e8ae-9996-41a1-bcdf-d84d7733a991\",\"device\":\"c4161b6c-2b44-46ad-9cd6-d9991f941218\",\"key\":\"a312660061584241f7f7e8cfa7e172821bc0d50d1a0813fb72a13d4975f5bb57\",\"language\":\"zh-Hans\",\"tos_version\":0,\"device_key\":\"Ah5adWk6yZqFBr8HO27VNzUcNCs6sQqO1fakcsBSFbuf\",\"sig_ts\":1610504362,\"sig\":\"MEUCIB2YoNt6ucvbnhig1Oeu5NRtpiwX2kjF03CRdcX/cpQlAiEAsPtaY/H0P42N8K23l01s7fs7a88wQeIcDOOH3Vrg/y8=\",\"request_recovery_token\":true,\"jailBreakValue\":1,\"hashes\":[1246829562,3755172437,3977864835,450399714,208793002,32586419,451640644,3699297719,1454439599,180101562,28605820,3062676827,2183708413,1343257052,2766223387,1043755953],\"integrity\":true}";
        JSONObject obj = (JSONObject) JSONObject.parse(json);
        obj = setValue(obj);

        String s = client.postJsonParams("https://live-as-sky-adr.game.163.com/account/login", obj.toString());
        JSONObject res = (JSONObject) JSONObject.parse(s);
        System.out.println(JSON.toJSONString(res, true));
        //Inspector.inspect(s.getBytes(), "响应");
    }

    private static JSONObject setValue(JSONObject obj) {
        String devicekey = obj.getString("device_key");
        Date date = new Date();
        long time = date.getTime() / 1000;
        String sign = getSign(devicekey, time);
        obj.put("sig", sign);
        obj.put("sig_ts", time);
        return obj;
    }

    private static String getSign(String devicekey, Long ts) {
        //TODO reqDCJ
        GoodClient client = GoodClient.getHttpClient(false);
        String param = String.format("http://127.0.0.1:5000/getsig?device_key=%s&sig_ts=%s", devicekey, ts);
        String sig = client.get(param);

        //String sig = CommandRunner.exec("python");
        return sig;

        //return "MEUCIB2YoNt6ucvbnhig1Oeu5NRtpiwX2kjF03CRdcX/cpQlAiEAsPtaY/H0P42N8K23l01s7fs7a88wQeIcDOOH3Vrg/y8=";
    }
}
