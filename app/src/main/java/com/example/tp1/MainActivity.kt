package com.example.tp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.view.View
import android.widget.Button

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity()
{
    var mainLayout: LinearLayout? = null
    var childLinearLayout: LinearLayout? = null
    var scrollView: ScrollView? = null
    var unlockButton: Button? = null
    lateinit var pionniersArray: JSONArray

    // dict contenant l'image et les text pour chaque pionniers
    val images = mutableMapOf<String, ImageView>()
    val descriptions = mutableMapOf<String, MutableList<TextView>>()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        // Création du layout principal
        mainLayout = LinearLayout(this);
        mainLayout?.orientation = LinearLayout.VERTICAL

        // params du layout principal
        var params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);

        // création du layout contenant les personnes
        childLinearLayout = LinearLayout(this)
        childLinearLayout?.orientation = LinearLayout.VERTICAL

        // création du scroll view
        scrollView = ScrollView(this)
        scrollView?.addView(childLinearLayout)

        // Création du bouton pour lancer le mini game
        unlockButton = Button(this)
        unlockButton?.setText("Unlock Pioneers")
        unlockButton?.setOnClickListener{
            onUnlockPioneersButtonClick()
        }

        mainLayout?.addView(unlockButton)
        mainLayout?.addView(scrollView)

        // Récupérer les infos du fichier json
        val bufferedReader = applicationContext.assets.open("pionniers.json").bufferedReader()
        val jsonText = bufferedReader.use { it.readText() }
        bufferedReader.close()
        val jsonObject = JSONObject(jsonText)
        pionniersArray = jsonObject.getJSONArray("pionniers")

        for(i in 0 until pionniersArray.length())
        {
            val pionnier = pionniersArray.getJSONObject(i)
            val name = pionnier.getString("name")

            // Initialiser chaque pionniers comme non découvert
            MyDictionary.set(name, false)

            // créer un layout représentant la case complète pour ce pionnier
            val caseLayout = LinearLayout(this)
            caseLayout.orientation = LinearLayout.HORIZONTAL

            // créer une image
            val imageView = ImageView(this)
            imageView.isClickable = true
            imageView.setOnClickListener {
                onImageClicked(name)
            }
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            caseLayout.addView(imageView)
            images[name] = imageView

            // Créer un layout qui contiendra le texte associé au pionnier (nom + description)
            val textLayout = LinearLayout(this)
            textLayout.orientation = LinearLayout.VERTICAL
            caseLayout.addView(textLayout)

            // créer la liste de text view du dict
            descriptions[name] = mutableListOf()

            // créer les texte view pour le nom et pour la description
            val textName = TextView(this)
            textLayout.addView(textName)
            val listTextView = descriptions[name]
            listTextView?.add(textName)

            val textDescription = TextView(this)
            textDescription.setText("description of something like that")
            textLayout.addView(textDescription)
            listTextView?.add(textDescription)

            childLinearLayout?.addView(caseLayout)
        }

        setContentView(mainLayout)
    }

    override fun onStart() {
        super.onStart()
        println("start")

        for(i in 0 until pionniersArray.length())
        {
            val pionnier = pionniersArray.getJSONObject(i)
            val name = pionnier.getString("name")

            // récupérer la liste des TextView associé à chaque pionnier [0] = Name [1] = description
            val listeTextView = descriptions[name]

            if(MyDictionary.get(name) == true){ // Si le pionnier est découvert
                Picasso.get().load(ImageRessources.getImageResource(name)).resize(200, 200).centerCrop().into(images[name])
                val listeTextView = descriptions[name]
                listeTextView?.get(0)?.setText(name)
                listeTextView?.get(1)?.setText(PersonDescription.getDescriptionShort(name))
            }
            else{ // Si le pionnier n'est pas découvert
                Picasso.get().load(ImageRessources.getImageResource("Unknown")).resize(200, 200).centerCrop().into(images[name])
                listeTextView?.get(0)?.setText("Un Pionnier")
                listeTextView?.get(1)?.setText("Aucune description")
            }
        }
    }

    fun onImageClicked(name: String){

        val isKnown: Boolean? = MyDictionary.get(name)
        if(isKnown == true){
            val intent = Intent(this, FullDescriptionActivity::class.java).apply {
                putExtra("NAME", name)
            }
            startActivity(intent)
        }
        else{
            println("Non découvert")
        }
    }

    fun onUnlockPioneersButtonClick(){
        val intent = Intent(this, Game::class.java)
        startActivity(intent)
    }
}