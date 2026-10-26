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

    private lateinit var statusTitle: TextView
    private lateinit var statusText: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 32, 24, 32)
            setBackgroundColor(bg)
        }

        val title = TextView(this).apply {
            text = "ХВАТАЙ ПАКЕТ"
            textSize = 29f
            setTextColor(neon)
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "АВТО • ПАНЕЛЬ УПРАВЛЕНИЯ"
            textSize = 13f
            setTextColor(muted)
            gravity = Gravity.CENTER
        }

        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(16, 22, 16, 22)
            setBackgroundColor(panel)
        }

        statusTitle = TextView(this).apply {
            textSize = 17f
            gravity = Gravity.CENTER
            setTextColor(neon)
        }

        statusText = TextView(this).apply {
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(muted)
        }

        card.addView(statusTitle)
        card.addView(space(8))
        card.addView(statusText)

        startButton = Button(this).apply {
            text = "▶  ЗАПУСТИТЬ"
            setTextColor(bg)
            setBackgroundColor(neon)
            setOnClickListener {
                if (!AutoTapService.serviceConnected) {
                    statusTitle.text = "●  СЛУЖБА ВЫКЛЮЧЕНА"
                    statusText.text =
                        "Открой настройки и включи службу приложения"
                    return@setOnClickListener
                }

                AutoTapService.automationRunning = true
                updateStatus()
            }
        }

        stopButton = Button(this).apply {
            text = "■  ОСТАНОВИТЬ"
            setTextColor(neon)
            setBackgroundColor(panel)
            setOnClickListener {
                AutoTapService.automationRunning = false
                updateStatus()
            }
        }

        val settingsButton = Button(this).apply {
            text = "⚙  НАСТРОЙКИ СЛУЖБЫ"
            setTextColor(neon)
            setBackgroundColor(panel)
            setOnClickListener {
                startActivity(
                    Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                )
            }
        }

        root.addView(title)
        root.addView(space(8))
        root.addView(subtitle)
        root.addView(space(28))
        root.addView(card)
        root.addView(space(16))
        root.addView(startButton)
        root.addView(stopButton)
        root.addView(settingsButton) 
        val testButton = Button(this).apply {
            text = "☝  ТЕСТ НАЖАТИЯ"
            setTextColor(neon)
            setBackgroundColor(panel)

            setOnClickListener {
                val service = AutoTapService.instance

                if (service == null) {
                    statusTitle.text = "●  СЛУЖБА НЕ ПОДКЛЮЧЕНА"
                    statusText.text = "Включи службу в настройках Android"
                } else {
                    service.testTap(200f, 400f) { success ->
                        runOnUiThread {
                            statusTitle.text =
                                if (success) "●  ТЕСТ УСПЕШЕН"
                                else "●  НАЖАТИЕ НЕ ВЫПОЛНЕНО"

                            statusText.text =
                                "Проверка координат экрана: X=200, Y=400"
                        }
                    }
                }
            }
        }

        root.addView(testButton)
        root.addView(space(16))

        val footer = TextView(this).apply {
            text = "Версия 0.2 • автоматизация в разработке"
            textSize = 12f
            gravity = Gravity.CENTER
            setTextColor(muted)
        }

        root.addView(footer)
        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        if (::statusTitle.isInitialized) updateStatus()
    }private fun updateStatus() {
        when {
            !AutoTapService.serviceConnected -> {
                statusTitle.text = "●  СЛУЖБА ВЫКЛЮЧЕНА"
                statusText.text = "Включи службу в настройках Android"
            }

            AutoTapService.automationRunning -> {
                statusTitle.text = "●  РЕЖИМ ВКЛЮЧЁН"
                statusText.text =
                    "Служба активна; распознавание игры ещё не готово"
            }

            else -> {
                statusTitle.text = "●  ГОТОВО К ЗАПУСКУ"
                statusText.text = "Служба подключена, режим остановлен"
            }
        }
    }

    private fun space(height: Int): View {
        return View(this).apply {
            layoutParams =
                LinearLayout.LayoutParams(1, height)
        }
    }
}
