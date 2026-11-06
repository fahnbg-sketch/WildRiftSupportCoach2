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
        "Karma"
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

    private fun createScroll(layout: LinearLayout) {
        val scroll = ScrollView(this)
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

        layout.addView(createText(
            "WILD RIFT SUPPORT COACH", 24f
        ))

        layout.addView(createText(
            "Desenvolvido por Numak", 15f
        ))

        layout.addView(createText(
            "Seu treinador estratégico de BOT LANE", 16f
        ))

        val draftButton = Button(this)
        draftButton.text = "MODO DRAFT"
        draftButton.setOnClickListener {
            showDraftScreen()
        }
        layout.addView(draftButton)

        val matchButton = Button(this)
        matchButton.text = "MODO PARTIDA"
        matchButton.setOnClickListener {
            showMatchScreen()
        }
        layout.addView(matchButton)

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

        if (allyAdc == "Ezreal" &&
            champion in arrayOf("Karma", "Nami", "Lux")
        ) score += 2

        if (allyAdc == "Jhin" &&
            champion in arrayOf("Nami", "Leona", "Morgana")
        ) score += 2

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

                // COUNTERS ENTRE SUPORTES

        if (enemySupport == "Nautilus" ||
            enemySupport == "Leona" ||
            enemySupport == "Blitzcrank" ||
            enemySupport == "Thresh" ||
            enemySupport == "Pyke") {

            if (champion == "Morgana") score += 4
            if (champion == "Alistar") score += 3
            if (champion == "Braum") score += 2
        }

        if (enemySupport == "Lux" ||
            enemySupport == "Senna" ||
            enemySupport == "Seraphine" ||
            enemySupport == "Sona") {

            if (champion == "Nautilus") score += 3
            if (champion == "Blitzcrank") score += 3
            if (champion == "Pyke") score += 2
        }

        if (enemySupport == "Samira") {
            if (champion == "Braum") score += 3
            if (champion == "Alistar") score += 3
        } 
        return score
    }

    private fun pickReason(
        champion: String,
        allyAdc: String,
        enemySupport: String,
        enemyAdc: String
    ): String {

        val reasons = mutableListOf<String>()

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
