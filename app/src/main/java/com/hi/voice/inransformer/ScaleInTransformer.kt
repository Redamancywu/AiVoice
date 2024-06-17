package com.hi.voice.inransformer

import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager


class ScaleInTransformer :ViewPager.PageTransformer{
    // 最小缩放比例
    private val MIN_SCALE = 0.70f
    // 最小透明度
    private val MIN_ALPHA = 0.5f
    // 重写方法，根据位置参数对页面进行缩放和透明度调整
    override fun transformPage(page: View, position: Float) {
        // 如果位置参数不在[-1,1]范围内，则缩放比例和透明度都设置为最小值
        if (position < -1 || position > 1) {
            page.alpha = MIN_ALPHA
            page.scaleX = MIN_SCALE
            page.scaleY = MIN_SCALE
        } else if (position <= 1) { // [-1,1]
            // 计算缩放比例，如果位置参数小于0，则根据位置参数调整缩放比例，否则根据位置参数调整缩放比例
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            if (position < 0) {
                val scaleX = 1 + 0.3f * position
                Log.d("google_lenve_fb", "transformPage: scaleX:$scaleX")
                page.scaleX = scaleX
                page.scaleY = scaleX
            } else {
                val scaleX = 1 - 0.3f * position
                page.scaleX = scaleX
                page.scaleY = scaleX
            }
            // 计算透明度，根据缩放比例和最小透明度计算透明度
            page.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)
        }
    }
}