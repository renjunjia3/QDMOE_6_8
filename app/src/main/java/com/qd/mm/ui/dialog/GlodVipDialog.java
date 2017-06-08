package com.qd.mm.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qd.mm.MainActivity;
import com.qd.mm.R;
import com.qd.mm.app.App;
import com.qd.mm.config.PageConfig;
import com.qd.mm.pay.PayUtil;
import com.qd.mm.util.ScreenUtils;
import com.qd.mm.util.ViewUtils;


/**
 * Case By:开通黄金VIP
 * package:
 * Author：scene on 2017/4/18 13:53
 */
public class GlodVipDialog extends Dialog {
    public GlodVipDialog(@NonNull Context context) {
        super(context);
    }

    public GlodVipDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected GlodVipDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenUtils.instance(getContext()).getScreenWidth() * 0.95f); // 宽度
        dialogWindow.setAttributes(lp);
        super.show();
    }

    public static class Builder {
        private Context context;
        private int videoId;
        private int pay_position_id;

        private int payWayType = 1;
        private int vip_type = PayUtil.VIP_TYPE_OPEN_15;

        private LinearLayout layout_type_diamond, layout_type_glod;
        private TextView text1;
        private TextView text_type_1;

        public Builder(Context context, int videoId, int pay_position_id) {
            this.context = context;
            this.videoId = videoId;
            this.pay_position_id = pay_position_id;
        }

        public GlodVipDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final GlodVipDialog dialog = new GlodVipDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_glod_vip, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final ImageView glodChoosed = (ImageView) layout.findViewById(R.id.glod_choosed);
            final ImageView diamondChoosed = (ImageView) layout.findViewById(R.id.diamond_choosed);
            ImageView image = (ImageView) layout.findViewById(R.id.image);
            TextView oldPrice1 = (TextView) dialog.findViewById(R.id.old_price_1);
            TextView oldPrice2 = (TextView) dialog.findViewById(R.id.old_price_2);
            oldPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            oldPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            text1 = (TextView) layout.findViewById(R.id.text_1);
            text_type_1 = (TextView) layout.findViewById(R.id.text_type_1);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height = (int) ((ScreenUtils.instance(context).getScreenWidth() - ScreenUtils.instance(context).dip2px(50)) * 3f / 5f);
            image.setLayoutParams(layoutParams);
            TextView discount = (TextView) dialog.findViewById(R.id.discount);
            SpannableStringBuilder builder = new SpannableStringBuilder(discount.getText().toString());
            ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#D3121A"));
            builder.setSpan(redSpan, 10, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            discount.setText(builder);

            layout_type_glod = (LinearLayout) layout.findViewById(R.id.layout_type_glod);
            layout_type_diamond = (LinearLayout) layout.findViewById(R.id.layout_type_diamond);
            if (App.role == 0) {
                layout_type_glod.setVisibility(View.VISIBLE);
                layout_type_diamond.setVisibility(View.VISIBLE);
            } else if (App.role == 1) {
                layout_type_glod.setVisibility(View.VISIBLE);
                layout_type_diamond.setVisibility(View.GONE);
                text1.setText("升级");
                text_type_1.setText("30元区");
            }

            layout.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            layout.findViewById(R.id.layout_type_glod).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (App.role == 0) {
                        vip_type = PayUtil.VIP_TYPE_OPEN_15;
                    } else {
                        vip_type = PayUtil.VIP_TYPE_UPDATE_30;
                    }
                    glodChoosed.setImageResource(R.drawable.ic_vip_type_s);
                    diamondChoosed.setImageResource(R.drawable.ic_vip_type_d);
                }
            });
            layout.findViewById(R.id.layout_type_diamond).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vip_type = PayUtil.VIP_TYPE_OPEN_30;
                    glodChoosed.setImageResource(R.drawable.ic_vip_type_d);
                    diamondChoosed.setImageResource(R.drawable.ic_vip_type_s);
                }
            });
            final RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.radio_group);
            layout.findViewById(R.id.open_vip).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (radioGroup.getCheckedRadioButtonId() == R.id.type_wechat) {
                        payWayType = 1;
                    } else {
                        payWayType = 2;
                    }
                    if (payWayType == 1) {
                        PayUtil.getInstance().payByWeChat(context, vip_type, videoId, pay_position_id);
                    } else {
                        PayUtil.getInstance().payByAliPay(context, vip_type, videoId, pay_position_id);
                    }
                    if (vip_type == PayUtil.VIP_TYPE_OPEN_15) {
                        MainActivity.upLoadPageInfo(PageConfig.CLICK_OPEN_15_POSITION_ID, videoId, pay_position_id);
                    } else if (vip_type == PayUtil.VIP_TYPE_OPEN_30) {
                        MainActivity.upLoadPageInfo(PageConfig.CLICK_OPEN_30_POSITION_ID, videoId, pay_position_id);
                    } else {
                        MainActivity.upLoadPageInfo(PageConfig.CLICK_UPDATE_30_POSITION_ID, videoId, pay_position_id);
                    }

                }
            });

            ViewUtils.setViewHeightByViewGroup(layout, (int) (ScreenUtils.instance(context).getScreenWidth() * 0.9f));
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}
