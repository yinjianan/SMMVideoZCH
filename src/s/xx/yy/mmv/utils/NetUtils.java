package s.xx.yy.mmv.utils;

import com.tandong.sa.loopj.AsyncHttpClient;

/**
 * Created by Administrator on 2016/3/16.
 */
public class NetUtils {
    private static AsyncHttpClient client;

    public static AsyncHttpClient getInstance() {
        if (client == null)
            client = new AsyncHttpClient();
        return client;
    }
}
