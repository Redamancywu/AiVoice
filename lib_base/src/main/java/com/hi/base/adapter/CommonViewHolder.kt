package com.hi.base.adapter

import android.content.IntentFilter
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * 通用的ViewHolder
 */
class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //view复用
    private var mViews: SparseArray<View?> = SparseArray()

    companion object {
        // 获取ViewHolder
        fun getViewHolder(parent: ViewGroup, layoutId: Int): CommonViewHolder {
            // 从父视图的上下文中inflate布局
            val itemView: View =
                LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            // 返回ViewHolder
            return CommonViewHolder(itemView)
        }

    }

    /**
     * 根据viewId获取View
     * @param viewId view的id
     * @return View
     */
    fun getView(viewId: Int): View {
        // 从mViews中获取view
        var view = mViews[viewId]
        // 如果view为空，则从itemView中查找view
        if (view == null) {
            view = itemView.findViewById(viewId)
            // 将view添加到mViews中
            mViews.put(viewId, view)
        }
        // 返回view
        return view!!
    }

    //设置文本内容
   fun setText(viewId: Int,text:String?){
        //根据viewId获取对应的视图
        (getView(viewId) as TextView).text=text
    }
    //设置图片
    fun setImage(viewId: Int,resId:Int?){
        //根据viewId获取对应的视图
        (getView(viewId) as ImageView).setImageResource(resId!!)
    }

    fun setOnClickListener(viewId: Int,listener:View.OnClickListener){
        getView(viewId).setOnClickListener(listener)
    }

}