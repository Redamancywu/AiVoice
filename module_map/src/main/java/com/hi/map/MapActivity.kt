package com.hi.map

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.map.databinding.ActivityMapBinding
@Route(path = ARouterHelper.PATH_MAP)
class MapActivity : BaseActivity<ActivityMapBinding>() {
    override fun getTitleText(): String {
      //  TODO("Not yet implemented")
        return "地图"
    }

    override fun isShowBack(): Boolean {
       // TODO("Not yet implemented")
        return true
    }

    override fun initView() {
       /// TODO("Not yet implemented")
    }

    override fun getViewBinding(): ActivityMapBinding {
       // TODO("Not yet implemented")
        return ActivityMapBinding.inflate(layoutInflater)
    }

}