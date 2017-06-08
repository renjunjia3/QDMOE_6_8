package com.qd.mm.event;

import com.qd.mm.bean.VoucherInfo;

/**
 * Case By:选择代金券之后返回shopFragment调用
 * package:com.mzhguqvn.mzhguq.event
 * Author：scene on 2017/5/25 16:41
 */

public class ChoosedVoucherBackEvent {
    public VoucherInfo voucherInfo;
    public int page;

    public ChoosedVoucherBackEvent(VoucherInfo voucherInfo, int page) {
        this.voucherInfo = voucherInfo;
        this.page = page;
    }
}
