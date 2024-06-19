package com.hi.base.service

import AppHelper
import android.app.IntentService
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import com.hi.base.helper.NotificationHelper
import com.hi.base.utils.HiLog
import com.hi.base.utils.SpUtils
import com.hi.voice.manager.VoiceManager

/**
 * @author RedamancyWu
 * @date 2024/6/5
 * 初始化服务
 * @description
 */
class InitWorker(mContext: Context,workParams: WorkerParameters) : Worker(mContext,workParams){

    override fun doWork(): Result {
        //TODO("Not yet implemented")
        // 初始化操作
        HiLog.e("初始化服务")
        return Result.success()
    }
    override fun onStopped() {
        super.onStopped()
    }

}