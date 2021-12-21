package com.outs.core.android.widget

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.LogUtils
import com.outs.utils.android.doOnDestroy
import com.outs.utils.android.e
import com.outs.utils.kotlin.finish
import java.util.*
import java.util.concurrent.*

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/12 17:20
 * desc:
 */
open class AudioPlayer(lifecycle: Lifecycle? = null) : IAudioPlayer {

    private val useAsync: Boolean = true
    private val progressExecutor: ScheduledExecutorService by lazy { Executors.newSingleThreadScheduledExecutor() }
    private val mediaExecutor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    private var playFuture: ScheduledFuture<*>? = null

    private var mediaPlayer: MediaPlayer? = null
    private val livePosition = MutableLiveData(0)
    private val liveDuration = MutableLiveData(0)
    private val livePlayState = MutableLiveData(false)
    private val statusStack = object : Stack<String>() {
        init {
            push("Init")
        }

        override fun push(item: String?): String {
            LogUtils.d("Player", item)
            return super.push(item)
        }
    }

    init {
        lifecycle?.let(::bindToLifecycle)
    }

    fun bindToLifecycle(lifecycle: Lifecycle) {
        lifecycle.doOnDestroy(this::release)
    }

    override fun setUrl(url: String, autoPlay: Boolean) {
        setDataSource(doOnSet = { it.setDataSource(url) })
    }

    fun setUri(
        context: Context,
        uri: Uri,
        autoPlay: Boolean = true,
        seek: Int = -1,
        doOnStart: () -> Unit = {}
    ) {
        setDataSource(
            autoPlay,
            seek,
            doOnSet = { it.setDataSource(context, uri) },
            doOnStart = doOnStart
        )
    }

    fun setDataSource(
        autoPlay: Boolean = true,
        seek: Int = -1,
        doOnSet: (MediaPlayer) -> Unit,
        doOnStart: () -> Unit = {}
    ) {
        launchOnMedia {
            togglePlayer().apply {
                doOnSet(this)
                if (useAsync) {
                    setOnPreparedListener {
                        launchOnMedia {
                            statusStack.push("prepared")
                            liveDuration.postValue(duration)
                            if (-1 != seek) {
                                seekTo(seek)
                            }
                            if (autoPlay) {
                                resume()
                            }
                            doOnStart()
                        }
                    }
                    statusStack.push("prepareAsync")
                    prepareAsync()
                } else {
                    statusStack.push("prepare")
                    prepare()
                    statusStack.push("prepared")
                    liveDuration.value = duration
                    if (-1 != seek) {
                        seekTo(seek)
                    }
                    if (autoPlay) {
                        resume()
                    }
                    doOnStart()
                }
            }
            playFuture = progressExecutor.scheduleWithFixedDelay({
                val currentPosition = mediaPlayer!!.currentPosition
                livePosition.postValue(currentPosition)
            }, 100, 100, TimeUnit.MILLISECONDS)
        }
    }

    override fun getLivePosition(): LiveData<Int> = livePosition

    override fun getLiveDuration(): LiveData<Int> = liveDuration

    override fun getLivePlayState(): LiveData<Boolean> = livePlayState

    override fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    override fun getPosition(): Int = mediaPlayer?.currentPosition ?: 0

    override fun getDuration(): Int = mediaPlayer?.duration ?: 0

    override fun seek(position: Int) {
        launchOnMedia {
            mediaPlayer?.seekTo(position)
        }
    }

    override fun resume() {
        launchOnMedia {
            if (false == mediaPlayer?.isPlaying) {
                statusStack.push("start")
                mediaPlayer?.start()
            }
            livePlayState.postValue(true)
        }
    }

    override fun pause() {
        launchOnMedia {
            if (true == mediaPlayer?.isPlaying) {
                statusStack.push("pause")
                mediaPlayer?.pause()
            }
            livePlayState.postValue(false)
        }
    }

    override fun stop() {
        launchOnMedia {
            if (true == mediaPlayer?.isPlaying) {
                statusStack.push("stop")
                mediaPlayer?.stop()
            }
            livePlayState.postValue(false)
        }
    }

    override fun release() {
        launchOnMedia {
            releaseSync()
        }
    }

    private fun releaseSync() {
        playFuture?.finish()
        playFuture = null
        mediaPlayer?.apply {
            statusStack.push("release")
            if (isPlaying) stop()
            //reset()
            release()
        }
        mediaPlayer = null
    }

    fun togglePlayer(): MediaPlayer {
        releaseSync()
        statusStack.push("new Player")
        mediaPlayer = MediaPlayer().apply {
            setOnCompletionListener {
                livePlayState.value = false
            }
            setOnErrorListener { mp: MediaPlayer?, what: Int, extra: Int ->
                statusStack.push("error what:$what extra:$extra")
                RuntimeException("MediaPlayerError what:$what extra:$extra").e()
                livePlayState.value = false
                this@AudioPlayer.release()
                true
            }
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setVolume(1f, 1f)
        }
        return mediaPlayer!!
    }

    private fun launchOnMedia(block: () -> Unit) {
        mediaExecutor.execute {
            try {
                block()
            } catch (e: Throwable) {
                e.e()
                releaseSync()
            }
        }
    }

    companion object {
        private const val TAG = "AudioPlayer"
    }
}