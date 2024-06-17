package com.hi.setting

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.setting.databinding.ActivitySettingBinding
@Route(path = ARouterHelper.PATH_SETTING)
class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    override fun getTitleText(): String {
      //  TODO("Not yet implemented")
        return "设置"
    }

    override fun isShowBack(): Boolean {
        //TODO("Not yet implemented")
        return true
    }

    override fun initView() {
       // TODO("Not yet implemented")
    }

    override fun getViewBinding(): ActivitySettingBinding {
       // TODO("Not yet implemented")
        return ActivitySettingBinding.inflate(layoutInflater)
    }

}