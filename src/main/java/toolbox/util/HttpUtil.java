package toolbox.util;

import okhttp3.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class HttpUtil {

    static Logger logger = Logger.getLogger("HttpUtil");

    static boolean verbose = true;
    final static OkHttpClient client = new OkHttpClient.Builder().build();

    public static String get(String url) {
        Request request = new Request.Builder().url(url).get().build();
        return execNewCall(request);
    }

    private static String execNewCall(Request request) {
        Response response = null;
        try {
            response = client.newCall(request).execute();

            if (verbose) {
                printHeader(request.headers(),"request");
                System.out.println("\r\n");
                printHeader(response.headers(),"response");
            }
            if (response.isSuccessful()) {
                return response.body().string();
            }
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
        String s = get("http://www.baidu.com");
        System.out.print(s);
    }
}
