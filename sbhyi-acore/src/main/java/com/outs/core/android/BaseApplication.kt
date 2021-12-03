package com.outs.core.android

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.blankj.utilcode.util.ToastUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.outs.utils.android.e
import com.outs.utils.android.isDebug
import com.outs.utils.android.toast
import com.outs.utils.kotlin.ILogger
import com.outs.utils.kotlin.setLogger

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/14 17:18
 * desc:
 */
open class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        initLog()
    }

    private fun initLog() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .methodCount(3)
            .methodOffset(5)
            .tag("LogByOu")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return isDebug
            }
        })
        setLogger(object : ILogger {
            override fun d(tag: String?, msg: String) {
                Logger.log(Logger.DEBUG, tag, msg, null)
            }

            override fun e(tag: String?, msg: String) {
                msg.toast()
                Logger.log(Logger.ERROR, tag, msg, null)
            }

        })
        if (isDebug) {
            Thread.setDefaultUncaughtExceptionHandler { thread: Thread?, throwable: Throwable? ->
                throwable?.e()
            }
        }
        ToastUtils.getDefaultMaker().setMode(ToastUtils.MODE.DARK)
    }
}