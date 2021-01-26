package toolbox.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class CommandRunner {
    public static String exec(String cmd) {
        Process proc;
        StringBuilder builder = new StringBuilder();
        try {
            proc = Runtime.getRuntime().exec(cmd);

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            in.close();
            proc.waitFor(60, TimeUnit.SECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
