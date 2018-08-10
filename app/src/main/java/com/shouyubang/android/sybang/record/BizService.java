package com.shouyubang.android.sybang.record;

import android.content.Context;
import android.util.Log;

import com.shouyubang.android.sybang.utils.Constants;
import com.tencent.cos.COSClient;
import com.tencent.cos.COSConfig;
import com.tencent.cos.common.COSEndPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by bradyxiao on 2017/4/11.
 * author bradyxiao
 */
public class BizService {

    private static final String TAG = "BizService";

    /** cos的appid */
    public String appid;
    /** appid的一个空间名称 */
    public  String bucket;
    /** cos sdk 的用户接口  */
    public  COSClient cosClient;

    /** 设置园区；根据创建的cos空间时选择的园区
     * 华南园区：gz 或 COSEndPoint.COS_GZ(已上线)
     * 华北园区：tj 或 COSEndPoint.COS_TJ(已上线)
     * 华东园区：sh 或 COSEndPoint.COS_SH*/
    /** cos sdk 配置设置; 根据需要设置*/
    private COSConfig config;

    private static BizService bizService;
    private BizService(){}
    private static byte[] syncObj = new byte[0];

    public static BizService instance(){
        synchronized (syncObj){
            if(bizService == null){
                bizService = new BizService();
            }
            return bizService;
        }
    }

    public void init(Context context){
        synchronized (this){
            if(cosClient == null){
                config = new COSConfig();
                config.setEndPoint(COSEndPoint.COS_SH);
                appid = Constants.COS_APP_ID;
                bucket = Constants.VIDEO_BUCKET;
                cosClient = new COSClient(context,appid,config,"kliu");
            }
        }
    }



    /***
     *  签名， 参考官网的签名方式，自己搭建签名服务器；
     *  签名类型：多次签名 ，单次签名
     */
    /**
     *
     * @return 返回多次签名串
     */
    public String getSign(){
        String sign = null;
        String cgi = Constants.AUTH_URL + "/getSign?" + "bucket=" + bucket + "&service=video";
        try {
            URL url = new URL(cgi);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = bufferedReader.readLine();
            if(line == null)return  null;
            JSONObject json = new JSONObject(line);
            if(json.has("data")){
                sign = json.getString("data");
            }
            Log.w(TAG,"sign = " +sign);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     *
     * @return 返回单次签名串
     */
    public  String getSignOnce(String fileId){
        urlEncoder(fileId);
        String onceSign = null;
        String cgi = Constants.AUTH_URL + "/getSignOnce?"+ "bucket=" +bucket + "&service=cos&expired=0&path=" + fileId;
        try {
            URL url = new URL(cgi);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = bufferedReader.readLine();
            if(line == null)return  null;
            JSONObject json = new JSONObject(line);
            if(json.has("sign")){
                onceSign = json.getString("sign");
            }
            Log.w(TAG,"onceSign =" + onceSign);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return onceSign;
    }

    private  String urlEncoder(String fileID){
        if(fileID == null){
            return fileID;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String[] strFiled = fileID.trim().split("/");
        int length = strFiled.length;
        for(int i = 0; i< length; i++){
            try{
                String str = URLEncoder.encode(strFiled[i], "utf-8").replace("+","%20");
                stringBuilder.append(str).append("/");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(fileID.startsWith("/")){
            fileID = "/" + stringBuilder.toString();
        }else{
            fileID = stringBuilder.toString();
        }
        return fileID;
    }
}
