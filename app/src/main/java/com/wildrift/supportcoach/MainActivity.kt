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
        "Braum", "Senna", "Seraphine", "Sona",
        "Karma","Janna"
    )

    private val adcs = arrayOf(
        "Jinx", "Kai'Sa", "Ezreal", "Jhin",
        "Ashe", "Xayah", "Caitlyn", "Varus",
        "Miss Fortune", "Lucian", "Tristana",
        "Draven", "Vayne", "Samira", "Nilah",
        "Corki", "Twitch", "Zeri", "Sivir","Kog'Maw"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

  private fun createLayout(): LinearLayout {
    val layout = LinearLayout(this)
    layout.orientation = LinearLayout.VERTICAL
    layout.gravity = Gravity.CENTER_HORIZONTAL
    layout.setPadding(24, 32, 24, 32)
    layout.setBackgroundColor(Color.rgb(10, 15, 29))

    return layout
  }

    private fun createText(text: String, size: Float): TextView {
    val view = TextView(this)
    view.text = text
    view.textSize = size
    view.setTextColor(Color.WHITE)
    view.gravity = Gravity.CENTER
    view.setPadding(8, 6, 8, 6)
    view.includeFontPadding = true
    view.minHeight = 0
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

    private fun createScroll(layout: LinearLayout) {
    val scroll = ScrollView(this)
    scroll.layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT
    )
    scroll.isFillViewport = true
    scroll.addView(layout)
    setContentView(scroll)
    }

    private fun addBackButton(layout: LinearLayout) {
        val button = Button(this)
        button.text = "VOLTAR"
        button.setOnClickListener { showHome() }
        layout.addView(button)
    }

    private fun showHome() {
    val layout = createLayout()

    layout.setBackgroundColor(Color.rgb(10, 15, 29))
    layout.setPadding(32, 48, 32, 32)

    val title = android.widget.TextView(this)
    title.text = "WILD RIFT"
    title.textSize = 32f
    title.setTextColor(Color.WHITE)
    title.gravity = Gravity.CENTER
    title.setTypeface(null, android.graphics.Typeface.BOLD)
    layout.addView(title)

    val subtitle = android.widget.TextView(this)
    subtitle.text = "SUPPORT COACH"
    subtitle.textSize = 22f
    subtitle.setTextColor(Color.rgb(0, 200, 255))
    subtitle.gravity = Gravity.CENTER
    subtitle.setTypeface(null, android.graphics.Typeface.BOLD)
    layout.addView(subtitle)

    val description = android.widget.TextView(this)
    description.text =
        "SEU TREINADOR ESTRATÉGICO DE BOT LANE"
    description.textSize = 13f
    description.setTextColor(Color.LTGRAY)
    description.gravity = Gravity.CENTER
    description.setPadding(0, 30, 0, 50)
    layout.addView(description)

    fun styleButton(button: Button) {
    button.setTextColor(Color.WHITE)
    button.textSize = 17f
    button.includeFontPadding = true
    button.gravity = Gravity.CENTER
    button.setTypeface(null, android.graphics.Typeface.BOLD)
    button.isAllCaps = false
    button.background = android.graphics.drawable.GradientDrawable().apply {
            setColor(Color.rgb(15, 75, 120))
            cornerRadius = 24f
            setStroke(2, Color.rgb(0, 200, 255))
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            65
        )
        params.setMargins(0, 12, 0, 12)
        button.layoutParams = params
    }

    val draftButton = Button(this)
    draftButton.text = "⚔  MODO DRAFT"
    styleButton(draftButton)
    draftButton.setOnClickListener {
        showDraftScreen()
    }
    layout.addView(draftButton)

    val matchButton = Button(this)
    matchButton.text = "🎯  MODO PARTIDA"
    styleButton(matchButton)
    matchButton.setOnClickListener {
        showMatchScreen()
    }
    layout.addView(matchButton)

    val footer = android.widget.TextView(this)
    footer.text = "DESENVOLVIDO POR NUMAK"
    footer.textSize = 11f
    footer.setTextColor(Color.GRAY)
    footer.gravity = Gravity.CENTER
    footer.setPadding(0, 55, 0, 0)
    layout.addView(footer)

    createScroll(layout)
    }

    // MODO DRAFT

    private fun showDraftScreen() {
        val layout = createLayout()

        layout.addView(createText(
            "ASSISTENTE DE DRAFT", 23f
        ))

        layout.addView(createText(
            "SEU TIME", 19f
        ))

        layout.addView(createText(
            "ADC aliado", 16f
        ))
        val allyAdc = createSpinner(adcs)
        layout.addView(allyAdc)

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
            "Selecione os campeões para receber recomendações.",
            15f
        )

        val analyzeButton = Button(this)
        analyzeButton.text = "RECOMENDAR PICKS E BANS"

        analyzeButton.setOnClickListener {
            result.text = generateDraftAnalysis(
                allyAdc.selectedItem.toString(),
                enemySupport.selectedItem.toString(),
                enemyAdc.selectedItem.toString()
            )
        }

        layout.addView(analyzeButton)
        layout.addView(result)

        addBackButton(layout)
        createScroll(layout)
    }

    private fun generateDraftAnalysis(
        allyAdc: String,
        enemySupport: String,
        enemyAdc: String
    ): String {

        val ranked = supports
            .filter { it != enemySupport }
            .map { champion ->
                Pair(
                    champion,
                    scoreSupport(
                        champion,
                        allyAdc,
                        enemySupport,
                        enemyAdc
                    )
                )
            }
            .sortedByDescending { it.second }
            .take(3)

        val picks = ranked.mapIndexed { index, item ->
            "${index + 1}º ${item.first}\n" +
            pickReason(
                item.first,
                allyAdc,
                enemySupport,
                enemyAdc
            )
        }.joinToString("\n\n")

        val bans = recommendBans(allyAdc, enemySupport)

        return """
            RESUMO DO DRAFT

            SEU ADC: $allyAdc
            SUPORTE INIMIGO: $enemySupport
            ADC INIMIGO: $enemyAdc

            TOP 3 SUPORTES RECOMENDADOS

            $picks

            BANS SUGERIDOS

            $bans

            DICA DO TREINADOR
            Escolha considerando também sua experiência
            com o campeão e a composição da equipe.

            Recomendações estratégicas iniciais.
            Desenvolvido por Numak.
        """.trimIndent()
    }

    private fun scoreSupport(
        champion: String,
        allyAdc: String,
        enemySupport: String,
        enemyAdc: String
    ): Int {

        var score = 0

        val engageEnemies = arrayOf(
            "Leona", "Nautilus", "Thresh",
            "Blitzcrank", "Pyke", "Rakan", "Alistar"
        )

        val pokeEnemies = arrayOf(
            "Lux", "Karma", "Senna",
            "Seraphine", "Morgana"
        )

        val engageAllies = arrayOf(
            "Kai'Sa", "Samira", "Draven",
            "Tristana", "Lucian", "Nilah"
        )

        val scalingAllies = arrayOf(
            "Jinx", "Vayne", "Twitch",
            "Zeri", "Kog'Maw"
        )

        if (enemySupport in engageEnemies) {
            if (champion == "Morgana") score += 4
            if (champion == "Alistar") score += 3
            if (champion == "Braum") score += 3
            if (champion == "Lulu") score += 2
        }

        if (enemySupport in pokeEnemies) {
            if (champion == "Nautilus") score += 3
            if (champion == "Blitzcrank") score += 3
            if (champion == "Pyke") score += 2
            if (champion == "Nami") score += 1
        }

        if (allyAdc in engageAllies) {
            if (champion in arrayOf(
                "Nautilus", "Leona", "Thresh",
                "Rakan", "Alistar"
            )) score += 4
        }

        if (allyAdc in scalingAllies) {
            if (champion in arrayOf(
                "Lulu", "Nami", "Braum",
                "Janna", "Yuumi"
            )) score += 4
        }


        if (allyAdc == "Jhin" &&
            champion in arrayOf("Nami", "Leona", "Morgana")
        ) score += 2
        if (allyAdc == "Jinx" &&
            champion in arrayOf("Lulu", "Nami", "Thresh")
        ) score += 3

        if (allyAdc == "Kai'Sa" &&
            champion in arrayOf("Nautilus", "Rakan", "Leona")
        ) score += 3

        if (allyAdc == "Caitlyn" &&
            champion in arrayOf("Morgana", "Karma", "Lux")
        ) score += 3

        if (allyAdc == "Draven" &&
            champion in arrayOf("Nautilus", "Leona", "Pyke")
        ) score += 3

        if (allyAdc == "Vayne" &&
            champion in arrayOf("Lulu", "Nami", "Braum")
        ) score += 3

        if (allyAdc == "Samira" &&
            champion in arrayOf("Nautilus", "Leona", "Alistar")
        ) score += 3

        if (allyAdc == "Ezreal" &&
            champion in arrayOf("Karma", "Nami", "Lux")
        ) score += 3

        if (enemyAdc in arrayOf(
            "Draven", "Lucian", "Caitlyn"
        )) {
            if (champion in arrayOf(
                "Braum", "Alistar", "Nautilus"
            )) score += 2
        }

         if (enemyAdc == "Samira" &&
            champion in arrayOf("Braum", "Alistar")
        ) score += 2

                if (enemyAdc in arrayOf("Jinx", "Vayne", "Tristana")) {
            if (champion in arrayOf("Braum", "Alistar", "Leona")) score += 2
        }

        score += matchupAdvantage(champion, enemySupport)

        return score
    }

    private fun matchupAdvantage(
        champion: String,
        enemySupport: String
    ): Int {

        val counters = mapOf(
            "Nautilus" to arrayOf("Lux", "Sona", "Senna"),
            "Morgana" to arrayOf("Nautilus", "Leona", "Thresh", "Blitzcrank"),
            "Alistar" to arrayOf("Leona", "Nautilus", "Rakan"),
            "Braum" to arrayOf("Blitzcrank", "Thresh"),
            "Blitzcrank" to arrayOf("Sona", "Yuumi", "Nami"),
            "Pyke" to arrayOf("Sona", "Yuumi", "Senna"),
            "Lulu" to arrayOf("Pyke", "Rakan"),
            "Nami" to arrayOf("Braum", "Alistar")
        )

        if (counters[champion]?.contains(enemySupport) == true) {
            return 3
        }

        return 0
    }
    private fun pickReason(
        champion: String,
        allyAdc: String,
        enemySupport: String,
        enemyAdc: String
    ): String {

        val reasons = mutableListOf<String>()
        if (matchupAdvantage(champion, enemySupport) > 0) {
            reasons.add(
                "VANTAGEM DE MATCHUP: este suporte possui " +
                "ferramentas favoráveis contra o suporte inimigo. " +
                "Explore as janelas de oportunidade."
            )
        }
        if (enemySupport in arrayOf(
            "Leona", "Nautilus", "Thresh",
            "Blitzcrank", "Pyke", "Rakan", "Alistar"
        ) && champion == "Morgana") {
            reasons.add("Escudo Negro pode bloquear controles importantes.")
        }

        if (champion == "Braum" &&
            enemyAdc in arrayOf("Draven", "Lucian", "Caitlyn", "Samira")
        ) {
            reasons.add("Boa ferramenta defensiva contra pressão e ataques.")
        }

        if (champion == "Alistar" &&
            enemySupport in arrayOf("Leona", "Nautilus", "Rakan")
        ) {
            reasons.add("Pode interromper investidas e proteger seu ADC.")
        }

        if (allyAdc in arrayOf(
            "Kai'Sa", "Samira", "Draven",
            "Tristana", "Lucian", "Nilah"
        ) && champion in arrayOf(
            "Nautilus", "Leona", "Thresh", "Rakan", "Alistar"
        )) {
            reasons.add("Combina com ADCs que aproveitam iniciações.")
        }

        if (allyAdc in arrayOf(
            "Jinx", "Vayne", "Twitch", "Zeri"
        ) && champion in arrayOf(
            "Lulu", "Nami", "Braum", "Yuumi"
        )) {
            reasons.add("Ajuda a proteger e fortalecer seu ADC.")
        }

        if (reasons.isEmpty()) {
            reasons.add(
                "Opção a considerar pela combinação da dupla e do confronto."
            )
        }

        return reasons.joinToString("\n")
    }

    private fun recommendBans(
        allyAdc: String,
        enemySupport: String
    ): String {

        val bans = when (allyAdc) {
            "Jinx", "Vayne", "Twitch", "Zeri" ->
                "Blitzcrank — ameaça de puxão e iniciação.\n" +
                "Draven — pressão elevada na rota."

            "Kai'Sa", "Samira", "Nilah" ->
                "Morgana — pode bloquear controles importantes.\n" +
                "Leona — forte potencial de iniciação."

            "Ezreal", "Jhin", "Ashe" ->
                "Blitzcrank — ameaça de iniciação.\n" +
                "Pyke — pressão e capacidade de executar alvos."

            else ->
                "Morgana — controle e Escudo Negro.\n" +
                "Blitzcrank — ameaça de puxão."
        }

        return bans +
            "\n\nAjuste os bans conforme os campeões " +
            "que já foram escolhidos ou removidos."
    }

    // MODO PARTIDA

    private fun showMatchScreen() {
        val layout = createLayout()

        layout.addView(createText(
            "ANÁLISE DA BOT LANE", 23f
        ))

        layout.addView(createText("SEU TIME", 19f))

        layout.addView(createText("Seu suporte", 16f))
        val mySupport = createSpinner(supports)
        layout.addView(mySupport)

        layout.addView(createText("Seu ADC aliado", 16f))
        val myAdc = createSpinner(adcs)
        layout.addView(myAdc)

        layout.addView(createText("TIME INIMIGO", 19f))

        layout.addView(createText("Suporte inimigo", 16f))
        val enemySupport = createSpinner(supports)
        layout.addView(enemySupport)

        layout.addView(createText("ADC inimigo", 16f))
        val enemyAdc = createSpinner(adcs)
        layout.addView(enemyAdc)

        val result = createText(
            "Escolha os quatro campeões para analisar.",
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

        addBackButton(layout)
        createScroll(layout)
    }

    private fun generateAnalysis(
        support: String,
        adc: String,
        enemySupport: String,
        enemyAdc: String
    ): String {

        if (support == "Nautilus") {
            return analyzeNautilus(
                adc, enemySupport, enemyAdc
            )
        }

        return """
            ANÁLISE DA BOT LANE

            Seu suporte: $support
            Seu ADC: $adc
            Suporte inimigo: $enemySupport
            ADC inimigo: $enemyAdc

            A análise individual deste suporte
            será desenvolvida nas próximas versões.

            Desenvolvido por Numak.
        """.trimIndent()
    }

    private fun analyzeNautilus(
        adc: String,
        enemySupport: String,
        enemyAdc: String
    ): String {

        val matchup = when (enemySupport) {
            "Morgana" ->
                "CONFRONTO DIFÍCIL\n" +
                "O Escudo Negro pode bloquear seu controle. " +
                "Tente provocar o uso do escudo antes de iniciar."

            "Alistar", "Braum" ->
                "CONFRONTO DE ATENÇÃO\n" +
                "Eles podem dificultar sua iniciação. " +
                "Escolha bem o momento de entrar."

            "Lulu", "Janna", "Nami", "Karma",
            "Sona", "Seraphine" ->
                "OPORTUNIDADE DE ENGAGE\n" +
                "Procure janelas de iniciação, mas cuidado " +
                "com o poke e as habilidades defensivas."

            "Thresh", "Leona", "Pyke",
            "Blitzcrank", "Rakan" ->
                "CONFRONTO DE INICIAÇÃO\n" +
                "Respeite o controle inimigo. " +
                "Evite trocar habilidades sem necessidade."

            "Senna", "Lux" ->
                "CONFRONTO DE POKE\n" +
                "Use os arbustos e evite receber habilidades " +
                "gratuitamente. Procure uma aproximação segura."

            else ->
                "CONFRONTO VARIÁVEL\n" +
                "Observe as habilidades inimigas e procure " +
                "uma oportunidade segura."
        }

        val synergy = when (adc) {
            "Kai'Sa", "Samira", "Draven",
            "Tristana", "Lucian", "Nilah" ->
                "BOA OPORTUNIDADE DE COMBATE\n" +
                "Sua iniciação pode permitir que seu ADC " +
                "entre rapidamente na luta."

            "Jinx", "Vayne", "Twitch", "Zeri" ->
                "PROTEJA O ESCALAMENTO\n" +
                "Prepare iniciações seguras e proteja seu ADC. " +
                "Evite lutas desnecessárias no começo."

            "Ezreal", "Caitlyn", "Jhin",
            "Ashe", "Varus", "Miss Fortune",
            "Sivir", "Corki" ->
                "COMBINE O ALCANCE\n" +
                "Use seu controle para criar oportunidades " +
                "de dano e pressão junto com seu ADC."

            else ->
                "COMBINE SUAS HABILIDADES\n" +
                "Procure iniciar quando seu ADC puder acompanhar."
        }

        val enemyAdcPlan = when (enemyAdc) {
            "Draven", "Lucian", "Caitlyn" ->
                "Cuidado com o dano e a pressão nos primeiros níveis."

            "Jinx", "Vayne", "Twitch", "Kai'Sa" ->
                "Não permita que o inimigo escale gratuitamente. " +
                "Procure oportunidades de vantagem na rota."

            "Ezreal" ->
                "Observe a habilidade de deslocamento do Ezreal " +
                "antes de gastar seu gancho."

            "Samira", "Nilah" ->
                "Respeite o potencial de combate próximo da dupla."

            else ->
                "Observe o alcance e as habilidades do ADC inimigo."
        }

        return """
            NAUTILUS — ANÁLISE ESTRATÉGICA

            SUA DUPLA
            Suporte: Nautilus
            ADC: $adc

            DUPLA INIMIGA
            Suporte: $enemySupport
            ADC: $enemyAdc

            MATCHUP CONTRA O SUPORTE
            $matchup

            SINERGIA COM SEU ADC
            $synergy

            ATENÇÃO AO ADC INIMIGO
            $enemyAdcPlan

            PLANO DE JOGO

            NÍVEIS 1 E 2
            Priorize posicionamento e controle dos arbustos.
            Não gaste seu gancho sem uma oportunidade clara.

            NÍVEIS 3 A 5
            Você ganha mais ferramentas para iniciar.
            Procure combinar seu controle com o dano do ADC.

            INICIAÇÃO
            Use seu gancho quando seu ADC puder acompanhar.
            Não entre sozinho em uma luta desfavorável.

            PROTEÇÃO
            Guarde habilidades de controle quando necessário
            para impedir que os inimigos alcancem seu ADC.

            LUTAS EM EQUIPE
            Use sua ultimate em alvos importantes.
            Avalie se é melhor iniciar ou proteger sua equipe.

            DICA DO TREINADOR
            Um bom Nautilus não é aquele que usa o gancho
            toda hora. É aquele que sabe quando acertar.

            Desenvolvido por Numak.
        """.trimIndent()
    }
}
