package com.mzhguqvn.mzhguq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mzhguqvn.mzhguq.R;
import com.mzhguqvn.mzhguq.bean.VoucherInfo;
import com.mzhguqvn.mzhguq.util.DecimalUtils;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Case By:我的代金券
 * package:com.mzhguqvn.mzhguq.adapter
 * Author：scene on 2017/5/10 17:34
 */

public class VoucherAdapter extends BaseAdapter {
    private Context context;
    private List<VoucherInfo> list;
    private LayoutInflater inflater;

    public VoucherAdapter(Context context, List<VoucherInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_voucher_item, parent, false);
            viewHolder = new OrderViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrderViewHolder) convertView.getTag();
        }
        VoucherInfo info = list.get(position);
        if (info != null) {
            viewHolder.price.setText(String.valueOf(DecimalUtils.toRound2(info.getMoney() / 100d)));
            viewHolder.name.setText(String.valueOf(DecimalUtils.toRound2(info.getMoney() / 100d)) + "元商城代金券");
        }

        return convertView;
    }

    static class OrderViewHolder {

        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.name)
        TextView name;

        OrderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
