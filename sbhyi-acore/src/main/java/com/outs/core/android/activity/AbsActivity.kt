package com.outs.core.android.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.outs.core.android.dialog.LoadingDialog
import com.outs.utils.android.inject
import com.outs.utils.android.injectIntent
import com.outs.utils.android.launch.ILaunchOwner
import com.outs.utils.android.launch.SimpleActivityLauncher

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/11/2 13:40
 * desc:
 */
abstract class AbsActivity : AppCompatActivity(), ILaunchOwner, View.OnClickListener {

    private val lock = Any()

    protected val context: Context = this
    protected val owner: LifecycleOwner = this

    protected val mLauncher = SimpleActivityLauncher(this)

//    protected val mLoading by lazy {
//        Dialog(context, R.style.dialog_style).apply {
//            setContentView(R.layout.dialog_loading)
//            setCancelable(false)
//            setCanceledOnTouchOutside(false)
//        }
//    }

    protected open val isSecure: Boolean
        get() = false

    protected open val isStatusBarDark: Boolean
        get() = false

    protected open val isStatusBarTranslate: Boolean
        get() = false

    protected open val isImmersiveMode: Boolean
        get() = false

    protected open val isFullScreen: Boolean
        get() = false

    protected open val isKeepScreenOn: Boolean
        get() = false

    protected open val screenBrightness: Float
        get() = -1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindow()
        injectIntent()
        initView()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.let(::inject)
    }

    protected abstract fun initView()

    open fun initWindow() {
        var statusBarFlag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isStatusBarDark) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        //禁止截屏
        if (isSecure) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        if (isImmersiveMode || isFullScreen) {
            statusBarFlag = statusBarFlag
                .or(View.SYSTEM_UI_FLAG_IMMERSIVE)
                .or(View.SYSTEM_UI_FLAG_FULLSCREEN)
                .or(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
        if (isFullScreen) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        if (isStatusBarTranslate) {
            window.statusBarColor = Color.TRANSPARENT
        }
        if (isKeepScreenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        window.attributes.screenBrightness = screenBrightness
        window.decorView.systemUiVisibility = statusBarFlag
    }

    override fun onClick(v: View) {
    }

    override fun getActivity(): AppCompatActivity = this

    override fun getLauncher(): SimpleActivityLauncher = mLauncher

    protected open fun showLoading() {
//        mLoading.show()
        synchronized(lock) {
            val loading = supportFragmentManager.findFragmentByTag(LoadingDialog.TAG)
            supportFragmentManager.beginTransaction()
                .add(loading ?: LoadingDialog(), LoadingDialog.TAG).commitAllowingStateLoss()
        }
    }

    protected open fun hideLoading() {
//        mLoading.hide()
        synchronized(lock) {
            val loading = supportFragmentManager.findFragmentByTag(LoadingDialog.TAG)
            loading ?: return
            supportFragmentManager.beginTransaction().remove(loading).commitAllowingStateLoss()
        }
    }

}