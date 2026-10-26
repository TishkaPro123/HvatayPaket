package com.hvataypaket

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.os.Handler
import android.os.Looper

class MainActivity : Activity() {

    private val bg = Color.rgb(16, 19, 29)
    private val panel = Color.rgb(28, 34, 47)
    private val neon = Color.rgb(169, 255, 104)
    private val muted = Color.rgb(165, 174, 192)

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var status: TextView
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText
    private lateinit var delayInput: EditText

    private var tapping = false
    private var collectCoins = false

    private val tapLoop = object : Runnable {
        override fun run() {
            if (!tapping) return

            val x = xInput.text.toString().toFloatOrNull()
            val y = yInput.text.toString().toFloatOrNull()
            val delay = delayInput.text.toString().toLongOrNull()
                ?: 200L

            if (x == null || y == null) {
                stopTapping()
                status.text = "Введи корректные координаты"
                return
            }

            val service = AutoTapService.instance
            if (service == null) {
                stopTapping()
                status.text = "Включи службу доступности"
                return
            }

            service.testTap(x, y) { success ->
                if (!success) {
                    runOnUiThread {
                        status.text = "Нажатие не выполнено"
                    }
                }
            }

            handler.postDelayed(this, delay.coerceIn(100L, 5000L))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 24, 24, 24)
            setBackgroundColor(bg)
        }

        fun label(text: String): TextView =
            TextView(this).apply {
                this.text = text
                textSize = 16f
                setTextColor(neon)
            }

        fun field(value: String, hintText: String): EditText =
            EditText(this).apply {
                setText(value)
                hint = hintText
                textSize = 16f
                setTextColor(Color.WHITE)
                setHintTextColor(muted)
                inputType = 8194
                setSingleLine(true)
            }

        root.addView(label("ХВАТАЙ ПАКЕТ АВТО"))
        root.addView(space(12))
        status = label("● ГОТОВО К НАСТРОЙКЕ")
        root.addView(status)
        root.addView(space(12))

        root.addView(label("Координата X"))
        xInput = field("400", "X")
        root.addView(xInput)

        root.addView(label("Координата Y"))
        yInput = field("900", "Y")
        root.addView(yInput)

        root.addView(label("Интервал (мс, минимум 100)"))
        delayInput = field("200", "200")
        root.addView(delayInput)

        val start = Button(this).apply {
            text = "▶ СТАРТ"
            setOnClickListener {
                if (!AutoTapService.serviceConnected ||
                    AutoTapService.instance == null) {
                    status.text = "Сначала включи службу доступности"
                    startActivity(
                        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    )
                    return@setOnClickListener
                }
                if (xInput.text.toString().toFloatOrNull() == null ||
                    yInput.text.toString().toFloatOrNull() == null) {
                    status.text = "Проверь координаты X и Y"
                    return@setOnClickListener
                }

                tapping = true
                AutoTapService.automationRunning = true
                status.text = "● АВТОНАЖАТИЯ ВКЛЮЧЕНЫ"
                handler.removeCallbacks(tapLoop)
                handler.post(tapLoop)
            }
        }

        val stop = Button(this).apply {
            text = "■ СТОП"
            setOnClickListener {
                stopTapping()
                status.text = "● ОСТАНОВЛЕНО"
            }
        }

        val collect = Button(this).apply {
            text = "🪙 ЗАБРАТЬ МОНЕТЫ"
            setOnClickListener {
                collectCoins = true
                val service = AutoTapService.instance
                if (service != null) {
                    service.testTap(
                        xInput.text.toString().toFloatOrNull() ?: 400f,
                        yInput.text.toString().toFloatOrNull() ?: 1550f
                    ) {
                        runOnUiThread {
                            status.text = "Команда получения монет выполнена"
                            collectCoins = false
                        }
                    }
                } else {
                    collectCoins = false
                    status.text = "Сначала включи службу"
                }
            }
        }

        listOf(start, stop, collect).forEach {
            it.setTextColor(if (it == start) bg else neon)
            it.setBackgroundColor(if (it == start) neon else panel)
            root.addView(it)
        }

        root.addView(space(12))
        root.addView(label("Сначала проверь координаты на безопасном экране."))

        setContentView(root)
    }

    private fun stopTapping() {
        tapping = false
        AutoTapService.automationRunning = false
        handler.removeCallbacks(tapLoop)
    }

    override fun onDestroy() {
        stopTapping()
        super.onDestroy()
    }

    private fun space(height: Int): View =
        View(this).apply {
            layoutParams = LinearLayout.LayoutParams(1, height)
        }
}
