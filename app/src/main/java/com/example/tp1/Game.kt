package com.example.tp1

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class Game: AppCompatActivity() {
    val ROWS = 3
    val COLUMNS = 4
    val MAXHEALTH = 6

    lateinit var mainLayout: LinearLayout
    lateinit var gridLayout: GridLayout
    lateinit var pointsTextView: TextView
    lateinit var lifeTextView: TextView
    lateinit var cases : MutableList<Case>
    var points = 0
    var life = MAXHEALTH

    // pop up
    lateinit var defeatDialog: AlertDialog
    lateinit var victoryDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainLayout = LinearLayout(this)
        mainLayout.orientation = LinearLayout.VERTICAL
        mainLayout.gravity = Gravity.CENTER_HORIZONTAL

        gridLayout = GridLayout(this).apply {
            rowCount = ROWS
            columnCount = COLUMNS
        }

        pointsTextView = TextView(this)
        pointsTextView.textSize = 50f
        pointsTextView.setText("Points: " + points.toString())

        lifeTextView = TextView(this)
        lifeTextView.textSize = 50f
        lifeTextView.setText("Vie: " + life.toString())

        mainLayout.addView(gridLayout)
        mainLayout.addView(pointsTextView)
        mainLayout.addView(lifeTextView)

        initCases()
        initViews()

        setContentView(mainLayout)

        var builderDefeat = AlertDialog.Builder(this)
        builderDefeat.setTitle("Défaite!")
        builderDefeat.setMessage("Voulez vous réessayer?")
        builderDefeat.setIcon(ImageRessources.getImageResource("SadFace"))

        builderDefeat.setPositiveButton("Oui") { dialog, wich->
            restartGame()
        }
        builderDefeat.setNegativeButton("Non") { dialog, wich->
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        defeatDialog = builderDefeat.create()
    }

    // Créer les objets cases qui représente le data d'une case
    private fun initCases() {
        val totalValues = ROWS * COLUMNS
        val pairsCount = totalValues / 2
        val list = mutableListOf<Int>()
        for(i in 1..pairsCount){
            list.add(i)
            list.add(i)
        }
        list.shuffle()

        cases = mutableListOf<Case>()
        for(i in 0 until list.size){
            val case = Case(list[i], i)
            cases.add(case)
        }
    }

    // Créer les views qui sont associé aux cases par l'index
    private fun initViews() {
        // Créez une vue pour chaque case dans le tableau de cases
        for (i in 0 until ROWS * COLUMNS) {
            val view = TextView(this).apply {
                text = "?"
                textSize = 75f
                gravity = Gravity.CENTER_HORIZONTAL
                setOnClickListener { onCaseClicked(i) }
            }
            gridLayout.addView(view)
        }
    }

    private fun onCaseClicked(index: Int) {
        val case = cases[index]
        if (case.visible) {
            // Si la case est déjà visible, ignorer le clic
            return
        }

        // Afficher la valeur de la case
        val textView = gridLayout.getChildAt(index) as TextView
        textView.text = case.value.toString()

        // Marquer la case comme visible
        case.visible = true

        var list = getVisibleCasesCount()
        if(list.size == 2){
            if(list[0].value == list[1].value){
                list[0].found = true
                list[1].found = true

                // augmenter les points
                points++
                pointsTextView.setText("Points: " + points.toString())

                var casesFound = getFoundCasesCount()
                if(casesFound == ROWS * COLUMNS){
                    onVictory()
                }
            }
            else{
                // Mettre les élément non-cliquable
                toggleUI(false)

                // cacher les cases après 2 sec
                val handler = Handler()
                handler.postDelayed({
                    hideCases(list)
                }, 2000)

                // baisser la vie
                life--
                lifeTextView.setText("Vie: " + life.toString())

                if(life == 0){
                    defeatDialog.show()
                }
            }
        }
    }

    fun getVisibleCasesCount() : List<Case>{
        var list = mutableListOf<Case>()
        for(i in 0 until cases.size){
            if(cases[i].visible == true and cases[i].found == false){
                list.add(cases[i])
            }
        }
        return list
    }

    fun getFoundCasesCount() : Int{
        var cnt = 0
        for(i in 0 until cases.size){
            if(cases[i].found == true){
                cnt++
            }
        }
        return cnt
    }

    fun hideCases(casesToHide: List<Case>){
        for(i in 0 until casesToHide.size){
            // reset case
            casesToHide[i].visible = false

            // Afficher la valeur de la case
            val textView = casesToHide[i].idx?.let { gridLayout.getChildAt(it) } as TextView
            textView.text = "?"

            // Remettre les éléments cliquable
            toggleUI(true)
        }
    }

    fun toggleUI(enabled: Boolean){
        if(enabled){
            for(i in 0 until gridLayout.childCount){
                val textView = gridLayout.getChildAt(i) as TextView
                textView.isClickable = true
            }
        }
        else{
            for(i in 0 until gridLayout.childCount){
                val textView = gridLayout.getChildAt(i) as TextView
                textView.isClickable = false
            }
        }
    }

    fun restartGame(){
        cases.clear()
        initCases()

        for(i in 0 until cases.size){
            cases[i].visible = false
            cases[i].found = false
        }

        for(i in 0 until gridLayout.childCount){
            val textView = gridLayout.getChildAt(i) as TextView
            textView.isClickable = true
            textView.setText("?")
        }

        life = MAXHEALTH
        lifeTextView.setText("Vie: " + life.toString())

        points = 0
        pointsTextView.setText("Points: " + points.toString())
    }

    fun onVictory(){
        val newName = MyDictionary.getFirstUnkownPersonName()
        if(newName != null){
            var builder = AlertDialog.Builder(this)
            builder.setIcon(ImageRessources.getImageResource(newName))
            builder.setTitle("Bien joué!")
            builder.setMessage("Vous avez découvert " + newName)
            builder.setPositiveButton("Voir infos") {dialog, wich->
                val intent = Intent(this, FullDescriptionActivity::class.java).apply {
                    putExtra("NAME", newName)
                }
                startActivity(intent)
            }
            builder.setNegativeButton("Plus tard") {dialog, wich->
                restartGame()
            }
            victoryDialog = builder.create()
            victoryDialog.show()

            // indiquer que le pionnier est découvert
            MyDictionary.set(newName, true)
        }
    }
}