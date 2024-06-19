package com.hi.voice
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.hi.base.adapter.BasePagerAdapter
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.base.utils.HiLog
import com.hi.network.HttpManager
import com.hi.voice.data.PagerData
import com.hi.voice.databinding.ActivityMainBinding
import com.hi.voice.inransformer.ScaleInTransformer
import com.hi.voice.service.VoiceService
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission

import com.hjq.permissions.XXPermissions
import com.imooc.lib_base.helper.`fun`.ContactHelper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : BaseActivity<ActivityMainBinding>() {
    //不建议一股脑的申请权限 而是应该根据使用到的场景是让用户同意权限
    private val permission = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
      //  Manifest.permission.VIBRATE,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.CAMERA
    )
    private val mList=ArrayList<PagerData>()
    private  val mListView=ArrayList<View>()
    override fun getTitleText(): String {
        return "首页"
    }

    override fun isShowBack(): Boolean {
        return false
    }

    override fun initView() {
        // 动态权限
        if (checkPermission(permission)) {
            // 如果已有权限，则直接链接服务和初始化联系人帮助类
            linkService()
        } else {
            // 请求权限
            XXPermissions.with(this)
                .permission(permission)
                .request(object :OnPermissionCallback{
                    override fun onGranted(permissions: List<String>, all: Boolean) {
                        if (all) {
                            // 所有请求的权限都被用户授予了
                            linkService()
                        } else {
                            // 用户未授予所有权限，可以在这里添加逻辑来处理这种情况
                            Toast.makeText(this@MainActivity, "请授予所有权限以正常使用应用功能", Toast.LENGTH_SHORT).show()
                            XXPermissions.startPermissionActivity(this@MainActivity, permissions)
                        }
                    }

                    override fun onDenied(permissions: List<String>, neverAskAgain: Boolean) {
                        if (neverAskAgain) {
                            // 用户选择了“不再询问”，可能需要引导用户手动在设置中开启权限
                            Toast.makeText(this@MainActivity, "您拒绝了部分权限，且选择了不再询问。请在设置中手动开启权限。", Toast.LENGTH_SHORT).show()
                            XXPermissions.startPermissionActivity(this@MainActivity, permissions)
                        } else {
                            // 用户拒绝了权限，但允许再次询问
                            Toast.makeText(this@MainActivity, "请授予所需权限以正常使用应用功能", Toast.LENGTH_SHORT).show()
                            XXPermissions.startPermissionActivity(this@MainActivity, permissions)
                        }
                    }
                })

        }
        if (!checkWindowPermission()){
            requestWindowPermission(packageName)
        }

        initData()

        initPagerView()

        // 窗口权限和其他初始化逻辑...

    }

    //初始化viewPager的试图
    private fun initPagerView() {
        //设置ViewPager的页边距为20
        binding.viewPager.pageMargin=20
        // 设置视图页面的偏移量，当视图页面过多时，会从超出此偏移量的页面开始缓存
        binding.viewPager.offscreenPageLimit = mList.size
        //设置适配器
        //binding.viewPager.adapter=BasePagerAdapter()
        binding.viewPager.adapter = BasePagerAdapter(mListView)
        binding.viewPager.setPageTransformer(true, ScaleInTransformer())
    }

    //初始化vie    @SuppressLint("Recycle")
    @SuppressLint("Recycle")
    private fun initData() {
        HiLog.e("initData:"+mList.size+"mListView:"+mListView.size)
        // 获取MainTitleArray数组
        val pagerTitle = resources.getStringArray(com.hi.base.R.array.MainTitleArray)
        // 获取MainColorArray数组
        val pagerColor = resources.getIntArray(R.array.MainColorArray)
        // 获取MainIconArray数组
        val pagerIcon = resources.obtainTypedArray(R.array.MainIconArray)
        HiLog.e("pagerData: ${pagerTitle.size},pagerColor: ${pagerColor.size},pagerIcon: ${pagerIcon.length()}")
        // 遍历数组
        for ((index, value) in pagerTitle.withIndex()) {
            // 添加PagerData对象到mList
            mList.add(PagerData(value, pagerIcon.getResourceId(index, 0), pagerColor[index]))
        }
//        //非调试版本去除工程模式
//        if (!BuildConfig.DEBUG) {
//            mList.removeAt(mList.size - 1)
//        }

        val windowHeight = windowManager.defaultDisplay.height
        HiLog.e("PagerData: ${mList.size},windowHeight: $windowHeight")
        mList.forEach {
            val view = View.inflate(this,R.layout.layout_main_list, null)
            val mCvMainView = view.findViewById<CardView>(R.id.mCvMainView)
            HiLog.e("mCvMainView: ${mCvMainView.resources},mCvMainView id: ${mCvMainView.id}")
            val mIvMainIcon = view.findViewById<ImageView>(R.id.mIvMainIcon)
            HiLog.e("mIvMainIcon: ${mIvMainIcon.resources},mIvMainIcon id: ${mIvMainIcon.id}")
            val mTvMainText = view.findViewById<TextView>(R.id.mTvMainText)
            HiLog.e("mTvMainText: ${mTvMainText.resources},mTvMainText id: ${mTvMainText.id}")
            HiLog.e("initData: ${it.title},pagerColor: ${it.color},pagerIcon: ${it.icon}")
            mCvMainView.setBackgroundColor(it.color)
            HiLog.e("setImageResource:"+it.icon)
            mIvMainIcon.setImageResource(it.icon)
            mTvMainText.text = it.title

            mCvMainView.layoutParams?.let { lp ->
                lp.height = windowHeight / 5 * 3
            }

            //点击事件
            view.setOnClickListener { _ ->
                HiLog.e("initData: ${it.title},pagerColor: ${it.color},pagerIcon: ${it.icon}")
                when (it.icon) {
                    R.drawable.img_main_weather-> ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)
                    R.drawable.img_mian_contell -> ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION)
                    R.drawable.img_main_joke_icon -> ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
                    R.drawable.img_main_map_icon -> ARouterHelper.startActivity(ARouterHelper.PATH_MAP)
                    R.drawable.img_main_app_manager -> ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
                    R.drawable.img_main_voice_setting -> ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)
                    R.drawable.img_main_system_setting -> ARouterHelper.startActivity(ARouterHelper.PATH_SETTING)
                    R.drawable.img_main_developer -> ARouterHelper.startActivity(ARouterHelper.PATH_DEVELOPER)
                }
            }

            mListView.add(view)
        }

    }

    private fun linkService(){
        ContactHelper.initHelper()
        startService(Intent(this, VoiceService::class.java))
    }


    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)


}