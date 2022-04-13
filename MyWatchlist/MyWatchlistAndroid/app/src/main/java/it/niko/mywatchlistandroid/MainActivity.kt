package it.niko.mywatchlistandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import it.niko.mywatchlistandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}