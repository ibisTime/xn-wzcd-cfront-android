package com.cdkj.huatuweitong.adapters;

import android.support.annotation.Nullable;
import android.widget.ProgressBar;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.huatuweitong.MainActivity;
import com.cdkj.huatuweitong.R;
import com.cdkj.huatuweitong.bean.CarLoanDetailsActivityBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 齐胜涛
 * @des ${TODO}
 * @updateDts 2018/5/17
 * Created by lenovo on 2018/5/17.
 */

public class RepaymentPlanActivityAdapter extends BaseQuickAdapter<CarLoanDetailsActivityBean.RepayPlanListBean, BaseViewHolder> {
    List<CarLoanDetailsActivityBean.RepayPlanListBean> data;

    public RepaymentPlanActivityAdapter(@Nullable List<CarLoanDetailsActivityBean.RepayPlanListBean> data) {
        super(R.layout.item_repayment_plan_activity, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, CarLoanDetailsActivityBean.RepayPlanListBean item) {
        ProgressBar pb = helper.getView(R.id.pb);
        helper.setText(R.id.tv_number, (int) item.getCurPeriods() + "/" + (int) item.getPeriods() + "期");
        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getRepayDatetime(), DateUtil.DATE_YMD));
        helper.setText(R.id.tv_money, MoneyUtils.showPrice(item.getRepayCapital()));

        //计算已还比例
        int bili = item.getPayedAmount().divide(item.getRepayCapital()).multiply(new BigDecimal(100)).intValue();
        int progress = bili;//已还比例
        pb.setProgress(progress);

//        MyTextUtils.setStatusType004(helper.getView(R.id.tv_type), item.getCurNodeCode());

        if (item.getPayedAmount().compareTo(new BigDecimal(0)) == 0) {//比较大小
            helper.setText(R.id.tv_type, MainActivity.getNodeCode(item.getCurNodeCode()));
        } else {
            helper.setText(R.id.tv_type, "已还" + MoneyUtils.showPrice(
                    item.getPayedAmount()) + "   " + "剩余" + MoneyUtils.showPrice(item.getOverplusAmount()));
        }

//        if (TextUtils.equals("004_01", item.getCurNodeCode())) {
//
//            if (item.getPayedAmount().compareTo(new BigDecimal(0)) == 0) {//比较大小
//                helper.setText(R.id.tv_type, "未还");
//            } else {
//                helper.setText(R.id.tv_type, "已还" + MoneyUtils.showPrice(
//                        item.getPayedAmount()) + "   " + "剩余" + MoneyUtils.showPrice(item.getOverplusAmount()));
//            }
//
//        } else if (TextUtils.equals("004_02", item.getCurNodeCode())) {
//            //完成
//            pb.setProgress(100);
//        } else if (item.getCurNodeCode().equals("004_06")) {
//            //逾期
//            helper.setText(R.id.tv_type, "逾期");
//            helper.setTextColor(R.id.tv_type, Color.RED);
//        }
    }
}
