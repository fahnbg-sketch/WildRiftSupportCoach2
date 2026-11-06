package com.wildrift.supportcoach

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun createLayout(): LinearLayout {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(24, 24, 24, 24)
        layout.setBackgroundColor(Color.rgb(18, 20, 30))
        return layout
    }

    private fun createText(text: String, size: Float): TextView {
        val view = TextView(this)
        view.text = text
        view.textSize = size
        view.setTextColor(Color.WHITE)
        view.gravity = Gravity.CENTER
        view.setPadding(8, 16, 8, 16)
        return view
    }

    private fun showHome() {
        val layout = createLayout()

        layout.addView(createText("WILD RIFT SUPPORT COACH", 24f))
        layout.addView(createText(
            "Seu assistente estratégico de suporte", 16f
        ))

        val button = Button(this)
        button.text = "COMEÇAR"

        button.setOnClickListener {
            showSelectionScreen()
        }

        layout.addView(button)
        setContentView(layout)
    }

    private fun showSelectionScreen() {
        val layout = createLayout()

        layout.addView(createText("ANÁLISE DA PARTIDA", 23f))
        layout.addView(createText("Selecione o suporte inimigo", 16f))

        val supports = arrayOf(
            "Nautilus", "Thresh", "Leona", "Lulu",
            "Yuumi", "Nami", "Lux", "Morgana",
            "Pyke", "Blitzcrank", "Rakan"
        )

        val supportSpinner = Spinner(this)
        val supportAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            supports
        )
        supportAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        supportSpinner.adapter = supportAdapter
        layout.addView(supportSpinner)

        layout.addView(createText("Selecione seu ADC aliado", 16f))

        val adcs = arrayOf(
            "Jinx", "Kai'Sa", "Ezreal", "Jhin",
            "Ashe", "Xayah", "Caitlyn", "Varus",
            "Miss Fortune", "Lucian", "Tristana"
        )

        val adcSpinner = Spinner(this)
        val adcAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            adcs
        )
        adcAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        adcSpinner.adapter = adcAdapter
        layout.addView(adcSpinner)

        val result = createText(
            "Escolha os campeões para começar.", 15f
        )

        val analyzeButton = Button(this)
        analyzeButton.text = "ANALISAR CONFRONTO"

        analyzeButton.setOnClickListener {
            val enemy = supportSpinner.selectedItem.toString()
            val adc = adcSpinner.selectedItem.toString()

            result.text = """
                CONFRONTO SELECIONADO

                Suporte inimigo: $enemy
                Seu ADC: $adc

                ORIENTAÇÕES INICIAIS

                • Observe as habilidades de controle do inimigo.
                • Evite trocas desfavoráveis.
                • Coordene suas iniciações com seu ADC.
                • Ajuste sua estratégia conforme a partida.
                
                Análise detalhada será adicionada nas próximas versões.
            """.trimIndent()
        }

        layout.addView(analyzeButton)
        layout.addView(result)

        val backButton = Button(this)
        backButton.text = "VOLTAR"
        backButton.setOnClickListener {
            showHome()
        }

        layout.addView(backButton)
        setContentView(layout)
    }
}
