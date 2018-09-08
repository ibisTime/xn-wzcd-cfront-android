package com.cdkj.baselibrary.utils;

import com.cdkj.baselibrary.model.SystemKeyDataBean;

import java.util.List;

public interface OnSystemKeyListener {

    public void systemKeyValue(List<SystemKeyDataBean.ListBean> list);
}
