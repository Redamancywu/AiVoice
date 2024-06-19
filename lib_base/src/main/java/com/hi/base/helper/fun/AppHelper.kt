import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.hi.base.R
import com.hi.base.base.BaseApp
import com.hi.base.data.AppData
import com.hi.base.utils.AppGlobals
import com.hi.base.utils.HiLog

/**
 * FileName: AppHelper
 * Founder: RedamancyWU
 * Profile: 应用帮助类
 */
object AppHelper {
    //上下文
    private var pm: PackageManager? = null
    private val mAllAppList = mutableListOf<AppData>()
    private lateinit var mAllMarkArray: Array<String>
    val mAllViewList = mutableListOf<View>()

    fun initHelper() {
        // 获取PackageManager实例
        pm = AppGlobals.getApplication().packageManager
        loadAllApp(AppGlobals.getApplication())
    }
    //加载所有APP
    @SuppressLint("StaticFieldLeak")
    private fun loadAllApp(context: Context) {
        // 创建一个Intent，用于获取所有应用程序的详细信息
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        // 使用pm查询Intent，获取所有应用程序的详细信息
        val appInfo = pm?.queryIntentActivities(intent, 0) ?: emptyList()

        // 遍历所有应用程序的详细信息
        appInfo.forEachIndexed { _, resolveInfo ->
            // 创建一个AppData对象，用于存储应用程序的详细信息
            val appData = AppData(
                resolveInfo.activityInfo.packageName,
                resolveInfo.loadLabel(pm) as String,
                resolveInfo.loadIcon(pm),
                resolveInfo.activityInfo.name,
                (resolveInfo.activityInfo.flags and ApplicationInfo.FLAG_SYSTEM) > 0
            )
            // 将AppData对象添加到mAllAppList列表中
            mAllAppList.add(appData)
        }

        HiLog.e("mAllAppList:$mAllAppList")
        //加载商店包名
        mAllMarkArray = context.resources.getStringArray(R.array.AppMarketArray)

        // 初始化PageView
        initPageView()
    }
    //初始化PageView
    private fun initPageView() {
        // 遍历所有Apk对象的数量
        for (i in 0 until getPageSize()) {
            // -> FrameLayout
            val rootView = View.inflate(AppGlobals.getApplication(), R.layout.layout_app_manager_item, null) as ViewGroup
            // -> 第一层 线性布局
            for (j in 0 until rootView.childCount) {
                // -> 第二层 六个 线性布局
                val childX = rootView.getChildAt(j) as ViewGroup
                // -> 第三层  四个线性布局
                for (k in 0 until childX.childCount) {
                    // -> 第四层  两个View ImageView TextView
                    val child = childX.getChildAt(k) as ViewGroup
                    val iv = child.getChildAt(0) as ImageView
                    val tv = child.getChildAt(1) as TextView
                    // 计算你当前的下标
                    val index = i * 24 + j * 4 + k
                    if (index < mAllAppList.size) {
                        // 获取数据
                        val data = mAllAppList[index]
                        tv.text = data.appName
                        iv.setImageDrawable(data.appIcon)
                        // 点击事件
                        child.setOnClickListener {
                            intentApp( data.packName)
                        }
                    }
                }
            }
            mAllViewList.add(rootView)
        }
    }
    //获取页面数量
    fun getPageSize(): Int {
        if (pm == null) {
            initHelper()
        }
        return mAllAppList.size / 24 + 1
    }
    //获取非系统应用
    fun getNotSystemApp(): List<AppData> {
        if (pm == null) {
            initHelper()
        }
        return mAllAppList.filter { !it.isSystemApp }
    }
    //启动App
    fun launcherApp(appName: String): Boolean {
        // 如果pm为空，则初始化帮助
        if (pm == null) {
            initHelper()
        }
        // 返回mAllAppList中是否有名为appName的应用
        return mAllAppList.any { it.appName == appName }.also {
            // 如果存在，则调用intentApp方法
            if (it) {
                intentApp( mAllAppList.first { it.appName == appName }.packName)
            }
        }
    }
    //卸载App
    fun unInstallApp(appName: String): Boolean {
       // 如果pm为空，则初始化帮助
     if (pm == null) {
            initHelper()
        }
        // 判断mAllAppList中是否有名为appName的应用
        return mAllAppList.any { it.appName == appName }.also {
            // 如果有，则卸载该应用
            if (it) {
                intentUnInstallApp( mAllAppList.first { it.appName == appName }.packName)
            }
        }
    }
    //跳转应用市场
    fun launcherAppStore( appName: String): Boolean {
        // 如果pm为空，则初始化帮助
      if (pm == null) {
            initHelper()
        }
        // 返回mAllAppList中是否存在packName和appName相等的元素
        return mAllAppList.any { mAllMarkArray.contains(it.packName) && it.appName == appName }.also {
            // 如果存在，则执行以下操作
            if (it) {
                // 获取appName对应的appData
                val appData = mAllAppList.first { it.appName == appName }
                // 遍历mAllAppList
                mAllAppList.forEach { marketData ->
                    // 如果mAllMarkArray包含packName，则执行intentAppStore
                    if (mAllMarkArray.contains(marketData.packName)) {
                        intentAppStore(AppGlobals.getApplication(), appData.packName, marketData.packName)
                        return@also
                    }
                }
            }
        }
    }
    //启动App
    private fun intentApp( packageName: String) {
        val intent = pm?.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        AppGlobals.getApplication().startActivity(intent)
    }
    //启动卸载App
    private fun intentUnInstallApp( packageName: String) {
        // 解析uri
      val uri = Uri.parse("package:$packageName")
        // 创建一个Intent对象，并设置其Action为Intent.ACTION_DELETE
        val intent = Intent(Intent.ACTION_DELETE).apply {
            // 设置Intent的数据为解析后的uri
            data = uri
            // 设置Intent的flags，以在新任务中启动Activity
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        // 启动Activity
        AppGlobals.getApplication().startActivity(intent)
    }
    //跳转应用商店
    private fun intentAppStore(context: Context, packageName: String, markPackageName: String) {
        // 解析市场链接
        val uri = Uri.parse("market://details?id=$packageName")
        // 创建一个Intent对象，用于打开市场
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            // 设置包名
            setPackage(markPackageName)
            // 设置新任务标志
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        // 启动市场
        context.startActivity(intent)
    }
}