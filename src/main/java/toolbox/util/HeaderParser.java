package toolbox.util;

import okhttp3.Headers;

import java.util.LinkedHashMap;
import java.util.Map;

public class HeaderParser {
    public static Headers forMap(Map<String, String> headerMap) {
        Headers headers = Headers.of(headerMap);
        return headers;
    }

    public static Headers forMaoHaoStr(String str) {
        Map map = new LinkedHashMap();
        String[] val = str.split("\n");
        for (String line : val) {
            String[] kv = line.split(":");
            map.put(kv[0].trim(), kv[1].trim());
        }
        Headers headers = Headers.of(map);
        return headers;
    }
}
