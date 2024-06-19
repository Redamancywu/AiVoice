package com.hi.base.service

import android.app.IntentService
import android.app.Notification
import android.content.Intent
import android.os.Build
import com.hi.base.helper.NotificationHelper
import com.hi.base.utils.HiLog
import com.hi.base.utils.SpUtils
import com.hi.voice.words.WordsTools


/**
 * FileName: InitService
 * Founder: LiuGuiLinInitService
 * Profile: 初始化服务
 */
class InitService : IntentService(InitService::class.simpleName) {

    override fun onCreate() {
        super.onCreate()
        HiLog.i("初始化开始")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(999, NotificationHelper.bindVoiceService(this,"正在运行"))
        }

    }

    override fun onHandleIntent(intent: Intent?) {
        HiLog.i("执行初始化操作")

        SpUtils.intSpUtils(this)
        WordsTools.initTools(this)
//        SoundPoolHelper.init(this)

        AppHelper.initHelper()
//        CommonSettingHelper.initHelper(this)
//        ConsTellHelper.initHelper(this)
//        AssetsUtils.initUtils(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        HiLog.i("初始化完成")
    }

}