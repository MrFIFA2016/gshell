package toolbox.util;

import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ClientBox {
    static Logger logger = Logger.getLogger("OKClient");

    private ClientBox() {
    }

    static boolean verbose = true;

    public static OkHttpClient getClient(boolean isHttps, boolean verbose) {
        ClientBox.verbose = verbose;
        if (isHttps)
            return secClient;
        return client;
    }

    static OkHttpClient client;
    static OkHttpClient secClient;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);

        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        try {
            trustManager = new TrustAllManager();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        builder.sslSocketFactory(sslSocketFactory, trustManager);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                Certificate[] localCertificates = new Certificate[0];
                try {
                    //获取证书链中的所有证书
                    localCertificates = session.getPeerCertificates();
                } catch (SSLPeerUnverifiedException e) {
                    e.printStackTrace();
                }
                //打印所有证书内容
                if (verbose) {
                    for (Certificate c : localCertificates) {
                        logger.info("verify: " + c.toString());
                    }
                }
                return true;
            }
        });
        secClient = builder.addInterceptor(new UnzippingInterceptor()).build();
        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new UnzippingInterceptor()).build();
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
