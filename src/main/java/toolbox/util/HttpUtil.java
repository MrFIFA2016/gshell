package toolbox.util;

import okhttp3.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HttpUtil {

    static Logger logger = Logger.getLogger("HttpUtil");

    static Headers DEFAULT_HEADERS = Headers.of(
            "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv,84.0) Gecko/20100101 Firefox/84.0",
            "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2",
            "Accept-Encoding", " gzip, deflate, br",
            "Connection", "keep-alive", "Upgrade-Insecure-Requests", "1");

    static boolean verbose = true;
    final static OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new UnzippingInterceptor()).build();

    public static String get(String url) {
        Request request = new Request.Builder().url(url).headers(DEFAULT_HEADERS).get().build();
        return execNewCall(request);
    }

    private static String execNewCall(Request request) {
        Response response = null;
        try {

            response = client.newCall(request).execute();

            if (verbose) {
                printHeader(request.headers(), "request");
                System.out.println("\r\n");
                printHeader(response.headers(), "response");
            }
            if (!response.isSuccessful()) {
                System.err.println("返回数据异常 Code:" + response.code() + " Message:" + response.message());
                return null;
            }
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    public static String post(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .headers(DEFAULT_HEADERS)
                .build();
        return execNewCall(request);
    }

    public static String postJsonParams(String url, String jsonParams) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return execNewCall(request);
    }

    private static void printHeader(Headers headers, String label) {
        Map<String, List<String>> multimap = headers.toMultimap();
        StringBuilder kv = new StringBuilder();
        kv.append(label + " header:\r\n");
        kv.append("------------------------------------------------\r\n");
        Iterator<Map.Entry<String, List<String>>> it = multimap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            String name = entry.getKey();
            List<String> values = entry.getValue();
            kv.append("      ");
            kv.append(name).append(" -> ").append(values);
            kv.append("\r\n");
        }
        kv.append("------------------------------------------------\r\n");
        System.out.print(kv.toString());
    }

    public static void main(String[] args) {
        String s = get("http://www.iwhere.com");
        //System.out.print(s);
        //String s1 = StringFormatUtil.formatToHexStringWithASCII(s.getBytes(), 0, s.getBytes().length, "响应");
        Inspector.inspect(s.getBytes(),"响应");
        //System.out.print(s1);
    }
}
