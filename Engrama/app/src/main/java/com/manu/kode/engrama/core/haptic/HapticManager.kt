package com.manu.kode.engrama.core.haptic

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun error() {
        vibrate(longArrayOf(0, 50, 50, 50), VibrationEffect.EFFECT_DOUBLE_CLICK)
    }

    fun success() {
        vibrate(longArrayOf(0, 30), VibrationEffect.EFFECT_CLICK)
    }

    fun impact() {
        vibrate(longArrayOf(0, 40), VibrationEffect.EFFECT_TICK)
    }

    private fun vibrate(pattern: LongArray, effectId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(effectId))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        }
    }
}