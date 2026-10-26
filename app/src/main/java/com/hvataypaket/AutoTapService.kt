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
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceConnected = true
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Распознавание игры пока не реализовано.
    }

    override fun onInterrupt() {
        automationRunning = false
    }

    override fun onDestroy() {
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
}
