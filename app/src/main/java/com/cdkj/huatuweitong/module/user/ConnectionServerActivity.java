package com.cdkj.huatuweitong.module.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.huatuweitong.R;
import com.cdkj.huatuweitong.api.MyApiServer;
import com.cdkj.huatuweitong.bean.SystemParameterBean;
import com.cdkj.huatuweitong.databinding.ActivityConnectionServerBinding;

import java.util.HashMap;

import retrofit2.Call;

public class ConnectionServerActivity extends AbsBaseLoadActivity {
    private ActivityConnectionServerBinding mBinding;
    private String telephone;

    public static void open(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, ConnectionServerActivity.class);
            context.startActivity(intent);
        }

    }

    @Override
    public View addMainView() {
//        ActivityConn
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_connection_server, null, false);
        return mBinding.getRoot();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("客服");

        initData();
        initOnClick();


    }

    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "telephone");
        Call call = RetrofitUtils.createApi(MyApiServer.class).getTelephone("630047", StringUtils.getJsonToString(map));
        call.enqueue(new BaseResponseModelCallBack<SystemParameterBean>(this) {


            @Override
            protected void onSuccess(SystemParameterBean data, String SucMessage) {
                if (data == null) {
                    return;
                }
                telephone = data.getCvalue();
                mBinding.tvTelephone.setText(telephone);
            }

            @Override
            protected void onFinish() {

            }
        });
    }


    private void initOnClick() {
        mBinding.btnCall.setOnClickListener(v -> {

            if (TextUtils.isEmpty(telephone)) {
                UITipDialog.showInfo(this, "没有获取到服务热线");
                return;
            }
            checkPermissions();

//            callPhone();
            // showCallPhoneDialog();
        });
        mBinding.btnCancel.setOnClickListener(v -> finish());
    }

    private void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telephone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    private void checkPermissions() {

        if (!PermissionHelper.hasPermissions(this, Manifest.permission.CALL_PHONE)) {

            PermissionHelper permissionHelper = new PermissionHelper(this);
            permissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
                @Override
                public void doAfterGrand(String... permission) {
                    //通过
                    callPhone();
                }

                @Override
                public void doAfterDenied(String... permission) {
                    //未通过
                    UITipDialog.showInfo(ConnectionServerActivity.this, "请到设置中打开权限");
                }
            });
        } else {
            callPhone();
        }

    }

    private void showCallPhoneDialog() {
        new CommonDialog(ConnectionServerActivity.this)
                .builder()
                .setTitle("提示").setContentMsg("是否拨打客服电话")
                .setNegativeBtn("取消", null, false)
                .setPositiveBtn("确定", new CommonDialog.OnPositiveListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPositive(View view) {

                    }
                }).show();
    }
}
