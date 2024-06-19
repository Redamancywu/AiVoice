package com.hi.base.data

import android.graphics.drawable.Drawable
/**
 * FileName: AppData
 * Founder: RedamancyWu
 * Profile: App 数据
 */
data class AppData(val packName: String,
                   val appName: String,
                   val appIcon: Drawable,
                   val firstRunName: String,
                   val isSystemApp: Boolean)
