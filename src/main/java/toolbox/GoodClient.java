package toolbox;

import toolbox.util.ClientBox;

public class GoodClient extends AbstractOKClient {
    private GoodClient() {
    }

    public static GoodClient getHttpClient(boolean verbose) {
        GoodClient client = new GoodClient();
        client.verbose = verbose;
        client.okHttpClient = ClientBox.createClient(false, client.verbose);
        return client;
    }

    public static GoodClient getHttpsClient(boolean verbose) {
        GoodClient client = new GoodClient();
        client.verbose = verbose;
        client.okHttpClient = ClientBox.createClient(true, client.verbose);
        return client;
    }
}
