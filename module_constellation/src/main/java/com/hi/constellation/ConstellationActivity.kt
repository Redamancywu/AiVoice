package com.hi.constellation

import android.graphics.Color
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.base.utils.AppGlobals
import com.hi.base.utils.SpUtils
import com.hi.constellation.databinding.ActivityConstellationBinding
import com.hi.constellation.ui.DayFragment
import com.hi.constellation.ui.MonthFragment
import com.hi.constellation.ui.WeekFragment
import com.hi.constellation.ui.YearFragment

@Route(path = ARouterHelper.PATH_CONSTELLATION)
class ConstellationActivity : BaseActivity<ActivityConstellationBinding>() {
    private lateinit var mTodayFragment: DayFragment
    private lateinit var mTomorrowFragment: DayFragment
    private lateinit var mWeekFragment: WeekFragment
    private lateinit var mMonthFragment: MonthFragment
    private lateinit var mYearFragment: YearFragment
    private val mListFragment = ArrayList<Fragment>()
    override fun getTitleText(): String {
        return getString(com.hi.base.R.string.app_title_constellation)
    }
    override fun isShowBack(): Boolean {
        return true
    }
    override fun initView() {
        val name = intent.getStringExtra("name")
        if (!TextUtils.isEmpty(name)) {
            //语音进来的
            if (name != null) {
                initFragment(name)
            }
        } else {
            //主页进来的,读取历史
            val consTellName = SpUtils.getString("consTell")
            consTellName?.let {
                if (!TextUtils.isEmpty(it)) {
                    initFragment(it)
                } else {
                    initFragment(getString(R.string.text_def_con_tell))
                }
            }
        }
        //View控制
        binding.mTvToday.setOnClickListener {
            checkTab(true, 0)
        }
        binding.mTvTomorrow.setOnClickListener {
            checkTab(true, 1)
        }
        binding.mTvWeek.setOnClickListener {
            checkTab(true, 2)
        }
        binding.mTvMonth.setOnClickListener {
            checkTab(true, 3)
        }
        binding.mTvYear.setOnClickListener {
            checkTab(true, 4)
        }
    }
    //ViewPage + Fragment 实现滑动切换页面
    private fun initFragment(name: String) {

        SpUtils.putString("consTell", name)
        //设置标题
        supportActionBar?.title = name
        mTodayFragment = DayFragment(true, name)
        mTomorrowFragment = DayFragment(false, name)
        mWeekFragment = WeekFragment(name)
        mMonthFragment = MonthFragment(name)
        mYearFragment = YearFragment(name)
        mListFragment.add(mTodayFragment)
        mListFragment.add(mTomorrowFragment)
        mListFragment.add(mWeekFragment)
        mListFragment.add(mMonthFragment)
        mListFragment.add(mYearFragment)
        //初始化页面
        initViewPager()
    }
    private fun initViewPager() {
        binding.mViewPager.adapter = ViewPagerFragmentAdapter(supportFragmentManager)
        binding.mViewPager.offscreenPageLimit = mListFragment.size
        binding.mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                checkTab(false, position)
            }
        })

        //等待全部初始化之后采取做UI控制操作
        checkTab(false, 0)
    }
    //适配器
    inner class ViewPagerFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return mListFragment[position]
        }

        override fun getCount(): Int {
            return mListFragment.size
        }
    }
    //切换选项卡
    private fun checkTab(isClick: Boolean, index: Int) {
        if (isClick) {
            binding.mViewPager.currentItem = index
        }
        binding.mTvToday.setTextColor(if (index == 0) Color.RED else Color.BLACK)
        binding.mTvTomorrow.setTextColor(if (index == 1) Color.RED else Color.BLACK)
        binding.mTvWeek.setTextColor(if (index == 2) Color.RED else Color.BLACK)
        binding.mTvMonth.setTextColor(if (index == 3) Color.RED else Color.BLACK)
        binding.mTvYear.setTextColor(if (index == 4) Color.RED else Color.BLACK)
    }

    override fun getViewBinding(): ActivityConstellationBinding {

        return ActivityConstellationBinding.inflate(layoutInflater)
    }

}