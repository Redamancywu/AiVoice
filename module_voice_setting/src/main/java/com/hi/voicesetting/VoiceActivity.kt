package com.hi.voicesetting

import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hi.base.adapter.CommonAdapter
import com.hi.base.adapter.CommonViewHolder
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.base.utils.HiLog
import com.hi.voice.tts.VoiceTTs
import com.hi.voice.manager.VoiceManager
import com.hi.voicesetting.databinding.ActivityVoiceBinding

@Route(path = ARouterHelper.PATH_VOICE_SETTING)
class VoiceActivity : BaseActivity<ActivityVoiceBinding>() {
    private var mList = mutableListOf<String>()
   private var mTtsPeopleIndex: Array<String>?=null
    override fun getTitleText(): String {
        // TODO("Not yet implemented")
        return "语音设置"
    }

    override fun isShowBack(): Boolean {
        //TODO("Not yet implemented")
        return true
    }

    override fun initView() {
        //默认
        binding.barVoiceSpeed.progress = 5
        binding.barVoiceVolume.progress = 5
        //最大值
        binding.barVoiceSpeed.max = 15
        binding.barVoiceVolume.max = 15
        initListener()
        initData()
        initPeopleView()
        binding.btnTest.setOnClickListener {
            VoiceManager.ttsStart("你好 欢迎唤起语音助手，有什么需要帮助的吗？",
                object : VoiceTTs.OnTTsResultListener {
                    override fun onTTsEnd() {
                        // TODO("Not yet implemented")
                        HiLog.v("语音播报结束")
                    }

                })
        }
    }

    //初始化数据
    private fun initData() {
        //   TODO("Not yet implemented")
        val mTtsPeople = resources.getStringArray(R.array.TTSPeople)
        mTtsPeopleIndex = resources.getStringArray(R.array.TTSPeopleIndex)
        mTtsPeople.forEach { mList.add(it) }
    }
    /**
     * 初始化发音人
     */
    private fun initPeopleView() {

        binding.rvVoicePeople.layoutManager = LinearLayoutManager(this)
        binding.rvVoicePeople.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        binding.rvVoicePeople.adapter =
            CommonAdapter(mList, object : CommonAdapter.OnBindDataListener<String> {
                override fun onBindViewHolder(
                    model: String,
                    holder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    holder.setText(R.id.mTvPeopleContent, model)
                    holder.itemView.setOnClickListener {
                        mTtsPeopleIndex?.let {
                            VoiceManager.setVoicePeople(it[position])
                        }
                    }
                }

                override fun getLayoutId(type: Int): Int {
                    return R.layout.layout_tts_people_list
                }

            })
    }

    private fun initListener() {
        //监听语速
        binding.barVoiceSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.barVoiceSpeed.progress = progress
                VoiceManager.setVoicePeople(progress.toString())


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        //设置音量
        binding.barVoiceVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.barVoiceVolume.progress = progress
                VoiceManager.setVoiceVolume(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

    }

    override fun getViewBinding(): ActivityVoiceBinding {

        return ActivityVoiceBinding.inflate(layoutInflater)
    }

}