package toolbox.util;

import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ClientBox {
    Logger logger = Logger.getLogger("OKClient");

    boolean verbose = true;

    public static OkHttpClient createClient(boolean isHttps, boolean verbose) {
        ClientBox cbox = new ClientBox();
        cbox.verbose = verbose;
        return cbox.createClient(isHttps);
    }

    private ClientBox() {
    }

    private OkHttpClient createClient(boolean isHttps) {
        if (!isHttps) {
            return new OkHttpClient.Builder()
                    .addNetworkInterceptor(new UnzippingInterceptor()).build();
        }
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
                if (false) {
                    for (Certificate c : localCertificates) {
                        logger.info("verify: " + c.toString());
                    }
                }
                return true;
            }
        });
        return builder.addInterceptor(new UnzippingInterceptor()).build();
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
