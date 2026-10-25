package com.hvataypaket

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(Color.rgb(245, 245, 245))
        }

        val title = TextView(this).apply {
            text = "Хватай пакет Авто"
            textSize = 26f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }

        val info = TextView(this).apply {
            text = "\nАвтоматизация игры\n\nПока режим тестовый."
            textSize = 18f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
        }

        val button = Button(this).apply {
            text = "Проверить запуск"
            setOnClickListener {
                info.text = "\nПриложение запущено!\n\nАвтоматические нажатия ещё не подключены."
            }
        }

        layout.addView(title)
        layout.addView(info)
        layout.addView(button)
        setContentView(layout)
    }
}
