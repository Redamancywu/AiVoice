package com.hi.weather
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.hi.base.adapter.CommonAdapter
import com.hi.base.adapter.CommonViewHolder
import com.hi.base.base.BaseActivity
import com.hi.base.helper.ARouterHelper
import com.hi.base.utils.HiLog
import com.hi.base.utils.SpUtils
import com.hi.network.HttpManager
import com.hi.network.bean.Future
import com.hi.network.bean.WeatherData
import com.hi.weather.data.FutureWeather


import com.hi.weather.databinding.ActivityWeatherBinding
import com.hi.weather.databinding.ItemWeatherCardBinding
import com.hi.weather.tools.WeatherIconTools
import com.hi.weather.ui.CitySelectActivity
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Route(path = ARouterHelper.PATH_WEATHER)
class WeatherActivity : BaseActivity<ActivityWeatherBinding>() {
    private val mList = ArrayList<FutureWeather>()
    private lateinit var mDayAdapter: CommonAdapter<FutureWeather>
    private lateinit var weatherCardBinding: ItemWeatherCardBinding
    //当前城市
    private var currentCity = "深圳"

    //跳转
    private val codeSelect = 100
    override fun getTitleText(): String {
        return getString(com.hi.base.R.string.app_title_weather)
    }

    override fun isShowBack(): Boolean {
        return false
    }

    override fun initView() {
      weatherCardBinding = ItemWeatherCardBinding.inflate(LayoutInflater.from(this))
        loadWeather()
        intent.run {
            val city = getStringExtra("city")
            if (!TextUtils.isEmpty(city)) {
                loadWeather(city)
            } else {
                //非语音进入,先查询本地
                val localCity = SpUtils.getString("city")
                if (!TextUtils.isEmpty(localCity)) {
                    loadWeather(localCity!!)
                } else {
                    startCitySelectActivity()
                }
            }
        }
    }
    private fun loadWeather(city: String?) {
        if (city != null) {
            currentCity = city
        }
        SpUtils.putString("city", currentCity)
        initChart()
        loadWeather()
        //设置未来七天数据
        loadFutureWeather()
    }

    private fun loadFutureWeather() {

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        mDayAdapter=CommonAdapter(mList,object :CommonAdapter.OnBindDataListener<FutureWeather>{
            override fun onBindViewHolder(
                model: FutureWeather,
                holder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                holder.setText(R.id.day_of_week, model.date)
                holder.setImage(R.id.weather_icon,WeatherIconTools.getIcon(model.wid.day))
                holder.setText(R.id.high_temperature, "高温: ${model.temperature}")
                // 可选：如果需要显示其他天气信息，比如风向
                // holder.getView<TextView>(R.id.tv_direct).text = model.direct
            }
            override fun getLayoutId(type: Int): Int {
                return R.layout.item_weather_card
            }
        })
        binding.recyclerView.adapter=mDayAdapter

        lifecycleScope.launch {
            HttpManager.run {
                queryWeekWeather(currentCity,object :Callback<Future>{
                    override fun onResponse(call: Call<Future>, reponse: Response<Future>) {
                      if (reponse.isSuccessful){
                          reponse.body().let {
                            it.run {
                                weatherCardBinding.dayOfWeek.text=it?.weather
                                weatherCardBinding.highTemperature.text=it?.temperature
                            }
                          }
                      }
                    }

                    override fun onFailure(call: Call<Future>, t: Throwable) {

                    }

                })
            }
        }

    }
    //加载城市数据
    private fun loadWeather() {
        //设置
        supportActionBar?.title = currentCity
        HttpManager.run {
            queryWeather(currentCity, object : Callback<WeatherData> {
                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    HiLog.i("onFailure:$t")
                    Toast.makeText(
                        this@WeatherActivity,
                        getString(com.hi.base.R.string.text_load_fail),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    HiLog.i("onResponse")
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.error_code == 10012) {
                                //超过每日可允许的次数了
                                return
                            }
                            //填充数据
                            it.result.realtime.apply {
                                //设置天气 阴
                                binding.mInfo.text = info
                                //设置图片
                                binding.mIvWid.setImageResource(WeatherIconTools.getIcon(wid))
                                //设置温度
                                binding.mTemperature.text =
                                    String.format(
                                        "%s%s",
                                        temperature,
                                        getString(com.hi.base.R.string.app_weather_t)
                                    )
                                //设置湿度
                                binding.mHumidity.text =
                                    String.format(
                                        "%s\t%s",
                                        getString(com.hi.base.R.string.app_weather_humidity),
                                        humidity
                                    )
                                //设置风向
                                binding.mDirect.text =
                                    String.format(
                                        "%s\t%s",
                                        getString(com.hi.base.R.string.app_weather_direct),
                                        direct
                                    )
                                //设置风力
                                binding.mPower.text =
                                    String.format(
                                        "%s\t%s",
                                        getString(com.hi.base.R.string.app_weather_power),
                                        power
                                    )
                                //设置空气质量
                                binding.mAqi.text = String.format(
                                    "%s\t%s",
                                    getString(com.hi.base.R.string.app_weather_aqi),
                                    aqi
                                )
                            }

                            val data = ArrayList<Entry>()
                            //绘制图表
                            it.result.future.forEachIndexed { index, future ->
                                val temp = future.temperature.substring(0, 2)
                                data.add(Entry((index + 1).toFloat(), temp.toFloat()))
                            }
                              setLineChartData(data)
                        }
                    }
                }

            })
        }
    }

    override fun getViewBinding(): ActivityWeatherBinding {
        return ActivityWeatherBinding.inflate(layoutInflater)
    }

    //跳转
    private fun startCitySelectActivity() {
        val intent = Intent(this, CitySelectActivity::class.java)
        startActivityForResult(intent, codeSelect)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == codeSelect) {
                data?.let {
                    val city = it.getStringExtra("city")
                    if (!TextUtils.isEmpty(city)) {
                        loadWeather(city!!)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.city_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_setting) {
            //跳转到城市选择中去
            startCitySelectActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    //初始化图表
    private fun initChart() {

        //=============================基本配置=============================

        //后台绘制
        binding.mLineChart.setDrawGridBackground(true)
        //开启描述文本
        binding.mLineChart.description.isEnabled = true
        binding.mLineChart.description.text = getString(com.hi.weather.R.string.text_ui_tips)
        //触摸手势
        binding.mLineChart.setTouchEnabled(true)
        //支持缩放
        binding.mLineChart.setScaleEnabled(true)
        //拖拽
        binding.mLineChart.isDragEnabled = true
        //扩展缩放
        binding.mLineChart.setPinchZoom(true)

        //=============================轴配置=============================

        //平均线
        val xLimitLine = LimitLine(10f, "")
        xLimitLine.lineWidth = 4f
        xLimitLine.enableDashedLine(10f, 10f, 0f)
        xLimitLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
        xLimitLine.textSize = 10f

        val xAxis = binding.mLineChart.xAxis
        xAxis.enableAxisLineDashedLine(10f, 10f, 0f)
        //最大值
        xAxis.mAxisMaximum = 5f
        //最小值
        xAxis.axisMinimum = 1f

        val axisLeft = binding.mLineChart.axisLeft
        axisLeft.enableAxisLineDashedLine(10f, 10f, 0f)
        //最大值
        axisLeft.mAxisMaximum = 40f
        //最小值
        axisLeft.axisMinimum = 20f

        //禁止右边的Y轴
        binding.mLineChart.axisRight.isEnabled = false
    }

    //设置图标数据
    private fun setLineChartData(values: java.util.ArrayList<Entry>) {
        if (binding.mLineChart.data != null && binding.mLineChart.data.dataSetCount > 0) {
            //获取数据容器
            val set = binding.mLineChart.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            binding.mLineChart.data.notifyDataChanged()
            binding.mLineChart.notifyDataSetChanged()
            //如果存在数据才这样去处理
        } else {
            val set = LineDataSet(values, getString(R.string.text_chart_tips_text, currentCity))

            //=============================UI配置=============================
            set.enableDashedLine(10f, 10f, 0f)
            set.setCircleColor(Color.BLACK)
            set.lineWidth = 1f
            set.circleRadius = 3f
            set.setDrawCircleHole(false)
            set.valueTextSize = 10f
            set.formLineWidth = 1f
            set.setDrawFilled(true)
            set.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set.formSize = 15f

            set.fillColor = Color.YELLOW

            //设置数据
            val dataSet = ArrayList<LineDataSet>()
            dataSet.add(set)
            val data = LineData(dataSet as List<ILineDataSet>?)
            binding.mLineChart.data = data
        }
        //=============================UI配置=============================

        //X轴动画
        binding.mLineChart.animateX(2000)
        //刷新
        binding.mLineChart.invalidate()
        //页眉
        val legend = binding.mLineChart.legend
        legend.form = Legend.LegendForm.LINE
    }

}