package com.hi.base.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class CommonAdapter<T> : RecyclerView.Adapter<CommonViewHolder> {
    //数据
    private  var mList: List<T>?=null
    //接口
    private  var mOnBindDataListener: OnBindDataListener<T>?=null
    //更多数据接口
    private  var mOnMoreBindDataListener: OnMoreBindDataListener<T>?=null
    // 构造函数，传入一个T类型的列表和一个OnBindDataListener<T>类型的参数
    constructor(list: List<T>, onBindDataListener: OnBindDataListener<T>){
        // 将传入的列表赋值给成员变量mList
        this.mList = list
        // 将传入的参数赋值给成员变量mOnBindDataListener
        this.mOnBindDataListener= onBindDataListener
    }
    // 构造函数，传入一个T类型的列表和一个OnMoreBindDataListener<T>类型的参数
    constructor(list: List<T>, onMoreBindDataListener: OnMoreBindDataListener<T>){
        // 将传入的列表赋值给成员变量mList
        this.mList= list
        // 将传入的参数赋值给成员变量mOnBindDataListener
        this.mOnBindDataListener= onMoreBindDataListener
        // 将传入的参数赋值给成员变量mOnMoreBindDataListener
        this.mOnMoreBindDataListener= onMoreBindDataListener
    }

    // 获取父视图和视图类型，返回视图的布局ID
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
       val layoutId=mOnBindDataListener?.getLayoutId(viewType)
        // 根据布局ID，返回一个视图holder
        return CommonViewHolder.getViewHolder(parent,layoutId!!)
    }

    // 获取列表的大小
    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }
    // 重写方法，根据位置获取条目类型
    override fun getItemViewType(position: Int): Int {
        if (mOnMoreBindDataListener!=null){
            return mOnMoreBindDataListener?.getItemType(position)!!
        }
        return 0
    }

    // 根据position，获取视图类型，调用onBindViewHolder方法
    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        mList?.get(position)?.let { mOnBindDataListener?.onBindViewHolder(it,holder,getItemViewType(position),position) }
    }

    // 定义一个接口，用于绑定数据
   interface OnBindDataListener<T> {
        // 绑定视图
        fun onBindViewHolder(model: T, holder: CommonViewHolder, type: Int, position: Int)
        // 获取布局ID
        fun getLayoutId(type: Int): Int
    }

    // 定义一个接口，用于绑定更多数据
    interface OnMoreBindDataListener<T> : OnBindDataListener<T> {
        // 获取条目类型
        fun getItemType(position: Int): Int
    }


}