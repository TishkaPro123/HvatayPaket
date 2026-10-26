package com.hvataypaket

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class AutoTapService : AccessibilityService() {

    companion object {
        @Volatile
        var automationRunning = false

        @Volatile
        var serviceConnected = false

        var instance: AutoTapService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        serviceConnected = true
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}

    override fun onInterrupt() {
        automationRunning = false
    }

    override fun onDestroy() {
        instance = null
        serviceConnected = false
        automationRunning = false
        super.onDestroy()
    }

    fun tap(x: Float, y: Float) {
        if (!automationRunning) return

        val path = Path().apply {
            moveTo(x, y)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(
                GestureDescription.StrokeDescription(path, 0, 100)
            )
            .build()

        dispatchGesture(gesture, null, null)
    }

    fun testTap(x: Float, y: Float, callback: (Boolean) -> Unit) {
        val path = Path().apply {
            moveTo(x, y)
        }

        val gesture = GestureDescription.Builder()
            .addStroke(
                GestureDescription.StrokeDescription(path, 0, 100)
            )
            .build()

        dispatchGesture(gesture, object :
            AccessibilityService.GestureResultCallback() {
            override fun onCompleted(
                gestureDescription: GestureDescription
            ) {
                callback(true)
            }

            override fun onCancelled(
                gestureDescription: GestureDescription
            ) {
                callback(false)
            }
        }, null)
    }
}
