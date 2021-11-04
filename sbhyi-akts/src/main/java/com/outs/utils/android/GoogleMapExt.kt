package com.outs.utils.android

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/7 11:09
 * desc:
 */
//fun Location.asLatLng(): LatLng = LatLng(latitude, longitude)
//
//fun LatLng.asCameraUpdate(zoom: Float = 14f): CameraUpdate =
//    CameraUpdateFactory.newLatLngZoom(this, zoom)
//
//fun Location.updateCamera(camera: GoogleMap) = camera.moveCamera(asLatLng())
//
//fun LatLng.updateCamera(camera: GoogleMap) = camera.moveCamera(this.asCameraUpdate())
//
//fun GoogleMap.moveCamera(latLng: LatLng) = moveCamera(latLng.asCameraUpdate())
//
//fun GoogleMap.moveCamera(location: Location) = moveCamera(location.asLatLng().asCameraUpdate())
//
//fun LatLng.asGoogleMapIntent(): Intent = getGoogleMapIntent(latitude, longitude)
//
//fun getGoogleMapIntent(lat: Double, lng: Double): Intent =
//    Intent(Intent.ACTION_VIEW, Uri.parse("google.streetview:cbll=$lat,$lng")).also {
//        it.setPackage("com.google.android.apps.maps")
//    }
//
//fun <T, R> Task<T>.into(target: Continuation<R>, mapper: (T?) -> R?) {
//    this.addOnFailureListener { e -> target.resumeWithException(e) }
//        .addOnSuccessListener { target.resumeOrException(mapper(it)) }
//}
