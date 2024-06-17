package com.hi.joke

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.joke.databinding.ActivityJokBinding
@Route(path = ARouterHelper.PATH_JOKE)
class JokActivity : BaseActivity<ActivityJokBinding>() {
    override fun getTitleText(): String {
      //  TODO("Not yet implemented")
        return "笑话"
    }

    override fun isShowBack(): Boolean {
       // TODO("Not yet implemented")
        return true
    }

    override fun initView() {
       // TODO("Not yet implemented")
    }

    override fun getViewBinding(): ActivityJokBinding {
      //  TODO("Not yet implemented")
        return ActivityJokBinding.inflate(layoutInflater)
    }

}