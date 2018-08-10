package com.shouyubang.android.sybang.utils;

import com.tencent.ilivesdk.core.ILiveLoginManager;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wurengao on 17-10-27.
 */

public class PostUtil {
    public int feedbackPost(String urlStr,String title,String feedback_content) throws IOException, JSONException {
        URL url=new URL(urlStr);
        JSONObject ClientKey = new JSONObject();
        ClientKey.put("userId", ILiveLoginManager.getInstance().getMyUserId());
        ClientKey.put("title", title);
        ClientKey.put("content", feedback_content);

        String content = String.valueOf(ClientKey);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        // 设置允许输出
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        // 设置User-Agent: Fiddler
        conn.setRequestProperty("ser-Agent", "Fiddler");
        // 设置contentType
        conn.setRequestProperty("Content-Type","application/json");
        OutputStream os = conn.getOutputStream();
        os.write(content.getBytes());
        os.close();

        int code = conn.getResponseCode();
        return code;
    }
}
