package it.niko.mywatchlistandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayUser = findViewById<TextView>(R.id.tvHello)
        val username = intent.getStringExtra("USERNAME")
        val message = "$username's profile"
        displayUser.text = message

    }
}