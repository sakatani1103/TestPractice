package com.example.testpractice

import android.annotation.TargetApi
import android.provider.Settings
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@TargetApi(31)
class AnimationTestRule : TestRule {
    private val currWinScale = getWindowAnimationScale()
    private val currTraScale = getTransitionAnimationScale()
    private val currDurScale = getAnimatorDurationScale()

    // スケールの取得
    fun getWindowAnimationScale(): Float = _getScale(Settings.Global.WINDOW_ANIMATION_SCALE)
    fun getTransitionAnimationScale(): Float = _getScale(Settings.Global.TRANSITION_ANIMATION_SCALE)
    fun getAnimatorDurationScale(): Float = _getScale(Settings.Global.ANIMATOR_DURATION_SCALE)

    private fun _getScale(name: String): Float {
        val _uiDevice = UiDevice.getInstance(
            InstrumentationRegistry.getInstrumentation()
        )
        val _command = String.format("settings get global %s", name)
        val _result = _uiDevice.executeShellCommand(_command)
        if (!_result.startsWith("null")) {
            val _scale = java.lang.Float.valueOf(_result)
            if (_scale >= 0.0f) {
                return _scale
            }
        }
        return 1.0f
    }

    fun putWindowAnimationScale(scale: Float) {
        _putScale(Settings.Global.WINDOW_ANIMATION_SCALE, scale)
    }

    fun putTransitionAnimationScale(scale: Float) {
        _putScale(Settings.Global.TRANSITION_ANIMATION_SCALE, scale)
    }

    fun putAnimatorDurationScale(scale: Float) {
        _putScale(Settings.Global.ANIMATOR_DURATION_SCALE, scale)
    }

    private fun _putScale(name: String, scale: Float) {
        val _uiDevice = UiDevice.getInstance(
            InstrumentationRegistry.getInstrumentation()
        )
        val _command = String.format("settings put global %s %f", name, scale)
        _uiDevice.executeShellCommand(_command)
    }

    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    putWindowAnimationScale(0.0f)
                    putTransitionAnimationScale(0.0f)
                    putAnimatorDurationScale(0.0f)
                    base?.evaluate()
                } finally {
                    putWindowAnimationScale(currWinScale)
                    putTransitionAnimationScale(currTraScale)
                    putAnimatorDurationScale(currDurScale)
                }
            }
        }
    }
}