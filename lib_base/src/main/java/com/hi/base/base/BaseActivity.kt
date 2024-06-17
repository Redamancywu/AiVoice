package com.hi.base.base

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.service.credentials.Action
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hi.base.utils.HiLog
import com.hi.network.HttpManager
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private lateinit var mContext: Context
    protected val codeWindowRequest = 1000

    //  获取标题
    abstract fun getTitleText(): String

    //是否显示返回建
    abstract fun isShowBack(): Boolean

    //初始化布局
    abstract fun initView()

    private lateinit var mViewBinding: VB
    protected val binding get() = mViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mViewBinding = getViewBinding()
        setContentView(mViewBinding.root)
        supportActionBar.let {
            it?.title = getTitleText()
            it?.setDisplayHomeAsUpEnabled(isShowBack())
            it?.elevation = 0f  //去掉阴影
        }
        initView()
    }

    protected abstract fun getViewBinding(): VB

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    //检查窗口权限
    protected fun checkWindowPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Settings.canDrawOverlays(this)
        }
        return true
    }

    //申请权限
    protected fun requestWindowPermission(packageName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startActivityForResult(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                ), codeWindowRequest
            )
        }
    }

    //检查权限
    protected fun checkPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    //检查多个权限
    protected fun checkPermission(permission: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            permission.forEach {
                if (checkSelfPermission(it) == PackageManager.PERMISSION_DENIED) {
                    return false
                }
            }
        }
        return true
    }

    // 封装权限请求方法
   protected fun RequestPermissions(vararg permissions: String) {
        XXPermissions.with(this)
            .permission(*permissions) // 使用 vararg 参数展开数组
            .request(object : OnPermissionCallback {
                override fun onGranted(grantedList: MutableList<String>, isAllGranted: Boolean) {
                    if (!isAllGranted) {
                        Toast.makeText(mContext, "部分权限未被授予", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mContext, "所有权限已授予", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onDenied(deniedList: MutableList<String>, quick: Boolean) {
                    if (quick) {
                        Toast.makeText(
                            mContext,
                            "被永久拒绝授权，请手动开启权限",
                            Toast.LENGTH_SHORT
                        ).show()
                        // 如果是被永久拒绝就跳转到应用权限系统设置页面
                    } else {
                        Toast.makeText(mContext, "权限请求被拒绝", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
    //被拒绝后去申请权限
    fun gotoAppSettingPermissions(vararg permissions: String) {
        XXPermissions.startPermissionActivity(this,permissions)

    }

    /**
     *
     * // 申请单个权限
     * requestPermissions(Permission.RECORD_AUDIO)
     *
     * // 申请多个权限
     * requestPermissions(Permission.RECORD_AUDIO, Permission.CAMERA)
     *
     * // 申请权限组
     * val calendarPermissions = Permission.Group.CALENDAR // 假设这是返回权限组数组的属性
     * requestPermissions(*calendarPermissions)
     *
     * // 同时申请单个权限和权限组
     * requestPermissions(Permission.RECORD_AUDIO, *calendarPermissions)
     *
     */

}