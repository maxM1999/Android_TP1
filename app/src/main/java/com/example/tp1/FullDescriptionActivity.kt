package com.example.tp1

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class FullDescriptionActivity: AppCompatActivity() {
    lateinit var mainLayout: LinearLayout
    lateinit var image: ImageView
    lateinit var desc: TextView
    lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainLayout = LinearLayout(this)
        mainLayout.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
        image = ImageView(this)
        desc = TextView(this)

        mainLayout.addView(image)
        mainLayout.addView(desc)

        scrollView = ScrollView(this)
        scrollView.addView(mainLayout)

        setContentView(scrollView, params)

    }

    override fun onStart() {
        super.onStart()

        val name = intent.getStringExtra("NAME")
        if(name != null){
            Picasso.get().load(ImageRessources.getImageResource(name)).resize(200, 200).centerCrop().into(image)
            desc.setText(PersonDescription.getDescriptionLong(name))
        }

    }
}