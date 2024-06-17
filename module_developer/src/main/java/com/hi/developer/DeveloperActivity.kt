package com.hi.developer


import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.baidu.speech.EventListener
import com.hi.base.adapter.CommonAdapter
import com.hi.base.adapter.CommonViewHolder
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.developer.data.DeveloperListData
import com.hi.developer.databinding.ActivityDeveloperBinding
import com.hi.voice.tts.VoiceTTs
import com.hi.voice.manager.VoiceManager

@Route(path = ARouterHelper.PATH_DEVELOPER)
class DeveloperActivity : BaseActivity<ActivityDeveloperBinding>() {
    private val mTitleType = 0  //标题
    private val mTitleMsg = 1   //内容
    private val mList = ArrayList<DeveloperListData>()
    override fun getTitleText(): String {
        return getString(com.hi.base.R.string.app_title_developer)
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {
        initData()
        initListView()
    }

    private fun initData() {
        val dataArray = resources.getStringArray(com.hi.base.R.array.DeveloperListArray)
        dataArray.forEach {
            //如果有中括号 说明是标题
            if (it.contains("[")) {
                addItemData(mTitleType, it.replace("[", "").replace("]", ""))
            } else {
                addItemData(mTitleMsg, it)
            }
        }
    }

    private fun initListView() {
        //布局管理器
        binding.devRecycle.layoutManager = LinearLayoutManager(this)
        //分割线
        binding.devRecycle.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.devRecycle.adapter =
            CommonAdapter(mList, object : CommonAdapter.OnMoreBindDataListener<DeveloperListData> {
                override fun onBindViewHolder(
                    model: DeveloperListData,
                    holder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    //根据model.type的值，将不同的数据绑定到不同的视图
                    when (model.type) {
                        mTitleType -> {
                            holder.setText(R.id.mTvDeveloperTitle, model.msg)
                        }

                        mTitleMsg -> {
                            holder.setText(R.id.mTvDeveloperContent, "${position}.${model.msg}")
                            holder.itemView.setOnClickListener {
                                itemClick(position)
                            }
                        }
                    }
                }

                //根据position的值，返回不同的item类型
                override fun getItemType(position: Int): Int {
                    return mList[position].type
                }

                //根据type的值，返回不同的布局
                override fun getLayoutId(type: Int): Int {
                    return if (type == mTitleType) {
                        R.layout.item_developer_title
                    } else {
                        R.layout.item_developer_msg
                    }
                }
            }
            )
    }

    // 返回ActivityDeveloperBinding对象
    override fun getViewBinding(): ActivityDeveloperBinding {
        return ActivityDeveloperBinding.inflate(layoutInflater)
    }

    // 添加项目数据
    private fun addItemData(type: Int, msg: String) {
        val data = DeveloperListData(type, msg)
        mList.add(data)
    }

    // 根据位置判断点击事件
    private fun itemClick(position: Int) {
        when (position) {
            1 -> ARouterHelper.startActivity(ARouterHelper.PATH_APP_MANAGER)
            2 -> ARouterHelper.startActivity(ARouterHelper.PATH_CONSTELLATION)
            3 -> ARouterHelper.startActivity(ARouterHelper.PATH_JOKE)
            4 -> ARouterHelper.startActivity(ARouterHelper.PATH_MAP)
            5 -> ARouterHelper.startActivity(ARouterHelper.PATH_SETTING)
            6 -> ARouterHelper.startActivity(ARouterHelper.PATH_VOICE_SETTING)
            7 -> ARouterHelper.startActivity(ARouterHelper.PATH_WEATHER)

            9 -> VoiceManager.startAsr()
            10 -> VoiceManager.stopAsr()
            11 -> VoiceManager.cancelAsr()
            12 -> VoiceManager.releaseAsr()


            14 -> VoiceManager.startWakeUp()
            15 -> VoiceManager.stopWakeUp()


            20 -> VoiceManager.ttsStart("丝袜，给我打个那个熊和双鸟， 谢谢")
            21 -> VoiceManager.ttsPause()
            22 -> VoiceManager.ttsResume()
            23 -> VoiceManager.ttsStop()
            24 -> VoiceManager.ttsRelease()
        }
    }
}