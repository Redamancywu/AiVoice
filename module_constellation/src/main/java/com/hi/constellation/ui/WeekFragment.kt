package com.hi.constellation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.hi.base.base.BaseFragmentViewBinding
import com.hi.base.utils.HiLog
import com.hi.constellation.R
import com.hi.constellation.databinding.FragmentWeekBinding
import com.hi.network.HttpManager
import com.hi.network.bean.WeekData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeekFragment(val name: String) : BaseFragmentViewBinding<FragmentWeekBinding>() {
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWeekBinding {
        return FragmentWeekBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        loadWeekData()
    }

    private fun loadWeekData() {
        HttpManager.queryWeekConsTellInfo(name, object : Callback<WeekData> {
            override fun onFailure(call: Call<WeekData>, t: Throwable) {
                HiLog.e("onFailure:$t")
                Toast.makeText(
                    activity,
                    getString(com.hi.base.R.string.text_load_fail),
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            override fun onResponse(call: Call<WeekData>, response: Response<WeekData>) {
                val data = response.body()
                data?.let {
                    HiLog.i("it:$it")
                    binding.tvName.text = it.name
                    binding.tvData.text = it.date
                    binding.tvWeekth.text = getString(R.string.text_week_select, it.weekth)
                    binding.tvHealth.text = it.health
                    binding.tvJob.text = it.job
                    binding.tvLove.text = it.love
                    binding.tvMoney.text = it.money
                    binding.tvWork.text = it.work
                }
            }

        })
    }
}