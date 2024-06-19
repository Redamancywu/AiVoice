package com.hi.manager

import AppHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hi.base.R
import com.hi.base.adapter.BasePagerAdapter

import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.base.utils.HiLog
import com.hi.manager.databinding.ActivityAppManagerBinding

@Route(path = ARouterHelper.PATH_APP_MANAGER)
class AppManager : BaseActivity<ActivityAppManagerBinding>() {
    private val waitApp = 1000

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == waitApp) {
                waitAppHandler()
            }
        }
    }

    override fun getTitleText(): String {
        // TODO("Not yet implemented")
        return getString(R.string.app_title_app_manager)
    }

    override fun isShowBack(): Boolean {
        //  TODO("Not yet implemented")
        return true
    }

    override fun initView() {
        //加载布局
        binding.llLoading.gravity=View.VISIBLE
        //说明初始化AppView成功
        waitAppHandler()


    }

    override fun getViewBinding(): ActivityAppManagerBinding {
        // TODO("Not yet implemented")
        return ActivityAppManagerBinding.inflate(layoutInflater)
    }

    //等待应用加载完成
    private fun waitAppHandler() {
        HiLog.i("等待App列表刷新...")
        if (AppHelper.mAllViewList.size > 0) {
            initViewPager()
        } else {
            mHandler.sendEmptyMessageDelayed(waitApp, 1000)
            HiLog.e("等待App列表刷新..."+AppHelper.mAllViewList.size)
        }
    }

    private fun initViewPager() {
        //设置ViewPager的页数限制
        binding.mViewPager.offscreenPageLimit = AppHelper.getPageSize()
        //设置ViewPager的适配器
        binding.mViewPager.adapter = BasePagerAdapter(AppHelper.mAllViewList)
        //设置加载布局的显示状态
        binding.llLoading.gravity = View.GONE
        //设置内容布局的显示状态
        binding.llContent.gravity = View.VISIBLE

        binding.mPointLayoutView.setPointSize(AppHelper.getPageSize())

        //设置ViewPager的滑动监听
        binding.mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // 处理页面滚动事件
            }

            override fun onPageSelected(position: Int) {
                // 处理页面选择事件
            }

            override fun onPageScrollStateChanged(state: Int) {
                // 处理页面滚动状态变化事件
                binding.mPointLayoutView.setCheck(state)
            }

        })
    }

}