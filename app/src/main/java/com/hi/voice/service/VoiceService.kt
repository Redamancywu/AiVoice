package com.hi.voice.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.hi.base.helper.NotificationHelper
import com.hi.base.helper.SoundPoolHelper
import com.hi.base.helper.WindowHelper
import com.hi.base.utils.HiLog
import com.hi.base.utils.SpUtils
import com.hi.voice.R
import com.hi.voice.adapter.ChatListAdapter
import com.hi.voice.data.ChatList
import com.hi.voice.engine.VoiceEngineAnalyze
import com.hi.voice.enity.AppConstants
import com.hi.voice.itf.OnAsrResultListener
import com.hi.voice.itf.OnNluResultListener
import com.hi.voice.manager.VoiceManager
import com.hi.voice.tts.VoiceTTs
import com.hi.voice.words.WordsTools
import org.json.JSONObject

/**
 * 语音服务
 */
class VoiceService : Service(), OnNluResultListener {
    private val mHandler = Handler(Looper.getMainLooper())
    private lateinit var mFullWindowView: View
    private lateinit var mChatListView:RecyclerView
    private  val mChatList=ArrayList<ChatList>()
    private lateinit var mChatAdapter: ChatListAdapter
    private lateinit var mLottieView: LottieAnimationView
    private lateinit var tvVoiceTips: TextView
    private lateinit var ivCloseWindow: ImageView

    override fun onCreate() {
        super.onCreate()
        HiLog.e("VoiceService onCreate")
        initCoreVoiceService()
        NotificationHelper.initHelper(application)

    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bindNotification()
        HiLog.e("VoiceService onStartCommand")
        return START_STICKY_COMPATIBILITY
    }

    private fun bindNotification() {
        startForeground(
            NOTIFICATION_ID,
            NotificationHelper.bindVoiceService(this.applicationContext, "语音服务正在运行中")
        )
    }

    companion object {
        private const val NOTIFICATION_ID = 1000
    }

    private fun initCoreVoiceService() {
        WindowHelper.initHelper(this)
        mFullWindowView=WindowHelper.getView(R.layout.layout_window_item)
        mChatListView=mFullWindowView.findViewById(R.id.mChatListView)
        mLottieView = mFullWindowView.findViewById<LottieAnimationView>(R.id.mLottieView)
        ivCloseWindow = mFullWindowView.findViewById<ImageView>(R.id.ivCloseWindow)
        tvVoiceTips = mFullWindowView.findViewById<TextView>(R.id.tvVoiceTips)
        mChatListView.layoutManager=LinearLayoutManager(this)
        mChatAdapter = ChatListAdapter(mChatList)
        mChatListView.adapter = mChatAdapter
        val ivCloseWindow=mFullWindowView.findViewById<View>(R.id.ivCloseWindow)
        ivCloseWindow.setOnClickListener {
            hideTouchWindow()
        }
        //初始化语音服务
        VoiceManager.initVoiceManager(this, object : OnAsrResultListener {
            override fun wakeUpReady() {
                HiLog.i("开始唤醒")
                //发声人
                VoiceManager.setVoicePeople(
                    resources.getStringArray(com.hi.voicesetting.R.array.TTSPeopleIndex)[SpUtils.getInt(
                        "tts_people",
                        3
                    )]
                )
                //语速
                VoiceManager.setVoiceSpeed(SpUtils.getInt("tts_speed", 5).toString())
                //音量
                VoiceManager.setVoiceVolume(SpUtils.getInt("tts_volume", 5).toString())

                val isHello = SpUtils.getBoolean("isHello", true)
                if (isHello) {
                    //  addAiText("唤醒引擎准备就绪")
                }
            }

            override fun asrStartSpeak() {
                HiLog.i("开始说话")
            }

            override fun asrStopSpeak() {
                HiLog.i("停止说话")
                hideWindow()
            }

            override fun wakeUpSuccess(result: JSONObject) {
                HiLog.i("唤醒成功")
                HiLog.i("唤醒成功：$result")
                //当唤醒词是小爱同学的时候，才开启识别
                val errorCode = result.optInt("errorCode")
                //唤醒成功
                if (errorCode == 0) {
                    //唤醒词
                    val word = result.optString("word")
                    if (word == getString(com.hi.voice.R.string.text_voice_wakeup_text)) {
                          wakeUpFix()
                    }
                }
            }

            override fun updateUserText(text: String) {
                HiLog.i("用户输入文本：$text")
            }

            override fun asrResult(result: JSONObject) {
                HiLog.i("识别结果：$result")

            }

            override fun nluResult(nlu: JSONObject) {
                HiLog.i("语义结果：$nlu")
                VoiceEngineAnalyze.analyzeNlu(nlu, this@VoiceService)
            }

            override fun voiceError(text: String) {
                HiLog.e("语音错误：$text")
                hideWindow()
            }

        })
    }
    //显示窗口
    private fun showWindow() {
        HiLog.i("======显示窗口======")
        mLottieView.playAnimation()
        WindowHelper.show(mFullWindowView)
    }

    //隐藏窗口
    private fun hideWindow() {
        HiLog.i("======隐藏窗口======")
        mHandler.postDelayed({
            WindowHelper.hide(mFullWindowView)
            mLottieView.pauseAnimation()
            SoundPoolHelper.play(R.raw.record_over)
        }, 2 * 1000)
    }

    //直接隐藏窗口
    private fun hideTouchWindow() {
        HiLog.i("======隐藏窗口======")
        WindowHelper.hide(mFullWindowView)
        mLottieView.pauseAnimation()
        SoundPoolHelper.play(R.raw.record_over)
        VoiceManager.stopAsr()
    }
    /**
     * 唤醒成功之后的操作
     */
    private fun wakeUpFix() {
        showWindow()
        updateTips(getString(R.string.text_voice_wakeup_tips))
        SoundPoolHelper.play(R.raw.record_start)
        //应答
        val wakeupText = WordsTools.wakeupWords()
        addAiText(wakeupText,
            object : VoiceTTs.OnTTsResultListener {
                override fun onTTsEnd() {
                    //开启识别
                    VoiceManager.startAsr()
                }
            })
    }
    /**
     * 添加我的文本
     */
    private fun addMineText(text: String) {
        val bean = ChatList(AppConstants.TYPE_MINE_TEXT)
        bean.text = text
        baseAddItem(bean)
    }

    /**
     * 添加AI文本
     */
    private fun addAiText(text: String) {
        val bean = ChatList(AppConstants.TYPE_AI_TEXT)
        bean.text = text
        baseAddItem(bean)
        VoiceManager.ttsStart(text)
    }
    /**
     * 添加基类
     */
    private fun baseAddItem(bean: ChatList) {
        mChatList.add(bean)
        mChatAdapter.notifyItemInserted(mChatList.size - 1)
    }
    /**
     * 更新提示语
     */
    private fun updateTips(text: String) {
        tvVoiceTips.text = text
    }


    /**
     * 添加AI文本
     */
    private fun addAiText(text: String, mOnTTSResultListener: VoiceTTs.OnTTsResultListener) {
        val bean = ChatList(AppConstants.TYPE_AI_TEXT)
        bean.text = text
        baseAddItem(bean)
        VoiceManager.ttsStart(text, mOnTTSResultListener)
    }


    override fun queryWeather(city: String) {
        // TODO("Not yet implemented")
    }
}