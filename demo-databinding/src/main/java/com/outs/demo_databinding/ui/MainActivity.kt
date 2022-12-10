package com.outs.demo_databinding.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import androidx.core.os.bundleOf
import com.outs.core.android.databinding.activity.BaseActivity
import com.outs.core.android.takePhoto
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.ActivityMainBinding
import com.outs.utils.android.*
import com.outs.utils.kotlin.method
import com.outs.utils.kotlin.typeOfOrNull
import kotlinx.coroutines.delay
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/11/4 14:39
 * desc:
 */
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val getViewModel: MainViewModel
        get() = viewModel()

    override val getLayoutId: Int
        get() = R.layout.activity_main

    override fun onDebug() {
        super.onDebug()
        addDebugOpt(
            "3s Loading" to {
                mViewModel.launchOnUI(true) {
                    delay(3000)
                }
            },
            "readImgUri" to { readImgUri() },
            "readContacts" to { mViewModel.readContacts(context) },
            "readContactExtra" to { mViewModel.readContactExtra(context) },
            "readBigFileByUri" to { readBigFileByUri() },
            "inject(5, null)" to {
                startActivity<InjectActivity>(
                    bundleOf(
                        "iInt" to 5,
                        "iInteger" to null,
                    )
                )
            },
            "inject(9, 4)" to {
                startActivity<InjectActivity>(
                    bundleOf(
                        "iInt" to 9,
                        "iInteger" to 4,
                    )
                )
            },
            "sim card number" to {
                loadSimCardNumbers()
            },
            "send sms" to {
                sendSMS()
            },
            "sendAlarm5s" to {
                sendAlarm5s()
            },
            "sendAlarmRepeat" to {
                sendAlarmRepeat()
            }
        )
    }

    @SuppressLint("MissingPermission")
    private fun readImgUri() {
        mViewModel.launchOnUI {
            permissionOrThrow(Manifest.permission.CAMERA)
            //选择图片
            val uri = suspendCoroutine<Uri> { takePhoto(it::resume) }
            //读取uri内容
            uri.also(mBinding.imageCover1::setImageURI)
                .readImageAsFile(context)
                ?.path
                ?.let(BitmapFactory::decodeFile)
                ?.also(mBinding.imageCover2::setImageBitmap)
        }
    }

    private fun readBigFileByUri() {
        mViewModel.launchOnUI {
            permissionOrThrow(Manifest.permission.CAMERA)
            //选择图片
            val uri = suspendCoroutine<Uri> { pickVideo(it::resume) }
            //读取uri内容
            uri.also(mBinding.imageCover1::setImageURI).asFile(context)
        }
    }

    @SuppressLint("MissingPermission")
    private fun loadSimCardNumbers() {
        mViewModel.launchOnUI {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE)
                ?.typeOfOrNull<TelephonyManager>()
                ?: throw RuntimeException("System service <TELEPHONY_SERVICE> not found!")
            val subscriptionManager =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE)
                        ?.typeOfOrNull<SubscriptionManager>()
                        ?: throw RuntimeException("System service <TELEPHONY_SUBSCRIPTION_SERVICE> not found!")
                } else {
                    throw RuntimeException("System service <TELEPHONY_SUBSCRIPTION_SERVICE> need sdk version LOLLIPOP_MR1(${Build.VERSION_CODES.LOLLIPOP_MR1}). current version : ${Build.VERSION.SDK_INT}!")
                }
            val methodGetLine1Number =
                telephonyManager.method("getLine1Number", true, Int::class.java)
                    ?: throw RuntimeException("Method <getLine1Number> not found!")
            val getLine1Number: (Int) -> String? =
                { id -> methodGetLine1Number.invoke(telephonyManager, id)?.typeOfOrNull() }
            val dNumBySubId: (name: String, id: Int) -> Unit = { name: String, id: Int ->
                "$name: $id -> ${getLine1Number(id)}".d()
            }
            val dNumBySubIdPlus: (methodName: String) -> Unit = { methodName: String ->
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                    "$methodName -> Min sdk version: ${Build.VERSION_CODES.LOLLIPOP_MR1}, current sdk version: ${Build.VERSION.SDK_INT}".d()
                } else {
                    val member =
                        SubscriptionManager::class.members.firstOrNull { methodName == it.name }
                    val ret = member?.call()?.typeOfOrNull<Int>()
                    if (null == ret) {
                        "$methodName -> null (reflect by member: $member)".d()
                    } else {
                        dNumBySubId(methodName, ret)
                    }
                }
            }
            permissionOrThrow(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_PHONE_NUMBERS
            )
            "line1Number: ${telephonyManager.line1Number}".d()
            dNumBySubIdPlus("getDefaultSubscriptionId")
            dNumBySubIdPlus("getDefaultDataSubscriptionId")
            dNumBySubIdPlus("getDefaultSmsSubscriptionId")
            dNumBySubIdPlus("getActiveDataSubscriptionId")
            subscriptionManager.activeSubscriptionInfoList?.forEachIndexed { index, subscriptionInfo ->
                dNumBySubId(
                    "activeSubscriptionInfoList[$index].subscriptionId",
                    subscriptionInfo.subscriptionId
                )
            }
        }
    }

    private fun sendSMS() {
        fun sendBySMSApp(target: String, msg: String) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("smsto:")
                type = "vnd.android-dir/mms-sms"
                putExtra("address", target)
                putExtra("sms_body", msg)
            }
//            if (intent.resolveActivity(packageManager) != null) {
//                startActivity(intent)
//            }
            startActivity(intent)
        }

        fun sendBySmsManager(target: String, msg: String) {
            mViewModel.launchOnUI {
                permissionOrThrow(Manifest.permission.SEND_SMS)
                val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context.getSystemService(SmsManager::class.java)
                } else {
                    SmsManager.getDefault()
                }
                smsManager?.sendTextMessage(target, null, msg, null, null)
            }
        }

        val target = "15264794533"
        val msg = "这是一条编辑好的短信内容"
        sendBySMSApp(target, msg)
        //sendBySmsManager(target, msg)
    }

    private fun sendAlarm5s() {
        mViewModel.launchOnUI {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val intent = Intent(context, MainActivity::class.java)
            alarmManager?.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + 5000,
                PendingIntent.getActivity(context, 0, intent, 0)
            )
        }
    }

    private fun sendAlarmRepeat() {
        mViewModel.launchOnUI {
            val intent = Intent(context, MainActivity::class.java)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            alarmManager?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance().timeInMillis + TimeUnit.MINUTES.toMillis(1),
                AlarmManager.INTERVAL_DAY,
                PendingIntent.getActivity(context, 0, intent, 0)
            )
        }
    }

}