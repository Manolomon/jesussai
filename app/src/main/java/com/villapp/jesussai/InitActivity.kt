package com.villapp.jesussai

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.villapp.jesussai.galeria.GaleriaActivity

class InitActivity : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        button = findViewById<Button>(R.id.btn_start)
    }

    fun animar(v: View?) {
        val intent = Intent(this, GaleriaActivity::class.java)
        // To pass any data to next activity
    // start your next activity
    startActivity(intent)
}

    fun irTienda(v: View?) {
        val url = "https://www.jesussai.com/productos"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
