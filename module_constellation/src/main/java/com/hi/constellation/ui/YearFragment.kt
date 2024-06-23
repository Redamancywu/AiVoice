package com.hi.constellation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.hi.base.base.BaseFragmentViewBinding
import com.hi.base.utils.HiLog
import com.hi.constellation.databinding.FragmentYearBinding
import com.hi.network.HttpManager
import com.hi.network.bean.YearData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YearFragment(val name: String) : BaseFragmentViewBinding<FragmentYearBinding>() {
    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentYearBinding {
        return FragmentYearBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        loadYearData()
    }

    //加载年份数据
    private fun loadYearData() {
        HttpManager.queryYearConsTellInfo(name, object : Callback<YearData> {

            override fun onFailure(call: Call<YearData>, t: Throwable) {
                HiLog.e("onFailure:$t")
                Toast.makeText(
                    activity,
                    getString(com.hi.base.R.string.text_load_fail),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<YearData>, response: Response<YearData>) {
                val data = response.body()
                data?.let {
                    HiLog.i("it:$it")

                    binding.tvName.text = it.name
                    binding.tvDate.text = it.date

                    binding.tvInfo.text = it.mima.info
                    binding.tvInfoText.text = it.mima.text[0]

                    binding.tvCareer.text = it.career[0]
                    binding.tvLove.text = it.love[0]
                    binding.tvFinance.text = it.finance[0]
                }
            }

        })
    }
}