package com.imooc.lib_base.helper.`fun`

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import com.hi.base.data.ContactData
import com.hi.base.utils.AppGlobals
import com.hi.base.utils.HiLog


/**
 * FileName: ContactHelper
 * Founder: LiuGuiLin
 * Profile: 联系人帮助类 / 权限
 */
object ContactHelper {

    //联系人列表
    val mContactList = ArrayList<ContactData>()

    //数据库地址
    private val PHONE_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

    //查询条件 名称 - 号码
    private const val NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
    private const val PHONE = ContactsContract.CommonDataKinds.Phone.NUMBER

    //初始化
    fun initHelper() {
        loadContact()
    }

    //加载通讯录
    @SuppressLint("Range")
    private fun loadContact() {
        val resolver = AppGlobals.getApplication().contentResolver
        val cursor = resolver.query(
            PHONE_URI, arrayOf(NAME, PHONE),
            null, null, null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val data = ContactData(
                    it.getString(it.getColumnIndex(NAME)),
                    it.getString(it.getColumnIndex(PHONE)).trim()
                )
                mContactList.add(data)
            }
        }
        HiLog.i("mContactList:$mContactList")
        cursor?.close()
    }

    /**
     * 拨打电话
     */
    @SuppressLint("MissingPermission")
    fun callPhone(phone: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phone")
        AppGlobals.getApplication().startActivity(intent)
    }
}