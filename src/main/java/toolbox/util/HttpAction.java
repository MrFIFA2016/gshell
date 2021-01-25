package toolbox.util;

import java.util.Map;

public interface HttpAction {
    String post(String url, Map<String, String> params);
    String get(String url);
}
