package com.shouyubang.android.sybang.presenters;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.shouyubang.android.sybang.model.ApplicationInfo;
import com.shouyubang.android.sybang.model.Comment;
import com.shouyubang.android.sybang.model.CurVideoInfo;
import com.shouyubang.android.sybang.model.Job;
import com.shouyubang.android.sybang.model.MySelfInfo;
import com.shouyubang.android.sybang.model.RequestBackInfo;
import com.shouyubang.android.sybang.model.Staff;
import com.shouyubang.android.sybang.model.UserProfile;
import com.shouyubang.android.sybang.model.VideoMsg;
import com.shouyubang.android.sybang.utils.Constants;
import com.shouyubang.android.sybang.utils.HttpUtil;
import com.shouyubang.android.sybang.wallet.PayUtil;
import com.tencent.av.sdk.NetworkHelp;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络请求类
 */
public class UserServerHelper {
    private static final String TAG = "UserServerHelper";
    private static UserServerHelper instance = null;

    private static final String SERVER = Constants.USER_URL;
    private static final String AUTH = Constants.AUTH_URL;

    private static final String REGISTER        = SERVER + "/register";
    private static final String LOGIN           = SERVER + "/login";
    private static final String LOGOUT          = SERVER + "/logout";
    private static final String STAFFS          = SERVER + "/call/online";
    private static final String START_CALL      = SERVER + "/call/start";
    private static final String COMMENT         = SERVER + "/call/comment";
    private static final String UPLOAD          = SERVER + "/video/upload";
    private static final String VIDEOS          = SERVER + "/video/list";
    private static final String COUNT_VIDEOS    = SERVER + "/video/unread";
    private static final String JOB_LIST        = SERVER + "/job/list";
    private static final String JOB_DETAIL      = SERVER + "/job";
    private static final String JOB_APPLY       = SERVER + "/job/apply";
    private static final String APPLICATIONS    = SERVER + "/job/applications";
    private static final String JOB_MARK        = SERVER + "/job/mark";
    private static final String JOB_UNMARK      = SERVER + "/job/unmark";
    private static final String PROFILE_INFO    = SERVER + "/profile";
    private static final String PROFILE_UPDATE  = SERVER + "/profile/update";
    private static final String PROFILE_AVATAR  = SERVER + "/profile/avatar";
    private static final String COS_SIG = AUTH + "/getSign?bucket=cover&service=image";

    public static UserServerHelper getInstance() {
        if (instance == null) {
            instance = new UserServerHelper();
        }
        return instance;
    }
//==============================================================post
    /**
     * 注册ID （独立方式）
     */
    public RequestBackInfo registerId(String id, String password) {
        try {
            JSONObject jasonPacket = new JSONObject();
            jasonPacket.put("phone", id);
            jasonPacket.put("password", password);
            String json = jasonPacket.toString();
            String res = HttpUtil.post(REGISTER, json);
            JSONTokener jsonParser = new JSONTokener(res);
            JSONObject response = (JSONObject) jsonParser.nextValue();
            int code = response.getInt("code");
            String errorInfo = response.getString("message");

            return new RequestBackInfo(code, errorInfo);
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return null;
    }
//===============================================================
    /**
     * 登录ID （独立方式）
     */
    public RequestBackInfo loginId(String id, String password) {
        try {
            JSONObject jasonPacket = new JSONObject();
            jasonPacket.put("phone", id);
            jasonPacket.put("password", password);
            String json = jasonPacket.toString();
            String res = HttpUtil.post(LOGIN, json);

            JSONTokener jsonParser = new JSONTokener(res);
            JSONObject response = (JSONObject) jsonParser.nextValue();
            Log.i(TAG, "Response: " + res);
            int code = response.getInt("code");
            String errorInfo = response.getString("message");
            if (code >= 0) {
                String data = response.getString("data");
                MySelfInfo.getInstance().setId(id);
                MySelfInfo.getInstance().setUserSig(data);
            }
            return new RequestBackInfo(code, errorInfo);
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return null;
    }

    /**
     * 登出ID （独立方式）
     */
    public RequestBackInfo logoutId(String id) {
        try {
            JSONObject jasonPacket = new JSONObject();
            jasonPacket.put("phone", id);
            String json = jasonPacket.toString();
            String res = HttpUtil.post(LOGOUT, json);
            JSONTokener jsonParser = new JSONTokener(res);
            JSONObject response = (JSONObject) jsonParser.nextValue();

            int code = response.getInt("code");
            String errorInfo = response.getString("message");
            return new RequestBackInfo(code, errorInfo);

        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return null;
    }
//========================================================get
    /**
     * 获取客服列表
     */
    public static List<Staff> getOnlineStaffs() {
        List<Staff> staffs = new ArrayList<>();
        try {
            Gson gson = new Gson();
            String url = STAFFS;
            String res = HttpUtil.get(url);
            if(res == null || TextUtils.isEmpty(res))
                return staffs;
            JSONObject response = new JSONObject(res);
            String data = response.getString("data");
            Log.i(TAG, "data: "+ data);
            JsonArray array = new JsonParser().parse(data).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                staffs.add(gson.fromJson(jsonElement, Staff.class));
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return staffs;
    }
//========================================================
    /**
     * 建立连接
     */
    public static Boolean startCall(String callNum){
        try {
            String url = Uri.parse(START_CALL)
                    .buildUpon()
                    .appendQueryParameter("num", String.valueOf(callNum))
                    .build().toString();
            String res = HttpUtil.get(url);
            Log.i(TAG, "Response: " + res);
            JSONObject response = new JSONObject(res);

            int code = response.getInt("code");
            String message = response.getString("message");

            if(code >= 0) {
                return true;
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return false;
    }

    /**
     * 提交评价
     */
    public static Boolean submitComment(Comment comment){
        try {
            Gson gson = new Gson();
            String json = gson.toJson(comment);
            Log.i(TAG, "Request: " + json);
            String res = HttpUtil.post(COMMENT, json);
            Log.i(TAG, "Response: " + res);
            JSONObject response = new JSONObject(res);

            int code = response.getInt("code");
            String message = response.getString("message");

            if(code >= 0) {
                return true;
            }

        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return false;
    }

    /**
     * 上传视频
     */
    public RequestBackInfo uploadVideo(CurVideoInfo video){
        try {
            Gson gson = new Gson();
            String json = gson.toJson(video);
            Log.i(TAG, "Request: " + json);
            String res = HttpUtil.post(UPLOAD, json);
            Log.i(TAG, "Response: " + res);
            JSONObject response = new JSONObject(res);

            int code = response.getInt("code");
            String message = response.getString("message");

            if (code >= 0) {
                return new RequestBackInfo(code, message);
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return null;
    }

    /**
     * 获取任务
     */
    public static List<VideoMsg> getVideos(String userId) {
        List<VideoMsg> videos = new ArrayList<>();
        try {
            Gson gson = new Gson();
            String url = VIDEOS + "/" + userId;
            String res = HttpUtil.get(url);
            JSONObject response = new JSONObject(res);
            String data = response.getString("data");
            Log.i(TAG, "data: "+ data);
            JsonArray array = new JsonParser().parse(data).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                videos.add(gson.fromJson(jsonElement, VideoMsg.class));
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return videos;
    }

    /**
     * 上传视频
     */
    public static String countUnreadVideo(String userId){
        try {
            String url = COUNT_VIDEOS + "/" + userId;
            String res = HttpUtil.get(url);
            Log.i(TAG, "Response: " + res);
            JSONObject response = new JSONObject(res);

            int code = response.getInt("code");
            String message = response.getString("message");

            if(code >= 0) {
                return response.getString("data");
            }

        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return "0";
    }

    /**
     * 获取职位
     */
    public static List<Job> getJobs(String userId, Integer type) {
        List<Job> jobs = new ArrayList<>();
        try {
            Gson gson = new Gson();
            String url = Uri.parse(JOB_LIST)
                    .buildUpon()
                    .appendQueryParameter("id", userId)
                    .appendQueryParameter("type", String.valueOf(type))
                    .build().toString();
            String res = HttpUtil.get(url);
            if(res == null || TextUtils.isEmpty(res))
                return jobs;
            JSONObject response = new JSONObject(res);
            String data = response.getString("data");
            Log.i(TAG, "data: "+ data);
            JsonArray array = new JsonParser().parse(data).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                jobs.add(gson.fromJson(jsonElement, Job.class));
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return jobs;
    }
//=====================================================
    /**
     * 获取职位详情
     */
    public static Job getJob(String userId, Integer jobId) {
        Job job = new Job();
        try {
            Gson gson = new Gson();
            String url = Uri.parse(JOB_DETAIL)
                    .buildUpon()
                    .appendQueryParameter("id", userId)
                    .appendQueryParameter("job", String.valueOf(jobId))
                    .build().toString();
            String res = HttpUtil.get(url);
            JSONObject response = new JSONObject(res);
            int code = response.getInt("code");
            String message = response.getString("message");
            String data = response.getString("data");
            if(code >= 0) {
                job = gson.fromJson(data, Job.class);
            } else {
                job.setJobTitle("");
            }
            Log.i(TAG, "data: "+ data);
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return job;
    }

    /**
     * 申请工作
     */
    public static Boolean applyJob(String userId, Integer jobId){
        try {
            String url = Uri.parse(JOB_APPLY)
                    .buildUpon()
                    .appendQueryParameter("id", userId)
                    .appendQueryParameter("job", String.valueOf(jobId))
                    .build().toString();
            String res = HttpUtil.get(url);
            Log.i(TAG, "Response: " + res);
            JSONObject response = new JSONObject(res);

            int code = response.getInt("code");
            String message = response.getString("message");

            if(code >= 0) {
                return true;
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return false;
    }

    /**
     * 获取职位申请列表
     */
    public static List<ApplicationInfo> getApplications(String userId) {
        List<ApplicationInfo> jobs = new ArrayList<>();
        try {
            Gson gson = new Gson();
            String url = APPLICATIONS + "/" + userId;
            String res = HttpUtil.get(url);
            if(res == null || TextUtils.isEmpty(res))
                return jobs;
            JSONObject response = new JSONObject(res);
            String data = response.getString("data");
            Log.i(TAG, "data: "+ data);
            JsonArray array = new JsonParser().parse(data).getAsJsonArray();
            for (JsonElement jsonElement : array) {
                jobs.add(gson.fromJson(jsonElement, ApplicationInfo.class));
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return jobs;
    }

    /**
     * 收藏工作
     */
    public static Boolean markJob(String userId, Integer jobId){
        try {
            String url = Uri.parse(JOB_MARK)
                    .buildUpon()
                    .appendQueryParameter("id", userId)
                    .appendQueryParameter("job", String.valueOf(jobId))
                    .build().toString();
            String res = HttpUtil.get(url);
            Log.i(TAG, "Response: " + res);
            JSONObject response = new JSONObject(res);

            int code = response.getInt("code");
            String message = response.getString("message");

            if(code >= 0) {
                return true;
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return false;
    }

    /**
     * 取消收藏
     */
    public static Boolean unmarkJob(String userId, Integer jobId){
        try {
            String url = Uri.parse(JOB_UNMARK)
                    .buildUpon()
                    .appendQueryParameter("id", userId)
                    .appendQueryParameter("job", String.valueOf(jobId))
                    .build().toString();
            String res = HttpUtil.get(url);
            Log.i(TAG, "Response: " + res);
            JSONObject response = new JSONObject(res);

            int code = response.getInt("code");
            String message = response.getString("message");

            if(code >= 0) {
                return true;
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return false;
    }

    /**
     * 添加用户信息
     */
    public static Boolean addUserInfo(UserProfile userInfo){
        try {
            Gson gson = new Gson();
            String url = PROFILE_INFO;
            String json = gson.toJson(userInfo);
            Log.i(TAG, "Request: " + json);
            String res = HttpUtil.post(url, json);
            Log.i(TAG, "Response: " + res);
            JSONObject response = new JSONObject(res);

            int code = response.getInt("code");
            String message = response.getString("message");

            if(code >= 0) {
                return true;
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return false;
    }

    //=========================================================================
    /*
    * 获取支付单号
    */
    public RequestBackInfo getPayid(String UserId, String peerId,String body,int totalFee) {
        try {
            JSONObject jasonPacket = new JSONObject();
            jasonPacket.put("ownId", UserId);
            jasonPacket.put("peerId", "");
            jasonPacket.put("totalFee", totalFee);
            jasonPacket.put("body",body);
            jasonPacket.put("createIp", PayUtil.getIpAddress());
            String json = jasonPacket.toString();
            Log.d("申请订单号",json);

            String res = HttpUtil.post(Constants.GET_PAY_ODD, json);
            Log.d("服务器返回JSON",res);
            JSONTokener jsonParser = new JSONTokener(res);
            JSONObject response = (JSONObject) jsonParser.nextValue();

            int code = response.getInt("code");
            String errorInfo = response.getString("message");
            String payid=response.getString("data");
            return new RequestBackInfo(code, errorInfo,payid);
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return null;
    }


    /**
     * 获取用户简历   Eg:
     */
    /*data: {"id":3,"avatarUrl":"http://avatar-1254226083.cossh.myqcloud.com/86-18742508580_1507297643218.jpg",
            "nickname":"吴任高","gender":1,"nationality":null,
            "nativePlace":null,"phone":"86-18742508580",
            "emergencyContact":"86-18742508580","idNumber":
            "421127199805294719","disabilityNumber":
            "42112719980529471922","readingLevel":1,
            "writingLevel":0,"insurance":0,
            "city":"大连","address":"大连","education":"大学",
            "academy":null,"speciality":null,"interests":null,
            "experience":null,"position":"无","salary":"1000","introduction":""}
     */
    public static UserProfile getProfile(String id) {
        UserProfile profile = new UserProfile();
        try {
            Gson gson = new Gson();
            String url = PROFILE_INFO + "/" + id;
            String res = HttpUtil.get(url);
            if(res == null || TextUtils.isEmpty(res))
                return profile;
            JSONObject response = new JSONObject(res);
            String data = response.getString("data");
            profile = gson.fromJson(data, UserProfile.class);
            Log.i(TAG, "UserServerdata: "+ data);
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return profile;
    }

    /**
     * 更新信息
     */
    public static Boolean updateProfile(String userId, String nickname, String sign){
        try {
            String url = Uri.parse(PROFILE_UPDATE)
                    .buildUpon()
                    .appendQueryParameter("id", userId)
                    .appendQueryParameter("nickname", nickname)
                    .appendQueryParameter("sign", sign)
                    .build().toString();
            String res = HttpUtil.get(url);
            Log.i(TAG, "Response: " + res);
            JSONObject response = new JSONObject(res);

            int code = response.getInt("code");
            String message = response.getString("message");

            if(code >= 0) {
                return true;
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return false;
    }

    /**
     * 更新用户头像
     */
    public static Boolean updateAvatar(String userId, String avatarUrl){
        try {
            String url = Uri.parse(PROFILE_AVATAR)
                    .buildUpon()
                    .appendQueryParameter("id", userId)
                    .appendQueryParameter("avatar", avatarUrl)
                    .build().toString();
            String res = HttpUtil.get(url);
            Log.i(TAG, "Response: " + res);
            JSONObject response = new JSONObject(res);

            int code = response.getInt("code");
            String message = response.getString("message");

            if(code >= 0) {
                return true;
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return false;
    }

    String getCosSig() {
        String sign = "";
        try {
            String res = HttpUtil.get(COS_SIG);
            JSONTokener jsonParser = new JSONTokener(res);
            JSONObject response = (JSONObject) jsonParser.nextValue();
            int ret = response.getInt("code");
            if (ret >= 0) {
                sign = response.getString("data");
            }
            Log.w(TAG,"sign = " +sign);

        } catch (JSONException | IOException e) {
            Log.e(TAG, "Exception:", e);
        }
        return sign;
    }
}
