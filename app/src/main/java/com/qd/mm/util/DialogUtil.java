package com.qd.mm.util;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.qd.mm.MainActivity;
import com.qd.mm.config.PageConfig;
import com.qd.mm.pay.PayUtil;
import com.qd.mm.ui.dialog.BackGlodVipDialog;
import com.qd.mm.ui.dialog.CustomSubmitDialog;
import com.qd.mm.ui.dialog.GlodVipDialog;
import com.qd.mm.ui.dialog.OpenVipNoticeDialog;
import com.qd.mm.ui.dialog.SubmitAndCancelDialog;
import com.qd.mm.ui.dialog.WxQRCodePayDialog;
import com.qd.mm.ui.fragment.MainFragment;

/**
 * Case By:对dialog的封装
 * package:com.hfaufhreu.hjfeuio.util
 * Author：scene on 2017/4/20 18:39
 */

public class DialogUtil {

    private static DialogUtil instance = null;

    //只有确定的对话框
    private CustomSubmitDialog customSubmitDialog;
    //成功开通会员的弹窗
    private OpenVipNoticeDialog openVipNoticeDialog;

    //确定取消的对话框
    private SubmitAndCancelDialog submitAndCancelDialog;
    //开通黄金或者砖石的对话框
    private GlodVipDialog glodVipDialog;
    //优惠开通黄金会员
    private BackGlodVipDialog backGlodVipDialog;
    //微信二维码
    private WxQRCodePayDialog wxQRCodePayDialog;

    public static DialogUtil getInstance() {
        if (instance == null) {
            synchronized (PayUtil.class) {
                if (instance == null) {
                    instance = new DialogUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Case By:
     * Author: scene on 2017/4/20 19:28
     *
     * @param context          上下文
     * @param isCancelAsSubmit 是否把取消按钮当做确定按钮
     * @param message          显示的文字内容
     * @param isShowOpenVip    是否进入显示开通会员的对话框
     */
    public void showSubmitDialog(Context context, boolean isCancelAsSubmit, String message, final boolean isShowOpenVip, int pay_position_id) {
        showSubmitDialog(context, isCancelAsSubmit, message, isShowOpenVip, 0, pay_position_id);
    }

    /**
     * Case By:
     * Author: scene on 2017/4/20 19:28
     *
     * @param context          上下文
     * @param isCancelAsSubmit 是否把取消按钮当做确定按钮
     * @param message          显示的文字内容
     * @param isShowOpenVip    是否进入显示开通会员的对话框
     * @param videoId          视频Id
     */
    public void showSubmitDialog(final Context context, final boolean isCancelAsSubmit, String message, final boolean isShowOpenVip, final int videoId, final int pay_position_id) {

        if (submitAndCancelDialog != null && submitAndCancelDialog.isShowing()) {
            submitAndCancelDialog.cancel();
        }
        SubmitAndCancelDialog.Builder submitAndCancelDialogBuilder = new SubmitAndCancelDialog.Builder(context);
        submitAndCancelDialogBuilder.setMessage(message);

        submitAndCancelDialogBuilder.setSubmitButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitAndCancelDialog.dismiss();
                if (isShowOpenVip) {
                    showVipDialog(context, videoId, pay_position_id);
                }

            }
        });

        submitAndCancelDialogBuilder.setCancelButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitAndCancelDialog.dismiss();
                if (isShowOpenVip && isCancelAsSubmit) {
                    showVipDialog(context, videoId, pay_position_id);
                }
            }
        });
        submitAndCancelDialog = submitAndCancelDialogBuilder.create();
        submitAndCancelDialog.show();
    }

    /**
     * Case By:显示某一个开通VIp的Dialog
     * Author: scene on 2017/4/20 19:26
     *
     * @param context         上下文
     * @param videoId         视频Id
     * @param pay_position_id 吊起支付界面的position_id
     */
    private void showVipDialog(Context context, int videoId, int pay_position_id) {
        showGoldVipDialog(context, videoId, pay_position_id);

    }

    /**
     * Case By:显示开通黄金或者砖石会员的dialog
     * Author: scene on 2017/4/20 19:27
     *
     * @param context 上下文
     * @param videoId 视频id
     */
    public void showGoldVipDialog(Context context, int videoId, int pay_position_id) {
        if (glodVipDialog != null && glodVipDialog.isShowing()) {
            glodVipDialog.cancel();
        }
        GlodVipDialog.Builder glodVipDialogBuilder = new GlodVipDialog.Builder(context, videoId, pay_position_id);
        glodVipDialog = glodVipDialogBuilder.create();
        glodVipDialog.show();
        MainFragment.clickWantPay();
        MainActivity.upLoadPageInfo(PageConfig.OPEN_GLOD_VIP_DIALOG_POSITOTN_ID, videoId, pay_position_id);
    }


    /**
     * Case By:显示只有确定按钮的dialog
     * Author: scene on 2017/4/25 16:43
     *
     * @param context  上下文
     * @param message1 文字内容
     */
    public OpenVipNoticeDialog showOpenVipNoticeDialog(Context context, String message1) {
        if (openVipNoticeDialog != null && openVipNoticeDialog.isShowing()) {
            openVipNoticeDialog.cancel();
        }
        OpenVipNoticeDialog.Builder openVipNoticeDialogBuidler = new OpenVipNoticeDialog.Builder(context);
        openVipNoticeDialogBuidler.setMessage1(message1);
        openVipNoticeDialogBuidler.setButtonText(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (openVipNoticeDialog != null && openVipNoticeDialog.isShowing()) {
                    openVipNoticeDialog.cancel();
                }
            }
        });
        openVipNoticeDialog = openVipNoticeDialogBuidler.create();
        openVipNoticeDialog.show();
        return openVipNoticeDialog;
    }


    /**
     * Case By:开通会员之后的提示框
     * Author: scene on 2017/4/25 16:43
     *
     * @param context 上下文
     * @param message 文字内容
     */
    public CustomSubmitDialog showCustomSubmitDialog(Context context, String message) {
        if (customSubmitDialog != null && customSubmitDialog.isShowing()) {
            customSubmitDialog.cancel();
        }
        CustomSubmitDialog.Builder customSubmitDialgBuiler = new CustomSubmitDialog.Builder(context);
        customSubmitDialgBuiler.setMessage(message);
        customSubmitDialgBuiler.setButtonText("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (customSubmitDialog != null && customSubmitDialog.isShowing()) {
                    customSubmitDialog.cancel();
                }
            }
        });
        customSubmitDialog = customSubmitDialgBuiler.create();
        customSubmitDialog.show();
        return customSubmitDialog;
    }

    /**
     * 优惠开通会员
     *
     * @param context 上下文
     * @return 对话框
     */
    public BackGlodVipDialog showBackGlodVipDialog(Context context) {
        if (backGlodVipDialog != null && backGlodVipDialog.isShowing()) {
            backGlodVipDialog.cancel();
        }
        BackGlodVipDialog.Builder builder = new BackGlodVipDialog.Builder(context);
        backGlodVipDialog = builder.create();
        backGlodVipDialog.show();
        MainFragment.clickWantPay();
        MainActivity.upLoadPageInfo(PageConfig.BACK_OPEN_VIP_POSITOTN_ID, 0, 0);
        return backGlodVipDialog;
    }

    public WxQRCodePayDialog showWxQRCodePayDialog(Context context, String qrCodeUrl) {
        WxQRCodePayDialog.Builder builder = new WxQRCodePayDialog.Builder(context, qrCodeUrl);
        wxQRCodePayDialog = builder.create();
        wxQRCodePayDialog.show();
        return wxQRCodePayDialog;
    }

    /**
     * Case By:关闭所有的dialog
     * Author: scene on 2017/4/24 9:34
     */
    public void cancelAllDialog() {
        try {
            if (submitAndCancelDialog != null && submitAndCancelDialog.isShowing()) {
                submitAndCancelDialog.cancel();
            }
            if (glodVipDialog != null && glodVipDialog.isShowing()) {
                glodVipDialog.cancel();
            }
            if (openVipNoticeDialog != null && openVipNoticeDialog.isShowing()) {
                openVipNoticeDialog.cancel();
            }
            if (backGlodVipDialog != null && backGlodVipDialog.isShowing()) {
                backGlodVipDialog.cancel();
            }
            if (wxQRCodePayDialog != null && wxQRCodePayDialog.isShowing()) {
                wxQRCodePayDialog.cancel();
            }

        } catch (Exception e) {
            Log.e("DialogUtil", "对话框关闭异常");
        }
    }


}
