package com.hi.weather.ui

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hi.base.adapter.CommonAdapter
import com.hi.base.adapter.CommonViewHolder
import com.hi.base.base.BaseActivity
import com.hi.base.utils.AssetsUtils
import com.hi.weather.R
import com.hi.weather.data.CitySelectBean
import com.hi.weather.databinding.ActivityCitySelectBinding
import com.hi.weather.view.CitySelectView


/**
 * FileName: CitySelectActivity
 * Founder: LiuGuiLin
 * Profile: 城市选择
 */
class CitySelectActivity : BaseActivity<ActivityCitySelectBinding>() {

    /**
     * 实现城市选择列表的步骤
     * 0.加载assets数据 OK
     * 1.实现热门的头部 OK
     * 2.实现列表(多布局) OK
     * 3.自定义导航View
     * 4.双向的数据通信
     * 5.双向的联动
     */

    private val mList = ArrayList<CitySelectBean>()
    private lateinit var mCitySelectAdapter: CommonAdapter<CitySelectBean>

    private val mListTitle = ArrayList<String>()

    //标题
    private val mTypeTitle = 1000

    //热门城市
    private val mTypeHotCity = 1001

    //内容
    private val mTypeContent = 1002


    override fun getTitleText(): String {
        return "城市选择"
    }

    override fun isShowBack(): Boolean {
        return true
    }

    override fun initView() {

        parsingData()

        initListView()
    }

    override fun getViewBinding(): ActivityCitySelectBinding {
        return ActivityCitySelectBinding.inflate(layoutInflater)
    }

    private fun initListView() {
        binding.mCityListView.layoutManager = LinearLayoutManager(this)
        binding.mCityListView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        mCitySelectAdapter =
            CommonAdapter(mList, object : CommonAdapter.OnMoreBindDataListener<CitySelectBean> {
                override fun onBindViewHolder(
                    model: CitySelectBean,
                    viewHolder: CommonViewHolder,
                    type: Int,
                    position: Int
                ) {
                    when (model.type) {
                        mTypeTitle -> viewHolder.setText(com.hi.base.R.id.mTvCityTitle, model.title)
                        mTypeHotCity -> {
                            val mCityHotView =
                                viewHolder.getView(com.hi.base.R.id.mCityHotView) as RecyclerView
                            setHotCityView(mCityHotView)
                        }

                        mTypeContent -> {
                            viewHolder.setText(com.hi.base.R.id.mTvCityContent, model.content)
                            viewHolder.itemView.setOnClickListener {
                                finishResult(model.city)
                            }
                        }
                    }
                }

                override fun getLayoutId(type: Int): Int {
                    return when (type) {
                        mTypeTitle -> com.hi.base.R.layout.layout_city_title
                        mTypeHotCity -> com.hi.base.R.layout.layout_city_hot
                        mTypeContent -> com.hi.base.R.layout.layout_city_content
                        else -> com.hi.base.R.layout.layout_city_title
                    }
                }

                override fun getItemType(position: Int): Int {
                    return mList[position].type
                }

            })
        binding.mCityListView.adapter = mCitySelectAdapter

        //监听滚动
        binding.mCityListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //计算你滚动到的条目是哪一个城市，通知View去刷新选中状态
                val lm = recyclerView.layoutManager
                if (lm is LinearLayoutManager) {
                    // 0 - 12 12 24
                    val firstPosition = lm.findFirstCompletelyVisibleItemPosition()
                    val lastPosition = lm.findFirstCompletelyVisibleItemPosition()
                    //取中间值 -:得到每一页的数据12 / 2  = 6
                    val centerIndex = (lastPosition - firstPosition) / 2 + firstPosition
                    //得到省份
                    val province = mList[centerIndex].province
                    val itemIndex = mListTitle.indexOf(province)
                    if (itemIndex >= 0 && itemIndex < mListTitle.size) {
                       // binding.mCitySelectView.setSelectIndex(itemIndex)
                        binding.mCitySelectView.setCheckIndex(itemIndex)
                    }
                }
            }
        })
    }

    //设置热门城市
    private fun setHotCityView(mCityHotView: RecyclerView) {
        val cityList = resources.getStringArray(com.hi.base.R.array.HotCityArray)
        mCityHotView.layoutManager = GridLayoutManager(this, 3)
        mCityHotView.adapter = CommonAdapter<String>(cityList.toList(), object :
            CommonAdapter.OnBindDataListener<String> {
            override fun onBindViewHolder(
                model: String,
                viewHolder: CommonViewHolder,
                type: Int,
                position: Int
            ) {
                viewHolder.setText(R.id.mTvHotCity, model)

                viewHolder.itemView.setOnClickListener {
                    finishResult(model)
                }
            }

            override fun getLayoutId(type: Int): Int {
                return R.layout.layout_hot_city_item
            }
        })
    }

    //解析数据
    private fun parsingData() {
        val city = AssetsUtils.getCity()
        addTitle(getString(R.string.text_city_hot))
        addHot()
        //其他操作 标题 + 内容
        city.result.forEach {
            if (!mListTitle.contains(it.province)) {
                addTitle(it.province)
            }
            //添加内容
            addContent("${it.city}市${it.district}县", it.district, it.province)
        }

        //设置导航数据源
       binding. mCitySelectView.setCity(mListTitle)

        binding. mCitySelectView.setOnViewResultListener(object : CitySelectView.OnViewResultListener {

            override fun uiChange(uiShow: Boolean) {
                binding.mShowCity.visibility = if (uiShow) View.VISIBLE else View.GONE
            }

            override fun valueInput(text: String) {
                binding. mShowCity.text = text

                //计算值
                findTextIndex(text)
            }

        })
    }

    //根据城市寻找下标
    private fun findTextIndex(text: String) {
        if (mList.size > 0) {
            mList.forEachIndexed { index, citySelectBean ->
                if (text == citySelectBean.title) {
                    binding.mCityListView.scrollToPosition(index)
                    return@forEachIndexed
                }
            }
        }
    }

    //添加标题
    private fun addTitle(title: String) {
        val data = CitySelectBean(mTypeTitle, title, "", "", title)
        mList.add(data)
        mListTitle.add(title)
    }

    //添加内容
    private fun addContent(content: String, city: String, province: String) {
        val data = CitySelectBean(mTypeContent, "", content, city, province)
        mList.add(data)
    }

    //添加热门
    private fun addHot() {
        val data = CitySelectBean(mTypeHotCity, "", "", "", getString(R.string.text_city_hot))
        mList.add(data)
    }

    //结果回调
    private fun finishResult(city: String) {
        val intent = Intent()
        intent.putExtra("city", city)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}