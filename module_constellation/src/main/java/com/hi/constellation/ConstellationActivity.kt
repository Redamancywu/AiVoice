package com.hi.constellation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.constellation.databinding.ActivityConstellationBinding

@Route(path = ARouterHelper.PATH_CONSTELLATION)
class ConstellationActivity : BaseActivity<ActivityConstellationBinding>() {
    override fun getTitleText(): String {
      //  TODO("Not yet implemented")
        return "星座"
    }

    override fun isShowBack(): Boolean {
      return true
    }

    override fun initView() {
      //  TODO("Not yet implemented")
    }

    override fun getViewBinding(): ActivityConstellationBinding {
      //  TODO("Not yet implemented")
        return ActivityConstellationBinding.inflate(layoutInflater)
    }

}