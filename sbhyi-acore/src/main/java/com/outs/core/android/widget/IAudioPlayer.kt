package com.outs.core.android.widget

import androidx.lifecycle.LiveData

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/6 9:02
 * desc:
 */
interface IAudioPlayer {
    fun setUrl(url: String, autoPlay: Boolean = true)

    fun isPlaying(): Boolean

    fun getPosition(): Int

    fun getDuration(): Int

    fun seek(position: Int)

    fun resume()

    fun pause()

    fun stop()

    fun release()

    fun toggle() {
        if (isPlaying()) pause() else resume()
    }

    fun getLivePosition(): LiveData<Int>

    fun getLiveDuration(): LiveData<Int>

    fun getLivePlayState(): LiveData<Boolean>
}
