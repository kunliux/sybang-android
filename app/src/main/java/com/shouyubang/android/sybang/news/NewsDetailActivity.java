package com.shouyubang.android.sybang.news;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shouyubang.android.sybang.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by 吴任高 on 2017/10/8.
 */

public class NewsDetailActivity extends AppCompatActivity {
    private WebView mWebView;
    private String mId;
    private static final String NEWSAPI="http://www.shouyubang.com:8080/syb/api/news/";

    @Override
    protected void onCreate(Bundle savedInstanced){
        super.onCreate(savedInstanced);
        setContentView(R.layout.activity_news_detail);
        Intent intent=getIntent();
        mId=NEWSAPI+intent.getIntExtra("extra_id",0);
        mWebView =(WebView) findViewById(R.id.news_detail_web);
        mWebView.setWebViewClient(new WebViewClient());//设置浏览器解析器

        new FetchItemTask().execute();

    }
    private class FetchItemTask extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void...params){
            return new NewsDetailFetch().fetchDetail(mId);
        }
        @Override
        protected void onPostExecute(String NewsHteml){
            mWebView.loadData(NewsHteml.replace("\\",""),"text/html; charset=UTF-8", null);
        }

    }
    //联网专用类
    private class NewsDetailFetch{
        public String getUrlString(String urlSpec) throws IOException{
            URL url=new URL(urlSpec);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();

            try{
                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();//用于储存数据对象
                InputStream inputStream=connection.getInputStream();

                if(connection.getResponseCode()!=HttpURLConnection.HTTP_OK){
                    throw new IOException(connection.getResponseMessage()+": with"+urlSpec);
                }
                int byteRead=0;
                byte []buffer=new byte[1024];//一次读取数据量1K
                while((byteRead=inputStream.read(buffer))>0){
                    outputStream.write(buffer,0,byteRead);
                }
                outputStream.close();
                return new String(outputStream.toByteArray());
            }finally {
                connection.disconnect();//关闭网络连接
            }

        }
        public String fetchDetail(String urlSpec){
            String mNewsDetail =null;
            try{
                String jsonString=new NewsDetailFetch().getUrlString(urlSpec);//获取json字符串
                JSONObject jsonObject=new JSONObject(jsonString);
                mNewsDetail =paresNewsDetail(jsonObject);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mNewsDetail;
        }
        private String paresNewsDetail(JSONObject jsonObject)throws JSONException{
            return new String(jsonObject.getString("body"));
        }

    }

}
