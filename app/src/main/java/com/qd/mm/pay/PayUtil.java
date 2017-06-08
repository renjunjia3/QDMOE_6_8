package com.qd.mm.pay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.qd.mm.AliPayActivity;
import com.qd.mm.app.App;
import com.qd.mm.bean.PayTokenResultInfo;
import com.qd.mm.util.API;
import com.qd.mm.util.DialogUtil;
import com.qd.mm.util.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Case By: 支付工具类
 * package:
 * Author：scene on 2017/4/18 9:30
 */
public class PayUtil {
    private ProgressDialog dialog;

    //VIP的类型对应下面的描述
    //1：直接开通15元，2：直接开通30元，3：升级30元，4：优惠开通15元，5：优惠开通30元
    public static final int VIP_TYPE_OPEN_15 = 1;
    public static final int VIP_TYPE_OPEN_30 = 2;
    public static final int VIP_TYPE_UPDATE_30 = 3;
    public static final int VIP_TYPE_DISCOUNT_OPEN_15 = 4;
    public static final int VIP_TYPE_DISCOUNT_OPEN_30 = 5;


    //直接开通15元 1500
    private static final int VIP_MONEY_OPEN_15 = 1;
    //直接开通30元 3000
    private static final int VIP_MONEY_OPEN_30 = 2;
    //升级30元 3000
    private static final int VIP_MONEY_UPDATE_30 = 3;
    //优惠开通15元 1200
    private static final int VIP_MONEY_DISCOUNT_OPEN_15 = 4;
    //优惠开通30元 2400
    private static final int VIP_MONEY_DISCOUNT_OPEN_30 = 5;

//    //直接开通15元 1500
//    private static final int VIP_MONEY_OPEN_15 = 1500;
//    //直接开通30元 3000
//    private static final int VIP_MONEY_OPEN_30 = 3000;
//    //升级30元 1500
//    private static final int VIP_MONEY_UPDATE_30 = 1500;
//    //优惠开通15元 1200
//    private static final int VIP_MONEY_DISCOUNT_OPEN_15 = 1200;
//    //优惠开通30元 2400
//    private static final int VIP_MONEY_DISCOUNT_OPEN_30 = 2400;

    private static PayUtil instance = null;

    public static PayUtil getInstance() {
        if (instance == null) {
            synchronized (PayUtil.class) {
                if (instance == null) {
                    instance = new PayUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 微信去支付
     *
     * @param context 上下文
     * @param viptype 开通的服务类型
     * @param videoId 视频id
     */
    public void payByWeChat(Context context, int viptype, int videoId, int pay_position_id) {
        getOrderNo(context, viptype, true, videoId, pay_position_id);
    }

    /**
     * 支付宝去支付
     *
     * @param context 上下文
     * @param viptype 要开通的服务类型
     * @param videoId 视频id
     */
    public void payByAliPay(Context context, int viptype, int videoId, int pay_position_id) {
        getOrderNo(context, viptype, false, videoId, pay_position_id);
    }


    /**
     * 从服务器获取订单号
     *
     * @param context  上下文
     * @param vipType  要开通的服务类型
     * @param isWechat 支付类型true：微信，false：支付宝
     * @param video_id 视频id
     */
    private void getOrderNo(final Context context, final int vipType, final boolean isWechat, final int video_id, int pay_position_id) {
        App.isGoodsPay = false;
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
        dialog = ProgressDialog.show(context, "", "订单提交中...");
        Map<String, String> params = API.createParams();
        switch (vipType) {
            case VIP_TYPE_OPEN_15:
                params.put("money", String.valueOf(VIP_MONEY_OPEN_15));
                params.put("title", "开通15元区");
                break;
            case VIP_TYPE_OPEN_30:
                params.put("money", String.valueOf(VIP_MONEY_OPEN_30));
                params.put("title", "开通30元区");
                break;
            case VIP_TYPE_UPDATE_30:
                params.put("money", String.valueOf(VIP_MONEY_UPDATE_30));
                params.put("title", "升级30元区");
                break;
            case VIP_TYPE_DISCOUNT_OPEN_15:
                params.put("money", String.valueOf(VIP_MONEY_DISCOUNT_OPEN_15));
                params.put("title", "开通15元区（优惠）");
                break;
            case VIP_TYPE_DISCOUNT_OPEN_30:
                params.put("money", String.valueOf(VIP_MONEY_DISCOUNT_OPEN_30));
                params.put("title", "开通30元区（优惠）");
                break;
        }

        params.put("video_id", String.valueOf(video_id));
        params.put("position_id", String.valueOf(pay_position_id));
        params.put("pay_type", isWechat ? "1" : "2");
        params.put("type", String.valueOf(vipType));

        OkHttpUtils.post().url(API.URL_PRE + API.GET_ORDER_INFO_TYPE_2).params(params).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                ToastUtils.getInstance(context).showToast("订单信息获取失败，请重试");
            }

            @Override
            public void onResponse(String s, int i) {
                try {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    final PayTokenResultInfo info = JSON.parseObject(s, PayTokenResultInfo.class);
                    if (info.getPay_type() == 1) {
                        //微信扫码
                        DialogUtil.getInstance().showWxQRCodePayDialog(context, info.getCode_img_url());
                        App.isNeedCheckOrder = true;
                        App.orderIdInt = info.getOrder_id_int();
                        DialogUtil.getInstance().showCustomSubmitDialog(context, "支付二维码已经保存到您的相册，请前往微信扫一扫付费");
                    } else if (info.getPay_type() == 2) {
                        //支付宝wap
                        Intent intent = new Intent(context, AliPayActivity.class);
                        intent.putExtra(AliPayActivity.ALIPAY_URL, info.getCode_url());
                        context.startActivity(intent);
                        App.isNeedCheckOrder = true;
                        App.orderIdInt = info.getOrder_id_int();
                    } else {
                        ToastUtils.getInstance(context).showToast("订单信息获取失败，请重试");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.getInstance(context).showToast("订单信息获取失败，请重试");
                }
            }
        });
    }

}
