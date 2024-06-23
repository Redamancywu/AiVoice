package com.hi.voice.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.hi.base.helper.ARouterHelper
import com.hi.base.helper.CommonSettingHelper
import com.hi.base.helper.NotificationHelper
import com.hi.base.helper.SoundPoolHelper
import com.hi.base.helper.WindowHelper
import com.hi.base.utils.HiLog
import com.hi.base.utils.SpUtils
import com.hi.network.HttpManager
import com.hi.network.bean.JokeOneData
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 语音服务
 */
class VoiceService : Service(), OnNluResultListener {
    private val mHandler = Handler(Looper.getMainLooper())
    private lateinit var mFullWindowView: View
    private lateinit var mChatListView: RecyclerView
    private val mChatList = ArrayList<ChatList>()
    private lateinit var mChatAdapter: ChatListAdapter
    private lateinit var mLottieView: LottieAnimationView
    private lateinit var tvVoiceTips: TextView
    private lateinit var ivCloseWindow: ImageView

    override fun onCreate() {
        super.onCreate()
        HiLog.e("VoiceService onCreate")
        initCoreVoiceService()


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
        mFullWindowView = WindowHelper.getView(R.layout.layout_window_item)
        mChatListView = mFullWindowView.findViewById(R.id.mChatListView)
        mLottieView = mFullWindowView.findViewById<LottieAnimationView>(R.id.mLottieView)
        ivCloseWindow = mFullWindowView.findViewById<ImageView>(R.id.ivCloseWindow)
        tvVoiceTips = mFullWindowView.findViewById<TextView>(R.id.tvVoiceTips)
        mChatListView.layoutManager = LinearLayoutManager(this)
        mChatAdapter = ChatListAdapter(mChatList)
        mChatListView.adapter = mChatAdapter
        val ivCloseWindow = mFullWindowView.findViewById<View>(R.id.ivCloseWindow)
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

    //查询天气详情
    override fun queryWeatherInfo(city: String) {
        addAiText(getString(R.string.text_voice_query_weather, city))
        ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER, "city", city)
        hideWindow()
    }

    override fun nearByMap(poi: String) {
        HiLog.i("nearByMap:$poi")
        addAiText(getString(R.string.text_voice_query_poi, poi))
        ARouterHelper.startActivity(ARouterHelper.PATH_MAP, "type", "poi", "keyword", poi)
        hideWindow()
    }

    override fun routeMap(address: String) {
        HiLog.i("routeMap:$address")
        addAiText(getString(R.string.text_voice_query_navi, address))
        ARouterHelper.startActivity(ARouterHelper.PATH_MAP, "type", "route", "keyword", address)
        hideWindow()
    }

    override fun nluError() {
        //暂不支持
        addAiText(WordsTools.noSupportWords())
        hideWindow()
    }

    //播放笑话
    override fun playJoke() {
        HttpManager.queryJoke(object : Callback<JokeOneData> {
            override fun onFailure(call: Call<JokeOneData>, t: Throwable) {
                HiLog.i("onFailure:$t")
                jokeError()
            }

            override fun onResponse(call: Call<JokeOneData>, response: Response<JokeOneData>) {
                HiLog.i("Joke onResponse")
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.error_code == 0) {
                            //根据Result随机抽取一段笑话进行播放
                            val index = WordsTools.randomInt(it.result.size)
                            HiLog.i("index:$index")
                            if (index < it.result.size) {
                                val data = it.result[index]
                                addAiText(data.content, object : VoiceTTs.OnTTsResultListener {
                                    override fun onTTsEnd() {
                                        hideWindow()
                                    }

                                })
                            }
                        } else {
                            jokeError()
                        }
                    }
                } else {
                    jokeError()
                }
            }
        })
    }

    //随机笑话
    override fun jokeList() {
        addAiText(getString(R.string.text_voice_query_joke))
        ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
        hideWindow()
    }

    override fun conTellTime(name: String) {
        TODO("Not yet implemented")
    }

    override fun conTellInfo(name: String) {
        TODO("Not yet implemented")
    }

    override fun aiRobot(text: String) {
        TODO("Not yet implemented")
    }

    /**
     * 笑话错误
     */
    private fun jokeError() {
        hideWindow()
        addAiText(getString(R.string.text_voice_query_joke_error))
    }

    //打开APP
    override fun openApp(appName: String) {
        if (!TextUtils.isEmpty(appName)) {
            HiLog.i("Open App $appName")
            val isOpen = AppHelper.launcherApp(appName)
            if (isOpen) {
                addAiText(getString(R.string.text_voice_app_open, appName))
            } else {
                addAiText(getString(R.string.text_voice_app_not_open, appName))
            }
        }
        hideWindow()
    }

    //卸载App
    override fun unInstallApp(appName: String) {
        if (!TextUtils.isEmpty(appName)) {
            HiLog.i("unInstall App $appName")
            val isUninstall = AppHelper.unInstallApp(appName)
            if (isUninstall) {
                addAiText(getString(R.string.text_voice_app_uninstall, appName))
            } else {
                addAiText(getString(R.string.text_voice_app_not_uninstall))
            }
        }
        hideWindow()
    }

    //其他App
    override fun otherApp(appName: String) {
        //全部跳转应用市场
        if (!TextUtils.isEmpty(appName)) {
            val isIntent = AppHelper.launcherAppStore(appName)
            if (isIntent) {
                addAiText(getString(R.string.text_voice_app_option, appName))
            } else {
                addAiText(WordsTools.noAnswerWords())
            }
        }
        hideWindow()
    }

    //返回
    override fun back() {
        addAiText(getString(R.string.text_voice_back_text))
        CommonSettingHelper.back()
        hideWindow()
    }

    //主页
    override fun home() {
        addAiText(getString(R.string.text_voice_home_text))
        CommonSettingHelper.home()
        hideWindow()
    }

    //音量+
    override fun setVolumeUp() {
        addAiText(getString(R.string.text_voice_volume_add))
        CommonSettingHelper.setVolumeUp()
        hideWindow()
    }

    //音量-
    override fun setVolumeDown() {
        addAiText(getString(R.string.text_voice_volume_sub))
        CommonSettingHelper.setVolumeDown()
        hideWindow()
    }

    //退下
    override fun quit() {
        addAiText(WordsTools.quitWords(), object : VoiceTTs.OnTTsResultListener {
            override fun onTTsEnd() {
               hideTouchWindow()
            }
        })
    }

    override fun callPhoneForName(name: String) {
        // TODO("Not yet implemented")
    }

    override fun callPhoneForNumber(phone: String) {
        // TODO("Not yet implemented")
    }

}