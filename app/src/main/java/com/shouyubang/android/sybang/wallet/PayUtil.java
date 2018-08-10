package com.shouyubang.android.sybang.wallet;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.shouyubang.android.sybang.utils.Constants;
import com.tsy.sdk.pay.weixin.WXPay;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import tencent.tls.tools.MD5;

/**
 * Created by wurengao on 17-10-28.
 */

public class PayUtil {

    public static void doWXPay(String pay_param, final Context context) {
        Log.d("微信支付参数",pay_param);
        String wx_appid = Constants.APPID_WACHAT_PAY;     //替换为自己的appid  OK
        WXPay.init(context.getApplicationContext(), wx_appid);      //要在支付前调用

        WXPay.getInstance().doPay(pay_param, new WXPay.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(context.getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int error_code) {
                switch (error_code) {
                    case WXPay.NO_OR_LOW_WX:
                        Toast.makeText(context.getApplicationContext(), "未安装微信或微信版本过低", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY_PARAM:
                        Toast.makeText(context.getApplicationContext(), "参数错误", Toast.LENGTH_SHORT).show();
                        break;

                    case WXPay.ERROR_PAY:
                        Toast.makeText(context.getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(context.getApplicationContext(), "支付取消", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getNonceStr(){
        Random random = new Random();
        long val = random.nextLong();
        String res = MD5.toMD5(val+"xyz").toUpperCase();
        if(32<res.length()) return res.substring(0,32);
        else return res;
    }


    public static String createSign(String characterEncoding, SortedMap<String, String> parameters){
        StringBuilder sb = new StringBuilder();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + Constants.KEY_SIGN);//最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
        String sign = MD5.toMD5(sb.toString()).toUpperCase();
        return sign;
    }

    public static String getParam(String preid) throws JSONException {
        String noncestr=PayUtil.getNonceStr();
        long currentTimeMillis = System.currentTimeMillis();//生成时间戳
        long second = currentTimeMillis / 1000L;//（转换成秒）
        String seconds = String.valueOf(second).substring(0, 10);//（截取前10位）

        SortedMap<String, String> signParam = new TreeMap<String, String>();
        signParam.put("appid", Constants.APPID_WACHAT_PAY);//app_id
        signParam.put("partnerid", Constants.MCH_ID);//微信商户账号
        signParam.put("prepayid", preid);//预付订单id
        signParam.put("package", "Sign=WXPay");//默认sign=WXPay
        signParam.put("noncestr", noncestr);//自定义不重复的长度不长于32位
        signParam.put("timestamp", seconds);//北京时间时间戳

        String signAgain = PayUtil.createSign("UTF-8", signParam);//再次生成签名

        JSONObject result_pre = new JSONObject();
        result_pre.put("appid",Constants.APPID_WACHAT_PAY);
        result_pre.put("partnerid",Constants.MCH_ID);
        result_pre.put("prepayid",preid);
        result_pre.put("package","Sign=WXPay");
        result_pre.put("noncestr",noncestr);
        result_pre.put("timestamp",seconds);
        result_pre.put("sign",signAgain);

        return result_pre.toString();
    }

    public static String setSignAgain(String payID){

        long currentTimeMillis = System.currentTimeMillis();//生成时间戳
        long second = currentTimeMillis / 1000L;//（转换成秒）
        String seconds = String.valueOf(second).substring(0, 10);//（截取前10位）
        String noncestr=PayUtil.getNonceStr();

        SortedMap<String, String> signParam = new TreeMap<String, String>();
        signParam.put("appid", Constants.APPID_WACHAT_PAY);//app_id
        signParam.put("partnerid", Constants.MCH_ID);//微信商户账号
        signParam.put("prepayid", payID);//预付订单id
        signParam.put("package", "Sign=WXPay");//默认sign=WXPay
        signParam.put("noncestr",noncestr);//自定义不重复的长度不长于32位
        signParam.put("timestamp", seconds+"");//北京时间时间戳

        String signAgain = PayUtil.createSign("UTF-8", signParam);//再次生成签名

        JSONObject result_pre = new JSONObject();
        try {
            result_pre.put("appid",Constants.APPID_WACHAT_PAY);
            result_pre.put("partnerid",Constants.MCH_ID);
            result_pre.put("prepayid",payID);
            result_pre.put("package","Sign=WXPay");
            result_pre.put("noncestr",noncestr);
            result_pre.put("timestamp",seconds);
            result_pre.put("sign",signAgain);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //用订单号去启动微信支付

        return result_pre.toString();
    }
}
