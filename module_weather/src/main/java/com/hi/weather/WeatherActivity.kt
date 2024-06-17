package com.hi.weather

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.weather.databinding.ActivityWeatherBinding
@Route(path = ARouterHelper.PATH_WEATHER)
class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {
    override fun getTitleText(): String {
     //   TODO("Not yet implemented")
        return "天气"
    }

    override fun isShowBack(): Boolean {
      //  TODO("Not yet implemented")
        return false
    }

    override fun initView() {
      //  TODO("Not yet implemented")
    }

    override fun getViewBinding(): ActivityWeatherBinding {
       // TODO("Not yet implemented")
        return ActivityWeatherBinding.inflate(layoutInflater)
    }

}