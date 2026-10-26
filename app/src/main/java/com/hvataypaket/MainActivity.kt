package com.hvataypaket

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.provider.Settings
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private val bg = Color.rgb(16, 19, 29)
    private val panel = Color.rgb(28, 34, 47)
    private val neon = Color.rgb(169, 255, 104)
    private val muted = Color.rgb(165, 174, 192)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 36, 24, 28)
            setBackgroundColor(bg)
        }

        val title = TextView(this).apply {
            text = "ХВАТАЙ ПАКЕТ"
            textSize = 29f
            setTextColor(neon)
            gravity = Gravity.CENTER
            letterSpacing = 0.08f
        }

        val subtitle = TextView(this).apply {
            text = "АВТО • ПАНЕЛЬ УПРАВЛЕНИЯ"
            textSize = 12f
            setTextColor(muted)
            gravity = Gravity.CENTER
        }

        val statusCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(20, 22, 20, 22)
            setBackgroundColor(panel)
        }

        val statusTitle = TextView(this).apply {
            text = "●  СИСТЕМА ГОТОВА"
            textSize = 16f
            setTextColor(neon)
            gravity = Gravity.CENTER
        }

        val statusText = TextView(this).apply {
            text = "Автоматическая игра пока не подключена"
            textSize = 14f
            setTextColor(muted)
            gravity = Gravity.CENTER
        }

        statusCard.addView(statusTitle)
        statusCard.addView(statusText)

        val startButton = Button(this).apply {
            text = "▶  ЗАПУСТИТЬ"
            textSize = 16f
            setTextColor(Color.rgb(16, 19, 29))
            setBackgroundColor(neon)
            setOnClickListener {
                statusTitle.text = "●  ОЖИДАЕТ НАСТРОЙКИ"
                statusText.text = "Сначала подключи службу доступности"
            }
        }

        val settingsButton = Button(this).apply {
            text = "⚙  НАСТРОЙКИ СЛУЖБЫ"
            textSize = 14f
            setTextColor(neon)
            setBackgroundColor(panel)
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
        }

        val note = TextView(this).apply {
            text = "Версия 0.1 • интерфейс в разработке"
            textSize = 12f
            setTextColor(muted)
            gravity = Gravity.CENTER
        }

        root.addView(title)
        root.addView(subtitle)
        root.addView(space(24))
        root.addView(statusCard)
        root.addView(space(20))
        root.addView(startButton)
        root.addView(space(8))
        root.addView(settingsButton)
        root.addView(space(24))
        root.addView(note)

        setContentView(root)
    }

    private fun space(height: Int): View {
        return View(this).apply {
            layoutParams = LinearLayout.LayoutParams(1, height)
        }
    }
}
