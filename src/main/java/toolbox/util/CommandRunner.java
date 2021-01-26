package toolbox.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class CommandRunner {
    public static String exec(String cmd) {
        final Process[] proc = new Process[1];
        final StringBuilder builder = new StringBuilder();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    proc[0] = Runtime.getRuntime().exec(cmd);
                    BufferedReader in = new BufferedReader(new InputStreamReader(proc[0].getInputStream()));
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        builder.append(line);
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        long begin = System.currentTimeMillis();
        while (System.currentTimeMillis() - begin < 10 * 1000) {//10秒钟超时
            try {
                String str = builder.toString();
                if (StringUtils.isNotBlank(str))
                    return str;
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        proc[0].destroyForcibly();
        return builder.toString();
    }
}

