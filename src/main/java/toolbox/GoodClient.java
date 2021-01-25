package toolbox;

import toolbox.util.ClientBox;
import toolbox.util.Inspector;

public class GoodClient extends AbstractOKClient {
    private GoodClient() {
    }

    private static GoodClient client;


    public static GoodClient getInstance(boolean isHttps) {
        if (client == null) {
            client = new GoodClient();
            client.okHttpClient = ClientBox.getClient(isHttps, client.verbose);
        }
        return client;
    }

    public static void main(String[] args) {
        GoodClient client = GoodClient.getInstance(true);
        client.verbose = true;
        String s = client.get("https://8.129.48.70");
        Inspector.inspect(s.getBytes(), "响应");
    }
}
