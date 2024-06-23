package com.hi.constellation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.hi.base.base.BaseFragmentViewBinding
import com.hi.base.utils.HiLog
import com.hi.constellation.R
import com.hi.constellation.databinding.FragmentMonthBinding
import com.hi.network.HttpManager
import com.hi.network.bean.MonthData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonthFragment(val name: String):BaseFragmentViewBinding<FragmentMonthBinding>() {
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMonthBinding {
        return FragmentMonthBinding.inflate(inflater,container,false)
    }
    override fun initView() {
        loadMonthData()
    }

    private fun loadMonthData() {
        HttpManager.queryMonthConsTellInfo(name, object : Callback<MonthData> {
            override fun onFailure(call: Call<MonthData>, t: Throwable) {
                HiLog.e("onFailure:$t")
                Toast.makeText(activity, getString(com.hi.base.R.string.text_load_fail), Toast.LENGTH_LONG)
                    .show()
            }

            override fun onResponse(call: Call<MonthData>, response: Response<MonthData>) {
                val data = response.body()
                data?.let {
                    HiLog.i("it:$it")
                    binding.tvName.text = it.name
                    binding.tvDate.text = it.date
                    binding.tvAll.text = it.all
                    binding.tvHappy.text = it.happyMagic
                    binding.tvHealth.text = it.health
                    binding.tvLove.text = it.love
                    binding.tvMoney.text = it.money
                    binding.tvMonth.text = getString(R.string.text_month_select, it.month)
                    binding.tvWork.text = it.work
                }
            }

        })
    }

}