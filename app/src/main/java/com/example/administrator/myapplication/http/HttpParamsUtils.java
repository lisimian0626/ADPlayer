package com.example.administrator.myapplication.http;

import android.text.TextUtils;

import com.beidousat.mobile.minik.common.TConst;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * author: Hanson
 * date:   2016/9/21
 * describe:
 */
public class HttpParamsUtils {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

    public static Map<String, String> initCommonParams(int page) {
        Map<String, String> params = new HashMap<>();
        params.put("p", String.valueOf(page));
        params.put("page_num", String.valueOf(TConst.PAGE_NUM));

        return params;
    }

    public static Map<String, String> initRecordParams(Date start, Date end, int page) {
        Map<String, String> params = new HashMap<>();

        params.put("p", String.valueOf(page));
        params.put("page_num", String.valueOf(TConst.PAGE_NUM));
        if (start != null && end != null) {
            params.put("start_date", sdf1.format(start));
            params.put("end_date", sdf1.format(end));
        }

        return params;
    }

    public static Map<String, String> initAdIncomesParams(int deviceId, Date start, Date end) {
        Map<String, String> params = new HashMap<>();

        params.put("device_id", String.valueOf(deviceId));
        if (start != null && end != null) {
            params.put("start_date", sdf1.format(start));
            params.put("end_date", sdf1.format(end));
        }

        return params;
    }

    public static Map<String, String> initAdIncomesParams(int deviceId, int type) {
        Map<String, String> params = new HashMap<>();

//        params.put("device_id", String.valueOf(deviceId));
        params.put("time", String.valueOf(type));

        return params;
    }

    public static Map<String, String> init24hRoomsParams(Date start, Date end) {
        Map<String, String> params = new HashMap<>();

        if (start != null && end != null) {
            params.put("time_start", sdf1.format(start));
            params.put("time_end", sdf1.format(end));
        }

        return params;
    }

    public static Map<String, String> init24hRoomsParams(int type) {
        Map<String, String> params = new HashMap<>();

        params.put("time", String.valueOf(type));

        return params;
    }

    public static Map<String, String> initDevicesParams(String type, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("perpage", String.valueOf(TConst.PAGE_NUM));
        if (!TextUtils.isEmpty(type)) {
            params.put("type", type);
        }

        return params;
    }

    public static Map<String,String> initRechargeParams(
            int type,
            float money,
            String bank,
            String account,
            String receipt_number,
            String sys_order_id,
            String desc,
            String userid,
            String token) {

        Map<String, String> params = new HashMap<>();
        params.put("type", String.valueOf(type));
        params.put("money", String.valueOf(money));
        params.put("receipt_number", TextUtils.isEmpty(receipt_number) ? "" : receipt_number);
        params.put("sys_order_id", TextUtils.isEmpty(sys_order_id) ? "" : sys_order_id);
        params.put("desc", TextUtils.isEmpty(desc) ? "" : desc);
        params.put("userid", TextUtils.isEmpty(userid) ? "" : userid);
        params.put("token", TextUtils.isEmpty(token) ? "" : token);

        return params;
    }
}
