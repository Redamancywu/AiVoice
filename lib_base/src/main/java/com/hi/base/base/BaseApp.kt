package com.hi.base.base

import AppHelper
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.TextUtils
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.hi.base.helper.ARouterHelper
import com.hi.base.helper.NotificationHelper
import com.hi.base.service.InitService
import com.hi.base.service.InitWorker
import com.hi.base.utils.CommonUtils
import com.hi.base.utils.HiLog
import com.hi.base.utils.SpUtils

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //只有主进程才能初始化
        val processName = CommonUtils.getProcessName(this)
        if (!TextUtils.isEmpty(processName)) {
            if (processName == packageName) {
                initApp()
            }
        }
        // 定义后台任务的约束条件
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            // 设置是否需要电池电量不足
            .setRequiresBatteryNotLow(false)
            // 设置是否需要充电
            .setRequiresCharging(false)
            // 设置是否需要存储空间不足
            .setRequiresStorageNotLow(false)
            .build()
        // 创建并调度OneTimeWorkRequest
        val workerRequest = OneTimeWorkRequest.Builder(InitWorker::class.java)
            .setConstraints(constraints)
            .build()
        // 使用适当级别的日志记录
        HiLog.d( "Scheduled worker: $workerRequest")
        // 调度后台任务
        WorkManager.getInstance(this).enqueue(workerRequest)
        // 初始化其他框架
        AppHelper.initHelper()

    }

    private fun initApp() {
        // 初始化ARouter框架
        ARouterHelper.initHelper(this)
        // 初始化SharedPreferences工具类
        SpUtils.intSpUtils(this)
        NotificationHelper.initHelper(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, InitService::class.java))
        } else {
            startService(Intent(this, InitService::class.java))
        }
    }
    fun getAppContext(): Context {
        return applicationContext
    }
}