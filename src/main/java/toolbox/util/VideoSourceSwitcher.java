package toolbox.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Random;

public class VideoSourceSwitcher {
    final static String userNameOrigin = "admin";
    final static String pwdOrigin = "a123456789";

    final static HttpClient client = new DefaultHttpClient(); // 构建一个Client

    private VideoSourceSwitcher() {
    }

    static String validCookie;

    public static void main(String[] args) {
        switchTo("http://220.250.59.233:10081/ISAPI/PTZCtrl/channels/1/presets/3/goto");
    }

    /**
     * 登录并获得有效Cookies
     *
     * @param newSource
     * @return
     */
    public static boolean switchTo(String newSource) {

        if (StringUtils.isBlank(validCookie)) {
            reReadCookies();
        }

        boolean cookieValid = requestWithCookie(newSource, validCookie);

        //cookie无效重新登陆
        if (!cookieValid) {
            reReadCookies();
            return requestWithCookie(newSource, validCookie);
        }
        return true;
    }

    private static boolean reReadCookies() {
        try {
            validCookie = readCookies(userNameOrigin, pwdOrigin);
            System.out.println("获得Cookie：" + validCookie);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean requestWithCookie(String url, String cookies) {
        HttpPut post = new HttpPut(url);
        post.addHeader("Cookie", cookies);
        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            SAXReader reader = new SAXReader();
            try {
                org.dom4j.Document doc = reader.read(new StringReader(content));
                Element root = doc.getRootElement();
                String statusCode = root.element("statusCode").getText();
                if (Integer.valueOf(statusCode) == 1)
                    return true;

            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        post.releaseConnection();
        return false;
    }


    private static String readCookies(String userNameOrigin, String pwdOrigin) throws Exception {
        Random rdm = new Random();

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append("http://");
        urlBuilder.append(userNameOrigin);
        urlBuilder.append(":");
        urlBuilder.append(pwdOrigin);
        urlBuilder.append("@220.250.59.233:10081/ISAPI/Security/sessionLogin/capabilities?username=admin&random=");
        urlBuilder.append(Math.abs(rdm.nextInt()));

        HttpGet get = new HttpGet(urlBuilder.toString());
        HttpResponse response2 = client.execute(get);
        HttpEntity httpEntity = response2.getEntity();
        String content = EntityUtils.toString(httpEntity);

        SAXReader reader = new SAXReader();
        org.dom4j.Document doc = reader.read(new StringReader(content));
        Element root = doc.getRootElement();
        String sessionID = root.element("sessionID").getText();
        String salt = root.element("salt").getText();
        String challenge = root.element("challenge").getText();
        int iterations = Integer.valueOf(root.element("iterations").getText());
        boolean isIrreversible = Boolean.valueOf(root.element("isIrreversible").getText());

        get.releaseConnection();

        HttpPost post = new HttpPost(
                "http://220.250.59.233:10081/ISAPI/Security/sessionLogin?timeStamp=" + System.currentTimeMillis());

        String saltedPwd = getSaltedPwd(pwdOrigin, userNameOrigin, salt, isIrreversible, iterations, challenge);

        String form = "<SessionLogin><userName>admin</userName><password>" + saltedPwd + "</password>" + "<sessionID>"
                + sessionID + "</sessionID>"
                + "<isSessionIDValidLongTerm>false</isSessionIDValidLongTerm><sessionIDVersion>2</sessionIDVersion>"
                + "</SessionLogin>";

        post.setEntity(new StringEntity(form, Charset.defaultCharset()));

        HttpResponse loginresp = client.execute(post);
        post.releaseConnection();
        Header[] headers = loginresp.getHeaders("Set-Cookie");
        String cookies = headers[0].getValue();
        return cookies;
    }

    private static String getSaltedPwd(String pwd, String userName, String salt, boolean isIrreversible, int iterations,
                                       String challenge) {
        String i = "";
        if (isIrreversible) {
            i = EncryptUtils.SHA256(userName + salt + pwd);
            i = EncryptUtils.SHA256(i + challenge);
            for (int n = 2; iterations > n; n++)
                i = EncryptUtils.SHA256(i);
        } else {
            i = EncryptUtils.SHA256(pwd) + challenge;
            for (int n = 1; iterations > n; n++)
                i = EncryptUtils.SHA256(i);
        }
        return i;
    }
}
