package com.example.administrator.myapplication.common;

import android.os.Environment;

import java.io.File;

/**
 * author: Hanson
 * date:   2016/8/31
 * describe:
 */
public class TConst {
    private static final String APP_ROOT_DIR = "/ADPlayer/";
    private static final String APP_CACHE_DIR = APP_ROOT_DIR+"Cache/";


////    测试环境
//    public static final String BASE_URL = "http://192.168.1.233:8009/";
//    public static final String VERSION_UPDATE_URL = "http://60.195.40.230:2800/";


//    //生产环境
    public static final String BASE_URL = "http://minorder.beidousat.com";
    public static final String VERSION_UPDATE_URL = "http://m.beidousat.com/";

    public static final int PAGE_NUM = 20; //一页消息的数量


    public static final int APP_TYPE = 4; //miniktv系统 = 2
    /**
     * http 状态码
     */
    public static final class HttpStatus {
        public static final int QUERY_SUCCESS = 200; //请求成功
        public static final int MODIFY_SUCCESS = 201; //新建、修改成功
        public static final int DELETE_SUCCESS = 204; //删除成功
        public static final int USE_CACHE = 304; //通知客户端使用缓存
        public static final int REQUEST_ERROR = 400; //请求错误
        public static final int NOT_AUTHENTICATION = 203; //登录已过期
        public static final int NOT_AUTHORIZATION = 403; //用户已认证但无权限
        public static final int REUQET_NOT_EXIST = 404; //请求不存在
    }


    public static String getApkDir() {
        String filepath = Environment.getExternalStorageDirectory().getPath()+APP_CACHE_DIR;
        File file = new File(filepath);

        if (!file.exists()) {
            file.mkdirs();
        }

        return filepath;
    }
}
