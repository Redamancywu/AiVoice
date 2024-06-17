package com.hi.base.helper

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager

object WindowHelper {
    //声明一个上下文变量
    private lateinit var mContext: Context

    //声明一个窗口管理器变量
    private lateinit var wm: WindowManager

    //声明一个布局参数变量
    private lateinit var lp: WindowManager.LayoutParams

    fun initHelper(mContext: Context) {
        this.mContext = mContext
        wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        lp = createLayoutParams()
    }

    /**
     * 创建布局属性
     */
    // 创建一个WindowManager.LayoutParams对象
    fun createLayoutParams(): WindowManager.LayoutParams {
        val lp = WindowManager.LayoutParams()
        // 将lp的值应用到lp
        lp.apply {
            // 设置lp的宽度为WindowManager.LayoutParams.MATCH_PARENT
            this.width = WindowManager.LayoutParams.MATCH_PARENT
            // 设置lp的高度为WindowManager.LayoutParams.MATCH_PARENT
            this.height = WindowManager.LayoutParams.MATCH_PARENT
            // 设置lp的 gravity 为Gravity.CENTER
            this.gravity = Gravity.CENTER
            // 设置lp的format 为PixelFormat.TRANSLUCENT
            this.format = PixelFormat.TRANSLUCENT
            // 设置lp的flags 为WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            // 设置lp的type 为
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE
        }
        // 返回lp
        return lp
    }

    //获取视图控件
    fun getView(layoutId: Int): View {
        return View.inflate(mContext, layoutId, null)
    }

    //显示窗口
    fun show(view: View) {
        if (view.parent == null) {
            wm.addView(view, lp)
        }
    }

    //显示窗口，但是窗口属性自定义
    fun show(view: View, lp: WindowManager.LayoutParams) {
        if (view.parent == null) {
            wm.addView(view, lp)
        }
    }

    //隐藏视图
    fun hide(view: View) {
        if (view.parent != null) {
            wm.removeView(view)
        }
    }

    //更新视图
    fun update(view: View, lp: WindowManager.LayoutParams) {
        wm.updateViewLayout(view, lp)
    }

}