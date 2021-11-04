package com.outs.utils.android

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.OperationCanceledException
import androidx.annotation.RequiresPermission
import androidx.core.location.LocationManagerCompat
import androidx.core.os.CancellationSignal
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/15 16:25
 * desc:
 */
//
//import android.Manifest
//import android.content.Context
//import androidx.annotation.RequiresPermission
//import com.amap.api.location.AMapLocation
//import com.amap.api.location.AMapLocationClient
//import com.amap.api.location.AMapLocationClientOption
//
//@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//fun startLocation(context: Context, onGet: (AMapLocation) -> Unit) {
//    //初始化定位
//    val locationClient = AMapLocationClient(context.applicationContext)
//    locationClient.setLocationListener { aMapLocation ->
//        if (null != aMapLocation) {
//            onGet(aMapLocation)
//            locationClient.stopLocation()
//        }
//    }
//    //初始化AMapLocationClientOption对象
//    locationClient.setLocationOption(AMapLocationClientOption().apply {
//        locationPurpose = AMapLocationClientOption.AMapLocationPurpose.SignIn
//        locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
//        //获取一次定位结果： 该方法默认为false
//        isOnceLocation = true
//        //设置是否返回地址信息（默认返回地址信息）
//        isNeedAddress = true
//        httpTimeOut = 20000
//        //关闭缓存机制
//        isLocationCacheEnable = false
//        //获取最近3s内精度最高的一次定位结果：
//        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        isOnceLocationLatest = true
//    })
//    //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//    locationClient.stopLocation()
//    locationClient.startLocation()
//}

private val locationExecutor = Executors.newSingleThreadExecutor()

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun getCurrentLocation(context: Context = appInstance, onGet: (Location) -> Unit) {
    val lock = Any()
    val cancelSignature = CancellationSignal()
    fun onLocation(location: Location?) {
        if (null != location) {
            synchronized(lock) {
                "location: $location".d()
                if (!cancelSignature.isCanceled) {
                    cancelSignature.cancel()
                    onGet(location)
                }
            }
        } else {
            "location: null".d()
        }
    }

    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    locationManager.allProviders.forEach { provider ->
        try {
            LocationManagerCompat.getCurrentLocation(
                locationManager,
                provider,
                cancelSignature,
                locationExecutor,
                ::onLocation
            )
        } catch (e: OperationCanceledException) {

        }
    }

}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
suspend fun getCurrentLocation(context: Context = appInstance): Location =
    suspendCoroutine { continuation -> getCurrentLocation(context) { continuation.resume(it) } }
