package com.shouyubang.android.sybang.utils;

/**
 * 静态函数
 */
public class Constants {

    public static final String FEEDBACK_API="http://www.shouyubang.com:8080/guanli/api/user/advice";
    public static final String GET_PAY_ODD="http://www.shouyubang.com:8080/guanli/api/pay/requestWXPay";
    public static final String APPID_WACHAT_PAY="helloworld";
    public static final String KEY_SIGN ="helloworld";

    //传入api+订单号，返回支付结果
    //public static final String API_CHECK_KEY="http://www.shouyubang.com:8080/guanli/api/pay/checkOrder?trade_no=";

    public static final String MCH_ID ="helloworld";

    private static final String DOMAIN = "https://www.yunzhewentong.com:8443/syb";

    public static final String BASE = DOMAIN;
    public static final String USER_URL = BASE+"/api/user";
    public static final String AUTH_URL = BASE+"/api/auth";

    public static final int IM_APP_ID = 10000;
    public static final String BUGLY_APP_ID = "helloworld";
    public static final int ACCOUNT_TYPE = 10000;

    public static final String COS_APP_ID = "helloworld";
    public static final String VIDEO_BUCKET = "video";
    public static final String COVER_BUCKET = "cover";
    public static final String AVATAR_BUCKET = "avatar";

    public static final String MOB_APP_KEY = "helloworld";
    public static final String MOB_APP_SECRET = "helloworld";

    public static final String BD_EXIT_APP = "bd_sxb_exit";

    public static final String USER_INFO = "user_info";

    public static final String USER_ID = "user_id";

    public static final String USER_SIG = "user_sig";

    public static final String USER_TOKEN = "user_token";

    public static final String USER_NICK = "user_nick";

    public static final String USER_SIGN = "user_sign";

    public static final String USER_AVATAR = "user_avatar";

    public static final int GENDER_DEFAULT = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;
    public static final int GENDER_NO_LIMIT = 3;

    public static final int READING_GOOD = 3;
    public static final int READING_MEDIUM = 2;
    public static final int READING_POOR = 1;
    public static final int READING_DEFAULT = 0;

    public static final int WRITING_GOOD = 3;
    public static final int WRITING_MEDIUM = 2;
    public static final int WRITING_POOR = 1;
    public static final int WRITING_DEFAULT = 0;

    public static final int INSURANCE_TRUE = 1;
    public static final int INSURANCE_FALSE = 0;

    public static final int VIDEO_PRE_SERVICE = 0;
    public static final int VIDEO_IN_SERVICE = 1;
    public static final int VIDEO_POST_SERVICE = 2;
    public static final int VIDEO_END_SERVICE = 3;

    public static final int CALL_OFFLINE = 0;
    public static final int CALL_ONLINE = 1;
    public static final int CALL_AWAY = 2;
    public static final int CALL_BUSY = 3;

    public static final int JOB_UNMARK = 0;
    public static final int JOB_MARK = 1;

    public static final int WRITE_PERMISSION_REQ_CODE = 101;
}
