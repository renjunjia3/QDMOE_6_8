package com.qd.mm.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.qd.mm.R;
import com.qd.mm.util.ScreenUtils;
import com.qd.mm.util.ViewUtils;

/**
 * Case By: 开通会员之后的弹窗
 * package:
 * Author：scene on 2017/5/25 19:08
 */
public class OpenVipNoticeDialog extends Dialog {

    public OpenVipNoticeDialog(Context context) {
        super(context);
    }

    public OpenVipNoticeDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String message1;
        private String message2;
        private OnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage1(String message1) {
            this.message1 = message1;
            return this;
        }

        public Builder setMessage2(String message2) {
            this.message2 = message2;
            return this;
        }

        public Builder setButtonText(OnClickListener listener) {
            this.positiveButtonClickListener = listener;
            return this;
        }

        public OpenVipNoticeDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            final OpenVipNoticeDialog dialog = new OpenVipNoticeDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_open_vip_notice, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ViewUtils.setViewHeightByViewGroup(layout, (int) (ScreenUtils.instance(context).getScreenWidth() * 0.9f));

            if (positiveButtonClickListener != null) {
                layout.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        positiveButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }

            TextView content = (TextView) dialog.findViewById(R.id.content);
            SpannableStringBuilder builder = new SpannableStringBuilder("成功开通" + message1);

            ForegroundColorSpan span1 = new ForegroundColorSpan(Color.parseColor("#FF6724"));
            StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);

            builder.setSpan(span1, 4, message1.length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(styleSpan_B, 4, message1.length() + 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            content.setText(builder);
            dialog.setContentView(layout);
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }
}  