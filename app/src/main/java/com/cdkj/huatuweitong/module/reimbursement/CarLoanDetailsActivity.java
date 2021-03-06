package com.cdkj.huatuweitong.module.reimbursement;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.huatuweitong.MainActivity;
import com.cdkj.huatuweitong.R;
import com.cdkj.huatuweitong.api.MyApiServer;
import com.cdkj.huatuweitong.bean.CarLoanDetailsActivityBean;
import com.cdkj.huatuweitong.bean.CarLoanDetailsActivityMonthBean;
import com.cdkj.huatuweitong.databinding.ActivityCarLoanDetailsBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class CarLoanDetailsActivity extends AbsBaseLoadActivity {

    private ActivityCarLoanDetailsBinding mBinding;
    private String code;
    private String type;//1代表是 本月应还的   2代表是所有的贷款
    private CarLoanDetailsActivityBean mdata;

    public static void open(Context context, String code, String type) {
        if (context != null) {
            Intent intent = new Intent(context, CarLoanDetailsActivity.class);
            intent.putExtra("code", code);
            intent.putExtra("type", type);
            context.startActivity(intent);
        }
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_car_loan_details, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("贷款详情");
        if (getIntent() != null) {
            code = getIntent().getStringExtra("code");
            type = getIntent().getStringExtra("type");
        }

        if (TextUtils.equals(type, "2")) {
            initDatas();
            mBinding.tvTypeMoney.setText("剩余贷款");
        } else if (TextUtils.equals(type, "1")) {
            initMonthData();
            mBinding.tvTypeMoney.setText("应还总额");
            mBinding.llTime.setVerticalGravity(View.VISIBLE);
            mBinding.llButtom.setVisibility(View.GONE);
            mBinding.llRepaymentPlan.setVisibility(View.GONE);

        }
        mBinding.llRepaymentPlan.setOnClickListener(v -> {
            //还款计划
            if (mdata != null) RepaymentPlanActivity.open(CarLoanDetailsActivity.this, mdata);
        });


        mBinding.btnEarlyRepayment.setOnClickListener(v -> {

            String bankcardNumber;
            bankcardNumber = mdata.getCode();
            AdvanceDetailsActivity.open(CarLoanDetailsActivity.this, mdata.getRestAmount() + "", bankcardNumber, mdata.getCode());

        });

    }

    private void initMonthData() {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        Call call = RetrofitUtils.createApi(MyApiServer.class).getCarLoanDetailsMonthActivity("630541", StringUtils.getJsonToString(map));
        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<CarLoanDetailsActivityMonthBean>(this) {
            @Override
            protected void onSuccess(CarLoanDetailsActivityMonthBean data, String SucMessage) {
                //这个不需要跳转  所以数据不用传递
                CarLoanDetailsActivityMonthBean.RepayBizBean repayBiz = data.getRepayBiz();
                String beForOver = repayBiz == null ? "" : MoneyUtils.showPriceDouble(data.getRepayBiz().getRestAmount());
                mBinding.tvBeForOver.setText(beForOver);//这个先设置为本期本金

                CarLoanDetailsActivityMonthBean.RepayBizBean.BudgetOrderBean budgetOrder = repayBiz.getBudgetOrder();
                mBinding.tvLoanCar.setText(budgetOrder.getCarModel());
                mBinding.tvLoanTotal.setText(repayBiz == null ? "" : MoneyUtils.showPrice(repayBiz.getLoanAmount()));//贷款总额
                mBinding.tvLoanTerm.setText(data.getPeriods() + "");//贷款期sh
                mBinding.tvRepaymentPlan.setText(data.getBankcardNumber());//还款卡号
////                MyTextUtils.setStatusType004(mBinding.tvLoanType, data.getCurNodeCode());
                mBinding.tvLoanType.setText(MainActivity.getNodeCode(data.getCurNodeCode()));

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                UITipDialog.showFall(CarLoanDetailsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();

            }
        });

    }


    private void initDatas() {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        Call call = RetrofitUtils.createApi(MyApiServer.class).getCarLoanDetailsActivity("630521", StringUtils.getJsonToString(map));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<CarLoanDetailsActivityBean>(this) {
            @Override
            protected void onSuccess(CarLoanDetailsActivityBean data, String SucMessage) {
                mdata = data;
                mBinding.tvBeForOver.setText(MoneyUtils.getShowPriceSign(data.getRestAmount()));

                mBinding.tvBeForOver.setText(MoneyUtils.showPrice(data.getRestAmount()));//这个先设置为本期本金
                mBinding.tvLoanCar.setText(data.getBudgetOrder().getCarModel());
                mBinding.tvLoanTotal.setText(MoneyUtils.showPriceDouble(data.getLoanAmount()));//贷款总额
                mBinding.tvLoanTerm.setText(data.getPeriods() + "");//贷款期限
                mBinding.tvRepaymentPlan.setText(data.getBankcardNumber());//还款卡号
                mBinding.tvLoanType.setText(MainActivity.getNodeCode(data.getCurMonthRepayPlan() == null ? "" : data.getCurMonthRepayPlan().getCurNodeCode()));
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                UITipDialog.showFall(CarLoanDetailsActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();

            }
        });
    }
}
