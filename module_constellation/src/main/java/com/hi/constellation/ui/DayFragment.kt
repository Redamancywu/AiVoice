package com.hi.constellation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.hi.base.base.BaseFragmentViewBinding
import com.hi.base.utils.HiLog
import com.hi.constellation.R
import com.hi.constellation.databinding.FragmentTodayBinding
import com.hi.network.HttpManager
import com.hi.network.bean.TodayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DayFragment(private val isToday: Boolean, val name: String) :
    BaseFragmentViewBinding<FragmentTodayBinding>(), Callback<TodayData> {
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTodayBinding {
        return FragmentTodayBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        if (isToday) {
            loadToday()
        } else {
            loadTomorrow()
        }
    }

    //加载今天的数据
    private fun loadToday() {
        HttpManager.queryTodayConsTellInfo(name, this)
    }

    //加载明天的数据
    private fun loadTomorrow() {
        HttpManager.queryTomorrowConsTellInfo(name, this)
    }

    override fun onResponse(call: Call<TodayData>, response: Response<TodayData>) {
        val data = response.body()
        data?.let {
            HiLog.i("it:$it")
            binding.tvName.text = getString(R.string.text_con_tell_name, it.name)
            binding.tvTime.text = getString(R.string.text_con_tell_time, it.datetime)
            binding.tvNumber.text = getString(R.string.text_con_tell_num, it.number)
            binding.tvFriend.text = getString(R.string.text_con_tell_pair, it.QFriend)
            binding.tvColor.text = getString(R.string.text_con_tell_color, it.color)
            binding.tvSummary.text = getString(R.string.text_con_tell_desc, it.summary)

            //pbAll.progress = it.all.substring(0, it.all.length - 1).toInt()
            //pbHealth.progress = it.health.substring(0, it.health.length - 1).toInt()
            //pbLove.progress = it.love.substring(0, it.love.length - 1).toInt()
            //pbMoney.progress = it.money.substring(0, it.money.length - 1).toInt()
            //pbWork.progress = it.work.substring(0, it.work.length - 1).toInt()

            binding.pbAll.progress = it.all
            binding.pbHealth.progress = it.health
            binding.pbLove.progress = it.love
            binding.pbMoney.progress = it.money
            binding.pbWork.progress = it.work
        }
    }

    override fun onFailure(call: Call<TodayData>, t: Throwable) {
        HiLog.e("onFailure:$t")
        Toast.makeText(activity, getString(com.hi.base.R.string.text_load_fail), Toast.LENGTH_LONG)
            .show()
    }
}