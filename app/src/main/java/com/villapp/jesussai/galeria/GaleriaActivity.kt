package com.villapp.jesussai.galeria

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import android.widget.Toast
import com.villapp.jesussai.R
import com.villapp.jesussai.app.MainActivity

class GaleriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_galeria)
        val gridView = findViewById<GridView>(R.id.gridView) as GridView

        val arrayListImage = ArrayList<Int>()

        arrayListImage.add(R.drawable.pez)
        arrayListImage.add(R.drawable.paloma)
        arrayListImage.add(R.drawable.catrinas)

        val name = arrayOf("pez.jpeg", "paloma.jpg", "catrinas.jpg")

        val customAdapter = CustomAdapter(this@GaleriaActivity, arrayListImage, name)


        gridView.adapter = customAdapter

        gridView.setOnItemClickListener { adapterView, parent, position, l ->
            //Toast.makeText(this@GaleriaActivity, "Click on : " + name[position], Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selector", name[position])
            // To pass any data to next activity
            // start your next activity
            startActivity(intent)
        }

    }


}