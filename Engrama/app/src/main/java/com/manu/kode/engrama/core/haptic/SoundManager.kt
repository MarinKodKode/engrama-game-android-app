package com.manu.kode.engrama.core.haptic

import android.content.Context
import android.media.MediaPlayer
import com.manu.kode.engrama.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    enum class GameSound(val resId: Int) {
        SOUND_1(R.raw.sound_1),
        SOUND_2(R.raw.sound_2),
        SOUND_3(R.raw.sound_3),
        SOUND_4(R.raw.sound_4),
        SOUND_5(R.raw.sound_5),
        SOUND_6(R.raw.sound_6),
        SOUND_7(R.raw.sound_7),
        SOUND_8(R.raw.sound_8),
        SOUND_9(R.raw.sound_9),
        SOUND_10(R.raw.sound_10),
        SOUND_11(R.raw.sound_11),
    }

    private var player: MediaPlayer? = null

    fun play(sound: GameSound) {
        player?.release()
        player = MediaPlayer.create(context, sound.resId)
        player?.start()
        player?.setOnCompletionListener { it.release() }
    }

    fun release() {
        player?.release()
        player = null
    }
}