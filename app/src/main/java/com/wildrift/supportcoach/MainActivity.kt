package com.wildrift.supportcoach

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

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(24, 24, 24, 24)
        layout.setBackgroundColor(Color.rgb(18, 20, 30))

        val title = TextView(this)
        title.text = "WILD RIFT SUPPORT COACH"
        title.textSize = 24f
        title.setTextColor(Color.WHITE)
        title.gravity = Gravity.CENTER

        val description = TextView(this)
        description.text = "Seu assistente estratégico de suporte"
        description.textSize = 16f
        description.setTextColor(Color.LTGRAY)
        description.gravity = Gravity.CENTER

        val button = Button(this)
        button.text = "COMEÇAR"

        button.setOnClickListener {
            description.text = "Preparando análise da partida!"
        }

        layout.addView(title)
        layout.addView(description)
        layout.addView(button)

        setContentView(layout)
    }
}
