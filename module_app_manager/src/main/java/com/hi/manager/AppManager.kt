package com.hi.manager

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.hi.base.R

import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.manager.databinding.ActivityAppManagerBinding

@Route(path = ARouterHelper.PATH_APP_MANAGER)
class AppManager : BaseActivity<ActivityAppManagerBinding>() {
    override fun getTitleText(): String {
       // TODO("Not yet implemented")
        return getString(R.string.app_title_app_manager)
    }

    override fun isShowBack(): Boolean {
      //  TODO("Not yet implemented")
        return true
    }

    override fun initView() {
       // TODO("Not yet implemented")
    }

    override fun getViewBinding(): ActivityAppManagerBinding {
       // TODO("Not yet implemented")
        return ActivityAppManagerBinding.inflate(layoutInflater)
    }

}