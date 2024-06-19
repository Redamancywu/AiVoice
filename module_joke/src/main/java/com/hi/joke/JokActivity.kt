package com.hi.joke

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.hi.base.adapter.CommonAdapter
import com.hi.base.adapter.CommonViewHolder
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.base.utils.HiLog
import com.hi.joke.databinding.ActivityJokBinding
import com.hi.network.HttpManager
import com.hi.network.bead.Data
import com.hi.network.bead.JokeListData
import com.hi.voice.manager.VoiceManager
import com.hi.voice.tts.VoiceTTs
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Route(path = ARouterHelper.PATH_JOKE)
class JokActivity : BaseActivity<ActivityJokBinding>(), OnRefreshListener,
    OnRefreshLoadMoreListener {
    //页码
    private var currentPage = 1

    //数据源
    private val mList = ArrayList<Data>()
    private lateinit var mJokeAdapter: CommonAdapter<Data>

    //当前播放的下标
    private var currentPlayIndex = -1
    override fun getTitleText(): String {
        //  TODO("Not yet implemented")
        return getString(com.hi.base.R.string.app_title_joke)
    }

    override fun isShowBack(): Boolean {
        // TODO("Not yet implemented")
        return true
    }

    override fun initView() {
        initList()
        loadData(0)
    }
    //初始化列表
    private fun initList() {
        //刷新组件
        binding.refreshLayout.setRefreshHeader(ClassicsHeader(this))
        binding.refreshLayout.setRefreshFooter(ClassicsFooter(this))
        //监听
        binding.refreshLayout.setOnRefreshListener(this)
        binding.refreshLayout.setOnRefreshLoadMoreListener(this)
        binding.mJokeListView.layoutManager = LinearLayoutManager(this)
        mJokeAdapter = CommonAdapter(mList, object : CommonAdapter.OnBindDataListener<Data> {
            override fun onBindViewHolder(
                model: Data,
                viewHolder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                //内容
                viewHolder.setText(R.id.tvContent, model.content)

                //播放按钮
                val tvPlay = viewHolder.getView(R.id.tvPlay) as TextView
                //设置文本
                tvPlay.text =
                    if (currentPlayIndex == position) getString(
                        com.hi.base.R.string.app_media_play
                    ) else getString(
                        com.hi.base.R.string.app_media_play
                    )

                //点击事件
                tvPlay.setOnClickListener {
                    //比想象中更加复杂
                    //说明点击了正在播放的下标
                    if (currentPlayIndex == position) {
                        VoiceManager.ttsPause()
                        currentPlayIndex = -1
                        tvPlay.text = getString(com.hi.base.R.string.app_media_play)
                    } else {
                        val oldIndex = currentPlayIndex
                        currentPlayIndex = position
                        VoiceManager.ttsStop()
                        VoiceManager.ttsStart(model.content, object : VoiceTTs.OnTTsResultListener {
                            override fun onTTsEnd() {
                                currentPlayIndex = -1
                                tvPlay.text = getString(com.hi.base.R.string.app_media_play)
                            }

                        })
                        tvPlay.text = getString(com.hi.base.R.string.app_media_pause)
                        //刷新旧的
                        mJokeAdapter.notifyItemChanged(oldIndex)
                    }
                }
            }

            override fun getLayoutId(type: Int): Int {
                return R.layout.layout_joke_list_item
            }
        })
        binding.mJokeListView.adapter = mJokeAdapter
    }

    override fun getViewBinding(): ActivityJokBinding {
        //  TODO("Not yet implemented")
        return ActivityJokBinding.inflate(layoutInflater)
    }

    //加载数据 0:下拉 1：上拉
    private fun loadData(orientation: Int) {
        HttpManager.queryJokeList(currentPage, object : Callback<JokeListData> {
            override fun onFailure(call: Call<JokeListData>, t: Throwable) {
                HiLog.i("onFailure$t")
                if (orientation == 0) {
                    binding.refreshLayout.finishRefresh()
                } else if (orientation == 1) {
                    binding.refreshLayout.finishLoadMore()
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<JokeListData>, response: Response<JokeListData>) {
                HiLog.i("JokActivity onResponse:$orientation")
                if (orientation == 0) {
                    binding.refreshLayout.finishRefresh()
                    //列表要清空
                    mList.clear()
                    response.body()?.result?.data?.let { mList.addAll(it) }
                    //适配器要全部刷新
                    mJokeAdapter.notifyDataSetChanged()
                } else if (orientation == 1) {
                    binding.refreshLayout.finishLoadMore()
                    //追加在尾部
                    response.body()?.result?.data?.let {
                        //上一次的最大值
                        val positionStart = mList.size
                        mList.addAll(it)
                        //局部刷新
                        mJokeAdapter.notifyItemRangeInserted(positionStart, it.size)
                    }
                }
            }
        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        currentPage = 1
        loadData(0)
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        currentPage++
        loadData(1)
    }

}