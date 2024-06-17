package com.hi.base.helper

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.hi.base.base.BaseApp

/**
 * 通知栏的帮助类
 */
object NotificationHelper {
    private var nm:NotificationManager?=null
    private const val CHANNEL_ID="ai_voice_service"
    private const val CHANNEL_NAME="语音服务"

    //初始化类
    fun initHelper(mContext: Context){
      //  this.mContext = mContext.applicationContext
        nm= mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        setBindServiceChannel()
    }
    //设置绑定服务的渠道
    @SuppressLint("SuspiciousIndentation")
    fun setBindServiceChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel=  NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            //呼吸灯
            channel.enableLights(false)
            //震动
            channel.enableVibration(false)
            //角标
            channel.setShowBadge(false)
            //创建渠道对象
            nm?.createNotificationChannel(channel)
        }
    }
    //绑定服务
    fun bindVoiceService(mContext: Context,message:String?):Notification{
        //创建通知栏
        val notificationCompat=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationCompat.Builder(mContext,CHANNEL_ID)
        }else{
            NotificationCompat.Builder(mContext)
        }
        ///设置标题
        notificationCompat.setContentTitle(CHANNEL_NAME)
        //设置内容
        notificationCompat.setContentText(message)
        //设置时间
        notificationCompat.setWhen(System.currentTimeMillis())
        //静止滑动
        notificationCompat.setAutoCancel(false)
        return notificationCompat.build()
    }
}