package com.hi.base.base

import android.app.Application
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.hi.base.helper.ARouterHelper
import com.hi.base.service.InitWorker
import com.hi.base.utils.HiLog
import com.hi.base.utils.SpUtils

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化ARouter框架
        ARouterHelper.initHelper(this)

        // 初始化SharedPreferences工具类
        SpUtils.intSpUtils(this)

        // 定义后台任务的约束条件
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .setRequiresStorageNotLow(true)
            .build()

        // 创建并调度OneTimeWorkRequest
        val workerRequest = OneTimeWorkRequest.Builder(InitWorker::class.java)
            .setConstraints(constraints)
            .build()

        // 使用适当级别的日志记录
        HiLog.d( "Scheduled worker: $workerRequest")

        // 调度后台任务
        WorkManager.getInstance(this).enqueue(workerRequest)
    }
}