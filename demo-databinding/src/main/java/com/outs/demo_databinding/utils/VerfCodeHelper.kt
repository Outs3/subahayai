package com.outs.demo_databinding.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.outs.core.android.vm.BaseViewModel
import com.outs.utils.android.mapTo
import com.outs.utils.kotlin.finish
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/1 13:10
 * desc:  验证码View的帮助类，可以提供验证码倒计时、验证码View可用性功能
 */
class VerfCodeHelper(
    private val viewModel: BaseViewModel,
    private val text: String = "获取验证码",     //显示文本
    private val timeSecond: Int = 60            //倒计时
) {

    //发送验证码是否可用
    val enableSMS by lazy { MutableLiveData<Boolean>(true) }

    //按钮获取验证码的文字
    val sendVerfText by lazy { MutableLiveData<String>(text) }

    //发送验证码倒计时（Long）
    private val countDown by lazy { MutableLiveData<Long>() }

    //发送验证码倒计时文本
    private val countDownText by lazy {
        countDown.mapTo {
            it?.div(1000)?.toInt()?.let { "${it}s" }
        }
    }

    //倒计时Job
    private var timeOutJob: Job? = null

    //倒计时开始时间
    private var startTime: Long = -1

    //倒计时结束时间
    private var targetTime: Long = -1

    fun observe(owner: LifecycleOwner) {
        enableSMS.observe(owner) {
            if (it) {
                sendVerfText.value = text
            }
        }
        countDownText.observe(owner) {
            sendVerfText.value = it
        }
    }

    fun sendVerf(withProgress: Boolean = true, action: suspend () -> Unit) {
        viewModel.launchOnUI(withProgress) {
            if (true == enableSMS.value) {
                action()
                startTimer()
            }
        }
    }

    private suspend fun startTimer() {
        timeOutJob?.finish()
        timeOutJob = viewModel.launchOnUI {
            enableSMS.value = false
            val base = timeSecond * 1000
            startTime = System.currentTimeMillis()
            targetTime = startTime + base
            do {
                val currentTime = System.currentTimeMillis()
                countDown.value = targetTime - currentTime
                delay(1000)
            } while (currentTime < targetTime)
            enableSMS.value = true
        }
    }

}