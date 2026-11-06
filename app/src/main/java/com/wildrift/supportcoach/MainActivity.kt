
package com.wildrift.supportcoach

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {

    private val supports = arrayOf(
        "Nautilus", "Thresh", "Leona", "Lulu",
        "Yuumi", "Nami", "Lux", "Morgana",
        "Pyke", "Blitzcrank", "Rakan", "Alistar",
        "Braum", "Senna", "Seraphine", "Sona"
    )

    private val adcs = arrayOf(
        "Jinx", "Kai'Sa", "Ezreal", "Jhin",
        "Ashe", "Xayah", "Caitlyn", "Varus",
        "Miss Fortune", "Lucian", "Tristana",
        "Draven", "Vayne", "Samira", "Nilah",
        "Corki", "Twitch", "Zeri", "Sivir"
    )

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
        view.setPadding(8, 14, 8, 14)
        return view
    }

    private fun createSpinner(items: Array<String>): Spinner {
        val spinner = Spinner(this)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            items
        )
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner.adapter = adapter
        return spinner
    }

    private fun showHome() {
        val layout = createLayout()

        layout.addView(createText(
            "WILD RIFT SUPPORT COACH", 24f
        ))

        layout.addView(createText(
            "Seu treinador estratégico de BOT LANE", 16f
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

        layout.addView(createText(
            "ANÁLISE DA BOT LANE", 23f
        ))

        layout.addView(createText(
            "SEU TIME", 19f
        ))

        layout.addView(createText(
            "Seu suporte", 16f
        ))
        val mySupport = createSpinner(supports)
        layout.addView(mySupport)

        layout.addView(createText(
            "Seu ADC aliado", 16f
        ))
        val myAdc = createSpinner(adcs)
        layout.addView(myAdc)

        layout.addView(createText(
            "TIME INIMIGO", 19f
        ))

        layout.addView(createText(
            "Suporte inimigo", 16f
        ))
        val enemySupport = createSpinner(supports)
        layout.addView(enemySupport)

        layout.addView(createText(
            "ADC inimigo", 16f
        ))
        val enemyAdc = createSpinner(adcs)
        layout.addView(enemyAdc)

        val result = createText(
            "Escolha os quatro campeões para analisar a rota.",
            15f
        )

        val analyzeButton = Button(this)
        analyzeButton.text = "ANALISAR CONFRONTO"

        analyzeButton.setOnClickListener {
            result.text = generateAnalysis(
                mySupport.selectedItem.toString(),
                myAdc.selectedItem.toString(),
                enemySupport.selectedItem.toString(),
                enemyAdc.selectedItem.toString()
            )
        }

        layout.addView(analyzeButton)
        layout.addView(result)

        val backButton = Button(this)
        backButton.text = "VOLTAR"
        backButton.setOnClickListener {
            showHome()
        }
        layout.addView(backButton)

        val scroll = ScrollView(this)
        scroll.addView(layout)
        setContentView(scroll)
    }

    private fun generateAnalysis(
        support: String,
        adc: String,
        enemySupport: String,
        enemyAdc: String
    ): String {

        val supportStyle = when (enemySupport) {
            "Nautilus", "Thresh", "Leona",
            "Blitzcrank", "Pyke", "Rakan",
            "Alistar" -> "INICIAÇÃO E CONTROLE"

            "Lux", "Morgana", "Seraphine",
            "Senna" -> "POKE E CONTROLE À DISTÂNCIA"

            "Lulu", "Yuumi", "Nami",
            "Sona", "Braum" -> "PROTEÇÃO E SUSTENTAÇÃO"

            else -> "ESTILO VARIÁVEL"
        }

        val adcStyle = when (enemyAdc) {
            "Draven", "Caitlyn", "Lucian",
            "Miss Fortune" -> "PRESSÃO DE ROTA"

            "Ezreal", "Varus", "Jhin",
            "Ashe", "Corki" -> "POKE E ALCANCE"

            "Jinx", "Kai'Sa", "Tristana",
            "Vayne", "Twitch", "Zeri",
            "Sivir" -> "ESCALAMENTO"

            "Samira", "Nilah" -> "COMBATE PRÓXIMO"

            else -> "ESTILO MISTO"
        }

        val enemyThreat = when {
            supportStyle == "INICIAÇÃO E CONTROLE" &&
            adcStyle == "PRESSÃO DE ROTA" ->
                "ALTA PRESSÃO: respeite a iniciação e o dano da dupla."

            supportStyle == "INICIAÇÃO E CONTROLE" ->
                "PERIGO DE ENGAGE: cuidado com controles e iniciações."

            supportStyle == "POKE E CONTROLE À DISTÂNCIA" ->
                "PRESSÃO À DISTÂNCIA: evite receber habilidades gratuitamente."

            supportStyle == "PROTEÇÃO E SUSTENTAÇÃO" ->
                "DUPLA RESISTENTE: procure trocas coordenadas."

            else ->
                "CONFRONTO VARIÁVEL: observe o posicionamento inimigo."
        }

        val strategy = when (supportStyle) {
            "INICIAÇÃO E CONTROLE" ->
                "Espere o inimigo gastar habilidades importantes. " +
                "Evite ficar exposto e procure uma oportunidade segura."

            "POKE E CONTROLE À DISTÂNCIA" ->
                "Use os arbustos e a movimentação para evitar poke. " +
                "Avance quando o inimigo errar suas habilidades."

            "PROTEÇÃO E SUSTENTAÇÃO" ->
                "Procure trocas coordenadas e observe as habilidades " +
                "de proteção antes de iniciar."

            else ->
                "Observe as habilidades inimigas e procure trocas favoráveis."
        }

        val ownStrategy = when (support) {
            "Nautilus", "Thresh", "Leona",
            "Blitzcrank", "Pyke", "Rakan",
            "Alistar" ->
                "Seu suporte tem ferramentas de iniciação. " +
                "Procure oportunidades quando o inimigo estiver vulnerável."

            "Lulu", "Yuumi", "Nami", "Sona",
            "Braum", "Seraphine" ->
                "Priorize proteger seu ADC e escolha bem o momento das trocas."

            else ->
                "Use suas habilidades para controlar o espaço e ajudar seu ADC."
        }

        return """
            ANÁLISE DA BOT LANE

            SEU TIME
            Suporte: $support
            ADC: $adc

            TIME INIMIGO
            Suporte: $enemySupport
            ADC: $enemyAdc

            ESTILO DA DUPLA INIMIGA
            Suporte: $supportStyle
            ADC: $adcStyle

            NÍVEL DE ATENÇÃO
            $enemyThreat

            SUA ESTRATÉGIA
            $ownStrategy

            COMO JOGAR A ROTA
            $strategy

            PLANO DE JOGO
            • Coordene suas jogadas com $adc.
            • Observe o posicionamento dos dois inimigos.
            • Evite iniciar sem avaliar o perigo.
            • Proteja seu ADC nas trocas.
            • Aproveite habilidades inimigas desperdiçadas.

            Análise inicial baseada nos estilos dos campeões.
            Matchups específicos serão adicionados nas próximas versões.
        """.trimIndent()
    }
}


